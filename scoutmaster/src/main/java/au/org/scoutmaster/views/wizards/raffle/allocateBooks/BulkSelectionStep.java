package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.com.vaadinutils.crud.SearchableSelectableEntityTable;
import au.com.vaadinutils.dao.Path;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.RaffleBookDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.RaffleAllocation_;
import au.org.scoutmaster.domain.RaffleBook;
import au.org.scoutmaster.domain.RaffleBook_;
import au.org.scoutmaster.util.SMNotification;

public class BulkSelectionStep implements WizardStep, SelectStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(BulkSelectionStep.class);

	private final RaffleBookAllocationWizardView setupWizardView;

	private ComboBox issuedBy;

	private TextField noOfBooksField;

	private final List<Allocation> allocations = new ArrayList<>();

	private SearchableSelectableEntityTable<Contact> selectableTable;

	public BulkSelectionStep(final RaffleBookAllocationWizardView setupWizardView)
	{
		this.setupWizardView = setupWizardView;
	}

	@Override
	public String getCaption()
	{
		return "Select Contacts";
	}

	@Override
	public Component getContent()
	{
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		final Label label = new Label("<h1>Select the Contacts and no. of books to Allocate to each Contact.</h1>",
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
		this.noOfBooksField.setDescription("The no. of books to allocate to each Contact");

		final Builder<Contact> builder = new HeadingPropertySet.Builder<Contact>();
		builder.addColumn("Firstname", Contact_.firstname).addColumn("Lastname", Contact_.lastname)
				.addColumn("Section", Contact_.section).addColumn("Phone", Contact.PRIMARY_PHONE)
				.addColumn("Member", Contact_.isMember).addColumn("Group Role", Contact_.groupRole);

		this.selectableTable = new SearchableSelectableEntityTable<Contact>("Contact")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Filter getContainerFilter(final String filterString, final boolean advancedSearchActive)
			{

				if (filterString.length() > 0)
				{
					return new Or(new SimpleStringFilter(Contact_.firstname.getName(), filterString, true, false),
							new SimpleStringFilter(Contact_.lastname.getName(), filterString, true, false),
							new SimpleStringFilter(Contact.PRIMARY_PHONE, filterString, true, false),
							new SimpleStringFilter(Contact_.isMember.getName(), filterString, true, false),
							new SimpleStringFilter(Contact_.groupRole.getName(), filterString, true, false),
							new SimpleStringFilter(Contact_.section.getName(), filterString, true, false));
				}
				else
				{
					return null;
				}

			}

			@Override
			public String getTitle()
			{
				return "Contacts";
			}

			@Override
			public HeadingPropertySet<Contact> getHeadingPropertySet()
			{
				return builder.build();
			}

			@Override
			public Filterable getContainer()
			{
				return new DaoFactory().getContactDao().createVaadinContainer();
			}
		};

		this.selectableTable.setSizeFull();
		layout.addComponent(this.selectableTable);

		layout.addComponent(overviewForm);

		final Label labelAllocate = new Label("<h1>Clicking Next will Allocate the books!</h1>", ContentMode.HTML);

		layout.addComponent(labelAllocate);

		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		boolean enough = false;
		if (this.issuedBy.getValue() != null && getBookCount() > 0)
		{
			// check that we have enough books available.
			final RaffleBookDao daoRaffleBook = new DaoFactory().getRaffleBookDao();

			final Raffle raffle = this.setupWizardView.getRaffle();

			final List<RaffleBook> books = daoRaffleBook.findAllUnallocated(raffle);

			final int requiredPerContactBooks = Long.valueOf(this.noOfBooksField.getValue()).intValue();

			final Collection<Long> selectedIds = this.selectableTable.getSelectedIds();

			final int requiredBooks = selectedIds.size() * requiredPerContactBooks;

			enough = books.size() >= requiredBooks;

			if (!enough)
			{
				SMNotification.show("There are only " + books.size() + " books available to allocate.",
						Type.WARNING_MESSAGE);
			}
			else
			{
				preAllocateBooks(raffle, selectedIds, books, requiredPerContactBooks);
			}
		}
		else
		{
			SMNotification.show("You must select the Issued By and No. of Books first", Type.WARNING_MESSAGE);
		}

		return enough;
	}

	private void preAllocateBooks(final Raffle raffle, final Collection<Long> selectedIds,
			final List<RaffleBook> books, final int requiredBooks)
	{
		int bookIndex = 0;
		for (final Long selectedId : selectedIds)
		{
			final ContactDao daoContact = new DaoFactory().getContactDao();

			final Contact allocatedTo = daoContact.findById(selectedId);

			final List<RaffleBook> bookAllocation = new ArrayList<>();

			// allocate the required no. of books to the contact.
			for (int i = 0; i < requiredBooks; i++)
			{
				bookAllocation.add(books.get(bookIndex++));
			}
			final Allocation allocation = new Allocation(allocatedTo, bookAllocation);
			this.allocations.add(allocation);
		}

	}

	@Override
	public boolean onBack()
	{
		return true;
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
