package au.org.scoutmaster.views;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.ChildCrudView;
import au.com.vaadinutils.crud.CrudAction;
import au.com.vaadinutils.crud.CrudActionDelete;
import au.com.vaadinutils.crud.CrudActionPrint;
import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.dao.Path;
import au.com.vaadinutils.jasper.filter.ReportFilterUIBuilder;
import au.com.vaadinutils.jasper.parameter.ReportParameterConstant;
import au.com.vaadinutils.jasper.ui.JasperReportProperties;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.application.ScoutmasterViewEnum;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.RaffleBookDao;
import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.RaffleAllocation;
import au.org.scoutmaster.domain.RaffleAllocation_;
import au.org.scoutmaster.domain.RaffleBook;
import au.org.scoutmaster.jasper.SMJasperReportProperties;
import net.sf.jasperreports.engine.JRException;

public class RaffleAllocationChildView extends ChildCrudView<Raffle, RaffleAllocation>
{
	private static final long serialVersionUID = 1L;

	public RaffleAllocationChildView(final BaseCrudView<Raffle> parentCrud)
	{
		super(parentCrud, Raffle.class, RaffleAllocation.class, BaseEntity_.id, RaffleAllocation_.raffle.getName());

		final JPAContainer<RaffleAllocation> container = new DaoFactory().getRaffleAllocationDao()
				.createVaadinContainer();
		// container.sort(new String[]
		// { RaffleAllocation_.dateAllocated.getName(),
		// RaffleAllocation_.allocatedTo.getName() }, new boolean[]
		// { true, true });

		final Builder<RaffleAllocation> builder = new HeadingPropertySet.Builder<RaffleAllocation>();
		builder.addColumn("Allocated To", RaffleAllocation_.allocatedTo)
				.addColumn("Date Allocated", RaffleAllocation_.dateAllocated)
				.addColumn("Issued By", RaffleAllocation_.issuedBy)
				.addColumn("Date Issued", RaffleAllocation_.dateIssued).addColumn("Books", "bookCount");

		super.init(RaffleAllocation.class, container, builder.build());
	}

	class AllocationCrudActionPrint extends CrudActionPrint<RaffleAllocation>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected JasperReportProperties prepareReport(final EntityItem<RaffleAllocation> entity) throws JRException
		{
			final RaffleAllocation allocation = entity.getEntity();

			final ReportFilterUIBuilder builder = new ReportFilterUIBuilder();
			builder.addField(new ReportParameterConstant<Long>("allocationIds", allocation.getId()));

			final SMJasperReportProperties properties = new SMJasperReportProperties(
					ScoutmasterViewEnum.RaffleAllocationsReport)
			{
				@Override
				public String getReportTitle()
				{
					return "Raffle Allocation";
				}

				@Override
				public String getReportFileName()
				{
					return "RaffleAllocation.jasper";
				}

				@Override
				public ReportFilterUIBuilder getFilterBuilder()
				{
					ReportFilterUIBuilder builder = new ReportFilterUIBuilder();

					ReportParameterConstant<String> param = new ReportParameterConstant<String>("group_id",
							"" + SMSession.INSTANCE.getGroup().getId());
					builder.getReportParameters().add(param);

					return builder;
				}
			};

			return properties;
		}

		@Override
		public boolean showPreparingDialog()
		{
			// TODO Auto-generated method stub
			return false;
		}
	}

	@Override
	protected List<CrudAction<RaffleAllocation>> getCrudActions()
	{
		final List<CrudAction<RaffleAllocation>> actions = new LinkedList<CrudAction<RaffleAllocation>>();
		final CrudAction<RaffleAllocation> crudAction = new CrudActionDelete<RaffleAllocation>();
		actions.add(crudAction);

		actions.add(new AllocationCrudActionPrint());

		return actions;
	}

	@Override
	protected void preChildDelete(final Object entityId)
	{
		// If the allocation is being deleted then we need to detach all
		// RaffleBooks from this allocation.

		final RaffleBookDao daoRaffleBook = new DaoFactory().getRaffleBookDao();

		final List<RaffleBook> books = daoRaffleBook.findByAllocation((Long) entityId);

		for (final RaffleBook book : books)
		{
			book.setRaffleAllocation(null);
			daoRaffleBook.merge(book);
		}

	}

	@Override
	protected Component buildEditor(final ValidatingFieldGroup<RaffleAllocation> fieldGroup2)
	{
		final MultiColumnFormLayout<RaffleAllocation> overviewForm = new MultiColumnFormLayout<RaffleAllocation>(1,
				this.fieldGroup);
		overviewForm.setColumnFieldWidth(0, 240);
		overviewForm.setColumnLabelWidth(0, 110);
		// overviewForm.setColumnExpandRatio(0, 1.0f);
		overviewForm.setSizeFull();

		final FormHelper<RaffleAllocation> formHelper = overviewForm.getFormHelper();

		final ComboBox allocatedTo = formHelper.new EntityFieldBuilder<Contact>().setLabel("Allocated To")
				.setField(RaffleAllocation_.allocatedTo).setListFieldName(Contact_.fullname).build();
		allocatedTo.setFilteringMode(FilteringMode.CONTAINS);
		allocatedTo.setTextInputAllowed(true);
		allocatedTo.setNullSelectionAllowed(true);
		allocatedTo.setDescription("The person the book has been allocated to.");

		overviewForm.bindDateField("Date Allocated", RaffleAllocation_.dateAllocated, "yyyy-MM-dd", Resolution.DAY);

		final ComboBox issuedBy = formHelper.new EntityFieldBuilder<Contact>().setLabel("Issued By")
				.setField(RaffleAllocation_.issuedBy).setListFieldName(Contact_.fullname).build();
		issuedBy.setFilteringMode(FilteringMode.CONTAINS);
		issuedBy.setTextInputAllowed(true);
		issuedBy.setDescription("The leader that issued the book to the member.");
		issuedBy.setNullSelectionAllowed(true);

		overviewForm.bindDateField("Date Issued", RaffleAllocation_.dateIssued, "yyyy-MM-dd", Resolution.DAY);

		final TextField bookCountField = overviewForm.bindTextField("Book Count", "bookCount");
		bookCountField.setReadOnly(true);

		overviewForm.bindTextAreaField("Notes", RaffleAllocation_.notes, 6);

		return overviewForm;
	}

	@Override
	protected Filter getContainerFilter(final String filterString, final boolean advancedSearchActive)
	{
		return new FilterBuilder()
				.or(new SimpleStringFilter(new Path(RaffleAllocation_.issuedBy, Contact_.fullname).getName(),
						filterString, true, false))
				.or(new SimpleStringFilter(new Path(RaffleAllocation_.allocatedTo, Contact_.fullname).getName(),
						filterString, true, false))
				.or(new SimpleStringFilter(RaffleAllocation_.dateIssued.getName(), filterString, true, false))
				.or(new SimpleStringFilter(RaffleAllocation_.dateAllocated.getName(), filterString, true, false))
				.build();
	}

	@Override
	public void associateChild(final Raffle newParent, final RaffleAllocation child)
	{
		newParent.addRaffleAllocation(child);
	}

	static class FilterBuilder
	{
		ArrayList<Filter> filters = new ArrayList<>();

		FilterBuilder or(final Filter filter)
		{
			this.filters.add(filter);

			return this;
		}

		Or build()
		{
			final Filter[] aFilters = new Filter[1];
			return new Or(this.filters.toArray(aFilters));
		}
	}

	@Override
	public String getNewButtonActionLabel()
	{
		return "New Allocation";
	}

	@Override
	public SingularAttribute<RaffleAllocation, String> getGuidAttribute()
	{
		return RaffleAllocation_.guid;
	}
}
