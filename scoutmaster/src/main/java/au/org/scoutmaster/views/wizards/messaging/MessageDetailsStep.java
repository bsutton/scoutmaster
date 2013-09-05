package au.org.scoutmaster.views.wizards.messaging;

import java.util.List;

import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMSProviderDao;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.domain.SMSProvider_;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class MessageDetailsStep  implements WizardStep
{

	private TextField subject;
	private TextArea message;
	private Label remaining;
	
	//SMFormHelper<SMSProvider> formHelper;
	private TextField from;
	private ComboBox providers;

	public MessageDetailsStep(MessagingWizardView messagingWizardView)
	{
		
	}

	@Override
	public String getCaption()
	{
		return "Message Details";
	}

	@Override
	public Component getContent()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setDescription("MessageDetailsContent");
		
		layout.addComponent(new Label("Enter the subject and message and then click next."));
		MultiColumnFormLayout formLayout = new MultiColumnFormLayout<>(1, null, 60);
		formLayout.setSizeFull();
		//formHelper = new SMFormHelper<SMSProvider>(formLayout, null);
		providers = formLayout.bindEntityField("Provider", SMSProvider_.providerName, SMSProvider.class, SMSProvider_.providerName);
		SMSProviderDao daoSMSProvider = new DaoFactory().getSMSProviderDao();
		List<SMSProvider> list = daoSMSProvider.findAll();
		if (list.size() == 0)
			throw new IllegalStateException("You must first configure an SMS Provider");
		providers.select(list.get(0).getId());
		from = formLayout.bindTextField("From Mobile No.", "from");
		from.setDescription("Enter you mobile phone no. so that all messages appear to come from you and recipients can send a text directly back to your phone.");
		subject = formLayout.bindTextField("Subject", "subject");
		subject.setSizeFull();
		message = formLayout.bindTextAreaField("Message", "message", 4);
		remaining = formLayout.bindLabel("Characters remaining 160");
		//remaining.setCaption("Message");
		remaining.setImmediate(true);
		
		layout.addComponent(formLayout);
		layout.setMargin(true);
		//layout.setSizeFull();
		
		message.addTextChangeListener(new TextChangeListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void textChange(TextChangeEvent event)
			{
				remaining.setValue("Characters remaining " + (160 - event.getText().length()));
				
			}
		});
		
		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		boolean advance = this.subject.getValue() != null && this.message.getValue() != null;
		
		if (!advance)
			Notification.show("Please enter a Subject and a Message then click Next");
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

	public String getFrom()
	{
		return from.getValue();
	}

	public String getSubject()
	{
		return subject.getValue();
	}

}

