package au.org.scoutmaster.views.wizards.messaging;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.HeadingToPropertyId;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Contact;
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

	public SelectRecipientsStep(MessagingWizardView messagingWizardView)
	{

	}

	@Override
	public String getCaption()
	{
		return "Select Recipients";
	}

	@Override
	public Component getContent()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSizeFull();

		ContactDao daoContact = new DaoFactory().getContactDao();
		
		JPAContainer<Contact> contactContainer = daoContact.makeJPAContainer();
		List<HeadingToPropertyId> headings = new ArrayList<>();
		
		headings.add(new HeadingToPropertyId("First Name", Contact.FIRSTNAME));
		headings.add(new HeadingToPropertyId("Lastname", Contact.LASTNAME));
		headings.add(new HeadingToPropertyId("Birth Date", Contact.BIRTH_DATE));
		headings.add(new HeadingToPropertyId("Section", Contact.SECTION));
		headings.add(new HeadingToPropertyId("Mobile", Contact.MOBILE_PHONE));

		contactTable = new SearchableContactTable(contactContainer, headings);
		layout.addComponent(contactTable);
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

}
