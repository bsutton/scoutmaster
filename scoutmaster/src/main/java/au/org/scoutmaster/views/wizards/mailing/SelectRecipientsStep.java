package au.org.scoutmaster.views.wizards.mailing;

import java.util.ArrayList;

import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.views.SearchableContactTable;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class SelectRecipientsStep implements WizardStep
{

	private TextField subject;
	private TextArea message;

	private TextField from;
	private ComboBox providers;
	private SearchableContactTable contactTable;
	private VerticalLayout layout;

	public SelectRecipientsStep(BulkEmailWizardView messagingWizardView)
	{
		layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSizeFull();

		ContactDao daoContact = new DaoFactory().getContactDao();
		
		JPAContainer<Contact> contactContainer = daoContact.createVaadinContainer();
		
		Builder<Contact> builder = new HeadingPropertySet.Builder<Contact>();
		builder.addColumn("First Name", Contact_.firstname)
		.addColumn("Lastname", Contact_.lastname)
		.addColumn("Birth Date", Contact_.birthDate)
		.addColumn("Section", Contact_.section)
		.addColumn("Email", Contact_.homeEmail);

		contactTable = new SearchableContactTable(contactContainer, builder.build());
		layout.addComponent(contactTable);

	}

	@Override
	public String getCaption()
	{
		return "Select Recipients";
	}

	@Override
	public Component getContent()
	{
		
		
		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		boolean advance = true;
		if (contactTable.size() == 0)
		{
			advance = false;
			Notification.show("You must select at least one recipient.");
		}
		return advance;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	public Message getMessage()
	{
		return new Message(subject.getValue(), message.getValue(), new Phone(from.getValue()));
	}

	public SMSProvider getProvider()
	{
		return (SMSProvider) providers.getConvertedValue();
	}
	
	public ArrayList<Contact> getRecipients()
	{
		return contactTable.getFilteredContacts();
	}

	public int getRecipientCount()
	{
		return contactTable.size();
	}
}
