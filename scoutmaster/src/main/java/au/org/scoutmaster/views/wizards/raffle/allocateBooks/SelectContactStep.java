package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.org.scoutmaster.dao.Path;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
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
import com.vaadin.ui.VerticalLayout;

public class SelectContactStep  implements WizardStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(SelectContactStep.class);

	@SuppressWarnings("unused")
	private RaffleBookAllocationWizardView setupWizardView;

	private ComboBox allocatedToContact;

	private TextField noOfBooksField;

	private ComboBox issuedBy;

	public SelectContactStep(RaffleBookAllocationWizardView setupWizardView)
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

		allocatedToContact = formHelper.new EntityFieldBuilder<Contact>()
				.setLabel("Allocate To")
				.setField(new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.allocatedTo).getName())
				.setListFieldName(Contact_.fullname).build();
		allocatedToContact.setFilteringMode(FilteringMode.CONTAINS);
		allocatedToContact.setTextInputAllowed(true);
		allocatedToContact.setDescription("The Contact to issue tickets to.");
		
		noOfBooksField = overviewForm.addTextField("No. of Books");
		noOfBooksField.setDescription("The no. of books to allocate to this Contact");
		
		issuedBy= formHelper.new EntityFieldBuilder<Contact>()
				.setLabel("Issued By")
				.setField(new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.issuedBy).getName())
				.setListFieldName(Contact_.fullname).build();
		issuedBy.setFilteringMode(FilteringMode.CONTAINS);
		issuedBy.setTextInputAllowed(true);
		allocatedToContact.setDescription("The Contact that issue tickets.");

		
		layout.addComponent(overviewForm);
		
		Label labelAllocate = new Label("<h1>Clicking Next will Allocate the books!</h1>", ContentMode.HTML);

		layout.addComponent(labelAllocate);


		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		return true;
	}

	@Override
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

}
