package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.dao.Path;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.RaffleAllocationDao;
import au.org.scoutmaster.dao.RaffleBookDao;
import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.RaffleAllocation;
import au.org.scoutmaster.domain.RaffleAllocation_;
import au.org.scoutmaster.domain.RaffleBook;
import au.org.scoutmaster.domain.RaffleBook_;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.IsNull;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

/**
 * Select a non-contiguous
 *
 * @author bsutton
 *
 */
public class ManualSelectionStep implements WizardStep, SelectStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(ManualSelectionStep.class);

	private final RaffleBookAllocationWizardView setupWizardView;

	private ComboBox allocatedToContact;

	private TextField noOfBooksField;

	private ComboBox issuedBy;

	private final List<Allocation> allocations = new ArrayList<>();

	private TwinColSelect bookSelectionField;

	private ValidatingFieldGroup<RaffleAllocation> group;

	public ManualSelectionStep(final RaffleBookAllocationWizardView setupWizardView)
	{
		this.setupWizardView = setupWizardView;
	}

	@Override
	public String getCaption()
	{
		return "Select Contact";
	}

	@Override
	public Component getContent()
	{
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		final Label label = new Label("<h1>Select the Issuer (you) and the person to allocate books to.</h1>",
				ContentMode.HTML);
		label.setContentMode(ContentMode.HTML);

		layout.addComponent(label);

		final Raffle raffle = this.setupWizardView.getRaffle();

		// The container just lets us use a fieldgroup which we need to bind the
		// twincol selection to.
		final RaffleAllocationDao daoRaffleAllocation = new DaoFactory().getRaffleAllocationDao();
		final JPAContainer<RaffleAllocation> allocationContainer = daoRaffleAllocation.createVaadinContainer();

		this.group = new ValidatingFieldGroup<RaffleAllocation>(allocationContainer, RaffleAllocation.class);
		this.group.setItemDataSource(allocationContainer.createEntityItem(new RaffleAllocation()));
		final MultiColumnFormLayout<RaffleAllocation> overviewForm = new MultiColumnFormLayout<RaffleAllocation>(2,
				this.group);
		overviewForm.setColumnFieldWidth(0, 200);
		overviewForm.setColumnFieldWidth(1, 200);
		final FormHelper<RaffleAllocation> formHelper = overviewForm.getFormHelper();

		this.issuedBy = formHelper.new EntityFieldBuilder<Contact>().setLabel("Issued By")
				.setField(RaffleAllocation_.issuedBy).setListFieldName(Contact_.fullname).build();
		this.issuedBy.setFilteringMode(FilteringMode.CONTAINS);
		this.issuedBy.setTextInputAllowed(true);
		this.issuedBy.setRequired(true);
		overviewForm.newLine();

		this.allocatedToContact = formHelper.new EntityFieldBuilder<Contact>().setLabel("Allocate To")
				.setField(RaffleAllocation_.allocatedTo).setListFieldName(Contact_.fullname).build();
		this.allocatedToContact.setFilteringMode(FilteringMode.CONTAINS);
		this.allocatedToContact.setTextInputAllowed(true);
		this.allocatedToContact.setDescription("The Contact to issue tickets to.");
		this.allocatedToContact.setRequired(true);

		overviewForm.colspan(2);

		final RaffleBookDao daoRaffleBook = new DaoFactory().getRaffleBookDao();
		final JPAContainer<RaffleBook> container = daoRaffleBook.createVaadinContainer();
		container.addContainerFilter(new And(new IsNull(RaffleBook_.raffleAllocation.getName()), new Compare.Equal(
				new Path(RaffleBook_.raffle, BaseEntity_.id).getName(), raffle.getId())));

		this.bookSelectionField = formHelper.new TwinColSelectBuilder<RaffleBook>().setLabel("Books")
				.setField(RaffleAllocation_.books).setListFieldName(RaffleBook_.firstNo).setContainer(container)
				.setLeftColumnCaption("Available (First No.)").setRightColumnCaption("Allocated").build();

		layout.addComponent(overviewForm);

		final Label labelAllocate = new Label("<h1>Click Next to allocate the selected books.</h1>", ContentMode.HTML);

		layout.addComponent(labelAllocate);

		this.issuedBy.focus();

		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		boolean advance = false;

		final Raffle raffle = this.setupWizardView.getRaffle();

		if (this.group.isValid())
		{
			@SuppressWarnings("unchecked")
			final Collection<Long> selectedIds = (Collection<Long>) this.bookSelectionField.getValue();
			preAllocateBooks(raffle, getAllocatedContact(), selectedIds);
			advance = true;
		}
		else
		{
			Notification.show("Please fill in the form correctly first.");
		}

		return advance;
	}

	private void preAllocateBooks(final Raffle raffle, final Contact allocatedTo, final Collection<Long> bookids)
	{
		final List<RaffleBook> bookAllocation = new ArrayList<>();

		final RaffleBookDao raffleBookDao = new DaoFactory().getRaffleBookDao();

		for (final Long bookid : bookids)
		{
			final RaffleBook book = raffleBookDao.findById(bookid);
			bookAllocation.add(book);
		}
		final Allocation allocation = new Allocation(allocatedTo, bookAllocation);
		this.allocations.add(allocation);

	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	@SuppressWarnings("unchecked")
	public Contact getAllocatedContact()
	{
		return ((EntityItem<Contact>) this.allocatedToContact.getItem(this.allocatedToContact.getValue())).getEntity();
	}

	public int getBookCount()
	{
		return Long.valueOf(this.noOfBooksField.getValue()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Contact getIssuedByContact()
	{
		return ((EntityItem<Contact>) this.issuedBy.getItem(this.issuedBy.getValue())).getEntity();
	}

	@Override
	public List<Allocation> getAllocations()
	{
		return this.allocations;
	}

}
