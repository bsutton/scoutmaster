package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.com.vaadinutils.crud.SearchableSelectableEntityTable;
import au.org.scoutmaster.dao.ContactDao;
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
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class BulkSelectionStep implements WizardStep, SelectStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(BulkSelectionStep.class);

	private RaffleBookAllocationWizardView setupWizardView;

	private ComboBox issuedBy;

	private TextField noOfBooksField;

	private List<Allocation> allocations = new ArrayList<>();

	private SearchableSelectableEntityTable<Contact> selectableTable;

	public BulkSelectionStep(RaffleBookAllocationWizardView setupWizardView)
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
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		Label label = new Label("<h1>Select the Contacts and no. of books to Allocate to each Contact.</h1>",
				ContentMode.HTML);
		label.setContentMode(ContentMode.HTML);

		layout.addComponent(label);

		MultiColumnFormLayout<RaffleBook> overviewForm = new MultiColumnFormLayout<RaffleBook>(1, null);
		overviewForm.setColumnFieldWidth(0, 200);
		FormHelper<RaffleBook> formHelper = overviewForm.getFormHelper();

		issuedBy = formHelper.new EntityFieldBuilder<Contact>().setLabel("Issued By")
				.setField(new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.issuedBy).getName())
				.setListClass(Contact.class).setListFieldName(Contact_.fullname).build();
		issuedBy.setFilteringMode(FilteringMode.CONTAINS);
		issuedBy.setTextInputAllowed(true);

		noOfBooksField = overviewForm.addTextField("No. of Books");
		noOfBooksField.setDescription("The no. of books to allocate to each Contact");

		Builder<Contact> builder = new HeadingPropertySet.Builder<Contact>();
		builder.addColumn("Firstname", Contact_.firstname).addColumn("Lastname", Contact_.lastname)
				.addColumn("Section", Contact_.section).addColumn("Phone", Contact.PRIMARY_PHONE)
				.addColumn("Member", Contact_.isMember).addColumn("Group Role", Contact_.groupRole);

		selectableTable = new SearchableSelectableEntityTable<Contact>(new DaoFactory().getContactDao()
				.createVaadinContainer(), builder.build())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Filter getContainerFilter(String filterString, boolean advancedSearchActive)
			{
				if (filterString.length() > 0)
				{
					String likeMatch = filterString + "%";
					return new Or(new Like(Contact_.firstname.getName(), likeMatch, false), new Like(
							Contact_.lastname.getName(), likeMatch, false), new Like(Contact.PRIMARY_PHONE, likeMatch,
							false), new Like(Contact_.isMember.getName(), likeMatch, false), new Like(
							Contact_.groupRole.getName(), likeMatch, false));
				}
				else
					return null;

			}
		};

		selectableTable.setSizeFull();
		layout.addComponent(selectableTable);

		layout.addComponent(overviewForm);

		Label labelAllocate = new Label("<h1>Clicking Next will Allocate the books!</h1>", ContentMode.HTML);

		layout.addComponent(labelAllocate);

		issuedBy.focus();

		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		boolean enough = false;
		if (this.issuedBy.getValue() != null && this.getBookCount() > 0)
		{
			// check that we have enough books available.
			RaffleBookDao daoRaffleBook = new DaoFactory().getRaffleBookDao();

			Raffle raffle = setupWizardView.getRaffle();

			List<RaffleBook> books = daoRaffleBook.findAllUnallocated(raffle);

			int requiredPerContactBooks = Long.valueOf(noOfBooksField.getValue()).intValue();

			ArrayList<Long> selectedIds = selectableTable.getSelectedIds();

			int requiredBooks = selectedIds.size() * requiredPerContactBooks;

			enough = books.size() >= requiredBooks;

			if (!enough)
				SMNotification.show("There are only " + books.size() + " books available to allocate.",
						Type.WARNING_MESSAGE);
			else
				preAllocateBooks(raffle, selectedIds, books, requiredPerContactBooks);
		}
		else
			SMNotification.show("You must select the Issued By and No. of Books first", Type.WARNING_MESSAGE);

		return enough;
	}

	private void preAllocateBooks(Raffle raffle, ArrayList<Long> selectedIds, List<RaffleBook> books, int requiredBooks)
	{
		int bookIndex = 0;
		for (Long selectedId : selectedIds)
		{
			ContactDao daoContact = new DaoFactory().getContactDao();

			Contact allocatedTo = daoContact.findById(selectedId);

			List<RaffleBook> bookAllocation = new ArrayList<>();

			// allocate the required no. of books to the contact.
			for (int i = 0; i < requiredBooks; i++)
			{
				bookAllocation.add(books.get(bookIndex++));
			}
			Allocation allocation = new Allocation(allocatedTo, bookAllocation);
			this.allocations.add(allocation);
		}

	}

	public boolean onBack()
	{
		return true;
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
