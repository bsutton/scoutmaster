package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.org.scoutmaster.dao.Path;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.RaffleAllocation_;
import au.org.scoutmaster.domain.RaffleBook;
import au.org.scoutmaster.domain.RaffleBook_;

import com.vaadin.addon.jpacontainer.EntityItem;
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
 * @author bsutton
 *
 */
public class ManualSelectionStep  implements WizardStep, SelectStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(ManualSelectionStep.class);

	private RaffleBookAllocationWizardView setupWizardView;

	private ComboBox allocatedToContact;

	private TextField noOfBooksField;

	private ComboBox issuedBy;

	private List<Allocation> allocations = new ArrayList<>();

	private TwinColSelect twinCol;

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

		allocatedToContact = formHelper.new EntityFieldBuilder<Contact>()
				.setLabel("Allocate To")
				.setField(new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.allocatedTo).getName())
				.setListClass(Contact.class)
				.setListFieldName(Contact_.fullname).build();
		allocatedToContact.setFilteringMode(FilteringMode.CONTAINS);
		allocatedToContact.setTextInputAllowed(true);
		allocatedToContact.setDescription("The Contact to issue tickets to.");

		twinCol = new TwinColSelect();

		List<RaffleBook> availableBooks = new ArrayList<>();
		for (RaffleBook book : availableBooks)
		{
			twinCol.addItem(book);
		}

		layout.addComponent(overviewForm);
		
		Label labelAllocate = new Label("<h1>Clicking Next will Allocate the books!</h1>", ContentMode.HTML);

		layout.addComponent(labelAllocate);
		
		allocatedToContact.focus();

		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		Raffle raffle = setupWizardView.getRaffle();
		
		@SuppressWarnings("unchecked")
		Collection<RaffleBook> selectedIds = (Collection<RaffleBook>) twinCol.getVisibleItemIds();
		preAllocateBooks(raffle, getAllocatedContact(), selectedIds);
		
		
		return true;
	}


	private void preAllocateBooks(Raffle raffle, Contact allocatedTo, Collection<RaffleBook> books)
	{
		List<RaffleBook> bookAllocation = new ArrayList<>();
		
		for (RaffleBook book: books)
		{
			bookAllocation.add(book);
		}
		Allocation allocation = new Allocation(allocatedTo, bookAllocation);
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
