package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.Path;
import au.org.scoutmaster.dao.RaffleBookDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.RaffleAllocation_;
import au.org.scoutmaster.domain.RaffleBook;
import au.org.scoutmaster.domain.RaffleBook_;
import au.org.scoutmaster.util.SMNotification;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class SingleSelectionStep  implements WizardStep, SelectStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(SingleSelectionStep.class);

	private RaffleBookAllocationWizardView setupWizardView;

	private ComboBox allocatedToContact;

	private TextField noOfBooksField;

	private ComboBox issuedBy;

	private List<Allocation> allocations = new ArrayList<>();

	public SingleSelectionStep(RaffleBookAllocationWizardView setupWizardView)
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
		Label label = new Label("<h1>Select the Contact and no. of books to Allocate to them.</h1>",
				ContentMode.HTML);
		label.setContentMode(ContentMode.HTML);

		layout.addComponent(label);
		
		MultiColumnFormLayout<RaffleBook> overviewForm = new MultiColumnFormLayout<RaffleBook>(1, null);
		overviewForm.setColumnFieldWidth(0, 200);
		FormHelper<RaffleBook> formHelper = overviewForm.getFormHelper();

		issuedBy= formHelper.new EntityFieldBuilder<Contact>()
				.setLabel("Issued By")
				.setField(new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.issuedBy).getName())
				.setListClass(Contact.class)
				.setListFieldName(Contact_.fullname).build();
		issuedBy.setFilteringMode(FilteringMode.CONTAINS);
		issuedBy.setTextInputAllowed(true);

		noOfBooksField = overviewForm.addTextField("No. of Books");
		noOfBooksField.setDescription("The no. of books to allocate to this Contact");

		allocatedToContact = formHelper.new EntityFieldBuilder<Contact>()
				.setLabel("Allocate To")
				.setField(new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.allocatedTo).getName())
				.setListClass(Contact.class)
				.setListFieldName(Contact_.fullname).build();
		allocatedToContact.setFilteringMode(FilteringMode.CONTAINS);
		allocatedToContact.setTextInputAllowed(true);
		allocatedToContact.setDescription("The Contact to issue tickets to.");
		
		
		allocatedToContact.setDescription("The Contact that issue tickets.");

		
		layout.addComponent(overviewForm);
		
		Label labelAllocate = new Label("<h1>Clicking Next will Allocate the books!</h1>", ContentMode.HTML);

		layout.addComponent(labelAllocate);
		
		allocatedToContact.focus();


		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		// check that we have enough books available.
		RaffleBookDao daoRaffleBook = new DaoFactory().getRaffleBookDao();
		
		Raffle raffle = setupWizardView.getRaffle();
		
		List<RaffleBook> books = daoRaffleBook.findAllUnallocated(raffle);
		
		int requiredBooks = Long.valueOf(noOfBooksField.getValue()).intValue();
		
		boolean enough = books.size() >= requiredBooks;
		
		if (!enough)
			SMNotification.show("There are only " + books.size() + " books available to allocate.", Type.WARNING_MESSAGE);
		else
			preAllocateBooks(raffle, getAllocatedContact(), books, requiredBooks);
		
		
		return enough;
	}


	private void preAllocateBooks(Raffle raffle, Contact allocatedTo, List<RaffleBook> books, int requiredBooks)
	{
		List<RaffleBook> bookAllocation = new ArrayList<>();
		
		for (int i = 0; i < requiredBooks; i++)
		{
			bookAllocation.add(books.get(i));
		}
		Allocation allocation = new Allocation(allocatedTo, books);
		this.allocations .add(allocation);
		
	}

	public boolean onBack()
	{
		return true;
	}

	@SuppressWarnings("unchecked")
	public Contact getAllocatedContact()
	{
		return (Contact) ((EntityItem<Contact>)allocatedToContact.getItem(allocatedToContact.getValue())).getEntity();
	}

	public int getBookCount()
	{
		return Long.valueOf(noOfBooksField.getValue()).intValue();
	}

	@SuppressWarnings("unchecked")
	public Contact getIssuedByContact()
	{
		return (Contact) ((EntityItem<Contact>)issuedBy.getItem(issuedBy.getValue())).getEntity();
	}

	@Override
	public List<Allocation> getAllocations()
	{
		return this.allocations;
	}

}
