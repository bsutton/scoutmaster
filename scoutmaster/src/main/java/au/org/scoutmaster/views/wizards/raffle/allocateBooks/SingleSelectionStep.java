package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.com.vaadinutils.dao.Path;
import au.org.scoutmaster.dao.DaoFactory;
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

public class SingleSelectionStep implements WizardStep, SelectStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(SingleSelectionStep.class);

	private final RaffleBookAllocationWizardView setupWizardView;

	private ComboBox allocatedToContact;

	private TextField noOfBooksField;

	private ComboBox issuedBy;

	private final List<Allocation> allocations = new ArrayList<>();

	public SingleSelectionStep(final RaffleBookAllocationWizardView setupWizardView)
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
		final Label label = new Label("<h1>Select the Contact and no. of books to Allocate to them.</h1>",
				ContentMode.HTML);
		label.setContentMode(ContentMode.HTML);

		layout.addComponent(label);

		final MultiColumnFormLayout<RaffleBook> overviewForm = new MultiColumnFormLayout<RaffleBook>(1, null);
		overviewForm.setColumnFieldWidth(0, 200);
		final FormHelper<RaffleBook> formHelper = overviewForm.getFormHelper();

		this.issuedBy = formHelper.new EntityFieldBuilder<Contact>().setLabel("Issued By")
				.setField(new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.issuedBy).getName())
				.setListClass(Contact.class).setListFieldName(Contact_.fullname).build();
		this.issuedBy.setFilteringMode(FilteringMode.CONTAINS);
		this.issuedBy.setTextInputAllowed(true);

		this.noOfBooksField = overviewForm.addTextField("No. of Books");
		this.noOfBooksField.setDescription("The no. of books to allocate to this Contact");

		this.allocatedToContact = formHelper.new EntityFieldBuilder<Contact>().setLabel("Allocate To")
				.setField(new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.allocatedTo).getName())
				.setListClass(Contact.class).setListFieldName(Contact_.fullname).build();
		this.allocatedToContact.setFilteringMode(FilteringMode.CONTAINS);
		this.allocatedToContact.setTextInputAllowed(true);
		this.allocatedToContact.setDescription("The Contact to issue tickets to.");

		this.allocatedToContact.setDescription("The Contact that issue tickets.");

		layout.addComponent(overviewForm);

		final Label labelAllocate = new Label("<h1>Clicking Next will Allocate the books!</h1>", ContentMode.HTML);

		layout.addComponent(labelAllocate);

		this.issuedBy.focus();

		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		// check that we have enough books available.
		final RaffleBookDao daoRaffleBook = new DaoFactory().getRaffleBookDao();

		final Raffle raffle = this.setupWizardView.getRaffle();

		final List<RaffleBook> books = daoRaffleBook.findAllUnallocated(raffle);

		final int requiredBooks = Long.valueOf(this.noOfBooksField.getValue()).intValue();

		final boolean enough = books.size() >= requiredBooks;

		if (!enough)
		{
			SMNotification.show("There are only " + books.size() + " books available to allocate.",
					Type.WARNING_MESSAGE);
		}
		else
		{
			preAllocateBooks(raffle, getAllocatedContact(), books, requiredBooks);
		}

		return enough;
	}

	private void preAllocateBooks(final Raffle raffle, final Contact allocatedTo, final List<RaffleBook> books,
			final int requiredBooks)
	{
		final List<RaffleBook> bookAllocation = new ArrayList<>();

		for (int i = 0; i < requiredBooks; i++)
		{
			bookAllocation.add(books.get(i));
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
