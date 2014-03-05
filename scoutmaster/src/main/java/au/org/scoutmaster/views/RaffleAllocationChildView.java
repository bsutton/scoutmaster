package au.org.scoutmaster.views;

import java.util.ArrayList;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.ChildCrudView;
import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.RaffleAllocation;
import au.org.scoutmaster.domain.RaffleAllocation_;
import au.org.scoutmaster.domain.Raffle_;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

public class RaffleAllocationChildView extends ChildCrudView<Raffle, RaffleAllocation>
{
	private static final long serialVersionUID = 1L;

	public RaffleAllocationChildView(BaseCrudView<Raffle> parentCrud)
	{
		super(parentCrud, Raffle.class, RaffleAllocation.class, Raffle_.id, RaffleAllocation_.raffle.getName());

		JPAContainer<RaffleAllocation> container = new DaoFactory().getRaffleAllocationDao().createVaadinContainer();
		container.sort(new String[]
		{ RaffleAllocation_.dateAllocated.getName(), RaffleAllocation_.allocatedTo.getName() }, new boolean[]
		{ true, true });

		Builder<RaffleAllocation> builder = new HeadingPropertySet.Builder<RaffleAllocation>();
		builder.addColumn("Allocated To", RaffleAllocation_.allocatedTo)
		.addColumn("Date Allocated",RaffleAllocation_.dateAllocated)
		.addColumn("Issued To", RaffleAllocation_.allocatedTo)
		.addColumn("Date Issued",RaffleAllocation_.dateIssued);
		
		super.init(RaffleAllocation.class, container, builder.build());

	}

	@Override
	protected Component buildEditor(ValidatingFieldGroup<RaffleAllocation> fieldGroup2)
	{
		MultiColumnFormLayout<RaffleAllocation> overviewForm = new MultiColumnFormLayout<RaffleAllocation>(1, this.fieldGroup);
		overviewForm.setColumnFieldWidth(0, 240);
		overviewForm.setColumnLabelWidth(0, 110);
		// overviewForm.setColumnExpandRatio(0, 1.0f);
		overviewForm.setSizeFull();

		
		FormHelper<RaffleAllocation> formHelper = overviewForm.getFormHelper();
		

		ComboBox allocatedTo = formHelper.new EntityFieldBuilder<Contact>()
				.setLabel("Allocated By")
				.setField(RaffleAllocation_.allocatedTo)
				.setListFieldName(Contact_.fullname).build();
		allocatedTo.setFilteringMode(FilteringMode.CONTAINS);
		allocatedTo.setTextInputAllowed(true);
		allocatedTo.setNullSelectionAllowed(true);
		allocatedTo.setDescription("The person the book has been allocated to.");

		overviewForm.bindDateField("Date Allocated", RaffleAllocation_.dateAllocated, "yyyy-MM-dd", Resolution.DAY);
		
		ComboBox issuedBy = formHelper.new EntityFieldBuilder<Contact>()
				.setLabel("Issued By").setField(RaffleAllocation_.issuedBy).setListFieldName(Contact_.fullname).build();
		issuedBy.setFilteringMode(FilteringMode.CONTAINS);
		issuedBy.setTextInputAllowed(true);
		issuedBy.setDescription("The leader that issued the book to the member.");
		issuedBy.setNullSelectionAllowed(true);

		overviewForm.bindDateField("Date Issued", RaffleAllocation_.dateIssued, "yyyy-MM-dd", Resolution.DAY);

		overviewForm.bindTextAreaField("Notes", RaffleAllocation_.notes, 6);
	
		
		return overviewForm;
	}

	@Override
	protected Filter getContainerFilter(String filterString, boolean advancedSearchActive)
	{
		return new FilterBuilder()
		.or(new SimpleStringFilter(RaffleAllocation_.issuedBy.getName(), filterString, true,false))
		.or(new SimpleStringFilter(RaffleAllocation_.allocatedTo.getName(), filterString, true, false))
		.or(new SimpleStringFilter(RaffleAllocation_.dateIssued.getName(), filterString, true, false))
		.or(new SimpleStringFilter(RaffleAllocation_.dateAllocated.getName(), filterString, true, false))
		.build();
	}

	@Override
	public void associateChild(Raffle newParent, RaffleAllocation child)
	{
		newParent.addRaffleAllocation(child);
	}
	
	static class FilterBuilder
	{
		ArrayList<Filter> filters = new ArrayList<>();
		
		FilterBuilder or(Filter filter)
		{
			filters.add(filter);
			
			return this;
		}
		
		Or build()
		{
			Filter[] aFilters = new Filter[1];
			return new Or(filters.toArray(aFilters));
		}
	}
}
