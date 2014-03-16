package au.org.scoutmaster.views.wizards.raffle.importBooks;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.org.scoutmaster.domain.Organisation;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.RaffleBook;
import au.org.scoutmaster.util.SMNotification;

import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class TicketRangeStep implements WizardStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(TicketRangeStep.class);
	private RaffleBookImportWizardView wizard;
	private TextField firstTicketNoField;
	private TextField noOfBooksField;
	private TextField lastTicketNoField;

	public TicketRangeStep(RaffleBookImportWizardView setupWizardView)
	{
		this.wizard = setupWizardView;
	}

	@Override
	public String getCaption()
	{
		return "Ticket Range";
	}

	@Override
	public Component getContent()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		MultiColumnFormLayout<Organisation> formLayout = new MultiColumnFormLayout<>(1, null);
		formLayout.setColumnLabelWidth(0, 150);
		formLayout.setColumnFieldWidth(0, 250);
		formLayout.setSizeFull();

		Label label = new Label("<h1>Enter the details for the group of books you want to import.</h1>",
				ContentMode.HTML);

		layout.addComponent(label);
		layout.addComponent(formLayout);

		firstTicketNoField = formLayout.addTextField("First Book Ticket No.");
		noOfBooksField = formLayout.addTextField("No. of contiguous Books");
		noOfBooksField.setDescription("The no of Books to be imported. They must be in a contiguous number range!");
		lastTicketNoField = formLayout.addTextField("Last Ticket No. of Last Book");
		lastTicketNoField
				.setDescription("Enter the ticket no of the 'last' ticket of the 'last' book. This is used to check that all of the details are correct.");

		firstTicketNoField.addValidator(new IntegerRangeValidator("First Ticket No", 0, 6000000));
		noOfBooksField.addValidator(new IntegerRangeValidator("First Ticket No", 1, 1000));

		Label labelImport = new Label("<h1>Clicking Next will import the books!</h1>", ContentMode.HTML);

		layout.addComponent(labelImport);

		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		boolean advance = false;
		Raffle raffle = wizard.getRaffle();

		int firstTicketNo = Long.valueOf(firstTicketNoField.getValue()).intValue();
		int lastTicketNo = Long.valueOf(lastTicketNoField.getValue()).intValue();
		int noOfBooks = Long.valueOf(noOfBooksField.getValue()).intValue();
		int ticketsPerBook = raffle.getTicketsPerBook();

		RaffleBook book = findFirstDuplicateBook(raffle, firstTicketNo, lastTicketNo);

		if (book != null)
		{
			SMNotification.show("Please check book no.s as the book: " + book.toString()
					+ " has already been imported.", Type.WARNING_MESSAGE);

		}
		else if (firstTicketNo + (noOfBooks * ticketsPerBook) != lastTicketNo)
		{
			int expectedLast = firstTicketNo + (noOfBooks * ticketsPerBook);
			SMNotification
					.show("Please check the first and last ticket no. and the no. of books as the values you entered don't add up. We expected the last ticket to be: "
							+ expectedLast, Type.WARNING_MESSAGE);
		}
		else
			advance = true;

		return advance;
	}

	private RaffleBook findFirstDuplicateBook(Raffle raffle, int firstTicketNo, int lastTicketNo)
	{
		RaffleBook first = null;
		Set<RaffleBook> books = raffle.getImportedBooks();

		for (RaffleBook book : books)
		{
			int firstTicket = book.getFirstNo().intValue();
			int lastTicket = book.getFirstNo() + book.getTicketCount();

			if (firstTicket >= firstTicketNo && firstTicket <= lastTicketNo
					|| (lastTicket >= firstTicketNo && lastTicket <= lastTicketNo))
			{
				first = book;
				break;
			}
		}
		return first;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	public int getFirstBookNo()
	{
		return Long.valueOf(firstTicketNoField.getValue()).intValue();
	}

	public int getLastBookNo()
	{
		return Long.valueOf(lastTicketNoField.getValue()).intValue();
	}

	public int getBookCount()
	{
		return Long.valueOf(noOfBooksField.getValue()).intValue();
	}

}
