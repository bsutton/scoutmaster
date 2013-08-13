package au.org.scoutmaster.views.wizards.messaging;

import org.vaadin.teemu.wizards.WizardStep;

import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.util.FormHelper;
import au.org.scoutmaster.views.MessagingWizardView;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
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
	
	FormHelper formHelper;
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
		
		layout.addComponent(new Label("Enter the subject and message and then click next."));
		FormLayout formLayout = new FormLayout();
		formHelper = new FormHelper(formLayout, null);
		providers = formHelper.bindEntityField("Provider", "providerName", "providerName", SMSProvider.class);
		from = formHelper.bindTextField("From Mobile No.", "from");
		from.setDescription("Enter you mobile phone no. so that all messages appear to come from you and recipients can send a text directly back to your phone.");
		subject = formHelper.bindTextField("Subject", "subject");
		subject.setSizeFull();
		message = formHelper.bindTextAreaField("Message", "message", 4);
		remaining = formHelper.bindLabelField("Characters remaining 160", "remaining");
		remaining.setImmediate(true);
		
		formLayout.addComponent(message);
		layout.addComponent(formLayout);
		layout.setMargin(true);
		
		message.addTextChangeListener(new TextChangeListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void textChange(TextChangeEvent event)
			{
				remaining.setCaption("Characters remaining " + (160 - event.getText().length()));
				
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
		return new Message(subject.getValue(), message.getValue(), from.getValue());
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
