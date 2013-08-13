package au.org.scoutmaster.views.wizards.messaging;

import org.vaadin.teemu.wizards.WizardStep;

import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.filter.EntityManagerProvider;
import au.org.scoutmaster.util.FormHelper;
import au.org.scoutmaster.views.MessagingWizardView;
import au.org.scoutmaster.views.SearchableContactTable;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class SelectRecipientsStep implements WizardStep
{

	private TextField subject;
	private TextArea message;
	private Label remaining;

	FormHelper formHelper;
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

		JPAContainer<Contact> contactContainer = JPAContainerFactory.make(Contact.class,
				EntityManagerProvider.INSTANCE.getEntityManager());
		contactTable = new SearchableContactTable(contactContainer, new String[]
		{ Contact.FIRSTNAME, Contact.LASTNAME, Contact.BIRTH_DATE, Contact.SECTION });
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
		return new Message(subject.getValue(), message.getValue(), from.getValue());
	}

	public SMSProvider getProvider()
	{
		return (SMSProvider) providers.getConvertedValue();
	}

}
