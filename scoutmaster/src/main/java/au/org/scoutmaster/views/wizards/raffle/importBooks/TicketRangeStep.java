package au.org.scoutmaster.views.wizards.raffle.importBooks;

import java.util.Arrays;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.crud.CrudEntity;
import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.com.vaadinutils.fields.FieldValidator;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.RaffleBook;
import au.org.scoutmaster.util.SMNotification;

public class TicketRangeStep implements WizardStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(TicketRangeStep.class);
	private final RaffleBookImportWizardView wizard;
	private TextField firstTicketNoField;
	private TextField noOfBooksField;
	private TextField lastTicketNoField;
	private MultiColumnFormLayout<IntegerCrudEntity> formLayout;

	FieldGroup fieldGroup = new FieldGroup();
	private FieldValidator fieldValidator;

	public TicketRangeStep(final RaffleBookImportWizardView setupWizardView)
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
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);

		this.fieldValidator = new FieldValidator();
		this.formLayout = new MultiColumnFormLayout<IntegerCrudEntity>(1, null); // new
		// ValidatingFieldGroup(dynamicFieldItem));
		this.formLayout.setColumnLabelWidth(0, 150);
		this.formLayout.setColumnFieldWidth(0, 250);
		this.formLayout.setSizeFull();

		final Label label = new Label("<h1>Enter the details for the group of books you want to import.</h1>",
				ContentMode.HTML);

		layout.addComponent(label);
		layout.addComponent(this.formLayout);

		this.firstTicketNoField = this.formLayout.addTextField("First Book Ticket No.");
		this.noOfBooksField = this.formLayout.addTextField("No. of consecutive Books");
		this.noOfBooksField
		.setDescription("The no of Books to be imported. They must be in a consecutive number range!");
		this.lastTicketNoField = this.formLayout.addTextField("Last Ticket No. of Last Book");
		this.lastTicketNoField
				.setDescription("Enter the ticket no of the 'last' ticket of the 'last' book. This is used to check that all of the details are correct.");

		this.firstTicketNoField
		.addValidator(new IntegerRangeValidator("First Ticket No must be an integer", 0, 6000000));
		this.firstTicketNoField.setConverter(new StringToIntegerConverter());
		this.firstTicketNoField.setRequired(true);
		this.fieldValidator.addField(this.firstTicketNoField);

		this.noOfBooksField.addValidator(new IntegerRangeValidator("No. of Books must be an integer", 1, 1000));
		this.noOfBooksField.setConverter(new StringToIntegerConverter());
		this.noOfBooksField.setRequired(true);
		this.fieldValidator.addField(this.noOfBooksField);

		this.lastTicketNoField
		.addValidator(new IntegerRangeValidator("Last Ticket No. must be an integer", 1, 6000000));
		this.lastTicketNoField.setConverter(new StringToIntegerConverter());
		this.lastTicketNoField.setRequired(true);
		this.fieldValidator.addField(this.lastTicketNoField);

		final Label labelImport = new Label("<h1>Clicking Next will import the books!</h1>", ContentMode.HTML);

		layout.addComponent(labelImport);

		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		boolean advance = false;
		final Raffle raffle = this.wizard.getRaffle();

		try
		{
			this.fieldValidator.validate();

			final int firstTicketNo = (Integer) this.firstTicketNoField.getConvertedValue();
			final int lastTicketNo = (Integer) this.lastTicketNoField.getConvertedValue();
			final int noOfBooks = (Integer) this.noOfBooksField.getConvertedValue();
			final int ticketsPerBook = raffle.getTicketsPerBook();

			final RaffleBook book = findFirstDuplicateBook(raffle, firstTicketNo, lastTicketNo);

			if (book != null)
			{
				SMNotification.show("Please check book no.s as the book: " + book.toString()
						+ " has already been imported.", Type.WARNING_MESSAGE);

			}
			else
			{
				final int calcLastTicket = firstTicketNo + noOfBooks * ticketsPerBook - 1;
				if (calcLastTicket != lastTicketNo)
				{
					final int expectedLast = firstTicketNo + noOfBooks * ticketsPerBook - 1;
					SMNotification
							.show("Please check the first and last ticket no. and the no. of books as the values you entered don't add up. We expected the last ticket to be: "
									+ expectedLast, Type.WARNING_MESSAGE);
				}
				else
				{
					advance = true;
				}
			}
		}
		catch (final InvalidValueException e)
		{
			SMNotification.show(e, Type.ERROR_MESSAGE);
		}

		return advance;
	}

	private RaffleBook findFirstDuplicateBook(final Raffle raffle, final int firstTicketNo, final int lastTicketNo)
	{
		RaffleBook first = null;
		final Set<RaffleBook> books = raffle.getImportedBooks();
		final RaffleBook[] sortedBooks = books.toArray(new RaffleBook[]
				{});
		Arrays.sort(sortedBooks, (o1, o2) -> o1.getFirstNo().intValue() - o2.getFirstNo().intValue());

		for (final RaffleBook book : sortedBooks)
		{
			final int firstTicket = book.getFirstNo().intValue();
			final int lastTicket = book.getFirstNo() + book.getTicketCount() - 1;

			if (firstTicket >= firstTicketNo && firstTicket <= lastTicketNo || lastTicket >= firstTicketNo
					&& lastTicket <= lastTicketNo)
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
		return ((Integer) this.firstTicketNoField.getConvertedValue()).intValue();
	}

	public int getLastBookNo()
	{
		return ((Integer) this.lastTicketNoField.getConvertedValue()).intValue();
	}

	public int getBookCount()
	{
		return ((Integer) this.noOfBooksField.getConvertedValue()).intValue();
	}
	
	// HACK so we can continue using the MultiColumnFormLayout
	
	class IntegerCrudEntity implements CrudEntity
	{
		private Long id;

		IntegerCrudEntity(Long Id)
		{
			this.id = Id;
		}

		@Override
		public Long getId()
		{
			return id;
		}

		@Override
		public void setId(Long id)
		{
			this.id = id;
		}

		@Override
		public String getName()
		{
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
