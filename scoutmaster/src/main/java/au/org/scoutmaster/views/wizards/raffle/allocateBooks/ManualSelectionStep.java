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
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.RaffleAllocation;
import au.org.scoutmaster.domain.RaffleAllocation_;
import au.org.scoutmaster.domain.RaffleBook;
import au.org.scoutmaster.domain.RaffleBook_;
import au.org.scoutmaster.domain.Raffle_;
import au.org.scoutmaster.util.SMNotification;

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

	private RaffleBookAllocationWizardView setupWizardView;

	private ComboBox allocatedToContact;

	private TextField noOfBooksField;

	private ComboBox issuedBy;

	private List<Allocation> allocations = new ArrayList<>();

	private TwinColSelect bookSelectionField;

	private ValidatingFieldGroup<RaffleAllocation> group;

	public ManualSelectionStep(RaffleBookAllocationWizardView setupWizardView)
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
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		Label label = new Label("<h1>Select the Issuer (you) and the person to allocate books to.</h1>",
				ContentMode.HTML);
		label.setContentMode(ContentMode.HTML);

		layout.addComponent(label);
		
		Raffle raffle = setupWizardView.getRaffle();

		// The container just lets us use a fieldgroup which we need to bind the
		// twincol selection to.
		RaffleAllocationDao daoRaffleAllocation = new DaoFactory().getRaffleAllocationDao();
		JPAContainer<RaffleAllocation> allocationContainer = daoRaffleAllocation.createVaadinContainer();

		group = new ValidatingFieldGroup<RaffleAllocation>(allocationContainer,
				RaffleAllocation.class);
		group.setItemDataSource(allocationContainer.createEntityItem(new RaffleAllocation()));
		MultiColumnFormLayout<RaffleAllocation> overviewForm = new MultiColumnFormLayout<RaffleAllocation>(2, group);
		overviewForm.setColumnFieldWidth(0, 200);
		overviewForm.setColumnFieldWidth(1, 200);
		FormHelper<RaffleAllocation> formHelper = overviewForm.getFormHelper();

		issuedBy = formHelper.new EntityFieldBuilder<Contact>().setLabel("Issued By")
				.setField(RaffleAllocation_.issuedBy).setListFieldName(Contact_.fullname).build();
		issuedBy.setFilteringMode(FilteringMode.CONTAINS);
		issuedBy.setTextInputAllowed(true);
		issuedBy.setRequired(true);
		overviewForm.newLine();

		allocatedToContact = formHelper.new EntityFieldBuilder<Contact>().setLabel("Allocate To")
				.setField(RaffleAllocation_.allocatedTo).setListFieldName(Contact_.fullname).build();
		allocatedToContact.setFilteringMode(FilteringMode.CONTAINS);
		allocatedToContact.setTextInputAllowed(true);
		allocatedToContact.setDescription("The Contact to issue tickets to.");
		allocatedToContact.setRequired(true);

		overviewForm.colspan(2);

		RaffleBookDao daoRaffleBook = new DaoFactory().getRaffleBookDao();
		JPAContainer<RaffleBook> container = daoRaffleBook.createVaadinContainer();
		container.addContainerFilter(new And(new IsNull(RaffleBook_.raffleAllocation.getName()), new Compare.Equal(new Path(RaffleBook_.raffle, Raffle_.id).getName(), raffle.getId())));

		this.bookSelectionField = formHelper.new TwinColSelectBuilder<RaffleBook>().setLabel("Books")
				.setField(RaffleAllocation_.books).setListFieldName(RaffleBook_.firstNo).setContainer(container)
				.setLeftColumnCaption("Available (First No.)").setRightColumnCaption("Allocated").build();

		layout.addComponent(overviewForm);

		Label labelAllocate = new Label("<h1>Click Next to allocate the selected books.</h1>", ContentMode.HTML);

		layout.addComponent(labelAllocate);

		issuedBy.focus();

		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		boolean advance = false;

		Raffle raffle = setupWizardView.getRaffle();

		if (group.isValid())
		{
			@SuppressWarnings("unchecked")
			Collection<Long> selectedIds = (Collection<Long>) bookSelectionField.getValue();
			preAllocateBooks(raffle, getAllocatedContact(), selectedIds);
			advance = true;
		}
		else
		{
			SMNotification.show("Please fill in the form correctly first.");
		}

		return advance;
	}

	private void preAllocateBooks(Raffle raffle, Contact allocatedTo, Collection<Long> bookids)
	{
		List<RaffleBook> bookAllocation = new ArrayList<>();

		RaffleBookDao raffleBookDao = new DaoFactory().getRaffleBookDao();
		
		for (Long bookid : bookids)
		{
			RaffleBook book = raffleBookDao.findById(bookid);
			bookAllocation.add(book);
		}
		Allocation allocation = new Allocation(allocatedTo, bookAllocation);
		this.allocations.add(allocation);

	}

	public boolean onBack()
	{
		return true;
	}

	@SuppressWarnings("unchecked")
	public Contact getAllocatedContact()
	{
		return (Contact) ((EntityItem<Contact>) allocatedToContact.getItem(allocatedToContact.getValue())).getEntity();
	}

	public int getBookCount()
	{
		return Long.valueOf(noOfBooksField.getValue()).intValue();
	}

	@SuppressWarnings("unchecked")
	public Contact getIssuedByContact()
	{
		return (Contact) ((EntityItem<Contact>) issuedBy.getItem(issuedBy.getValue())).getEntity();
	}

	@Override
	public List<Allocation> getAllocations()
	{
		return this.allocations;
	}

}
