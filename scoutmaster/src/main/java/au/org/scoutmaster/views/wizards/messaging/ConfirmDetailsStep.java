package au.org.scoutmaster.views.wizards.messaging;

import org.vaadin.teemu.wizards.WizardStep;

import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.util.FormHelper;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ConfirmDetailsStep  implements WizardStep
{

	private TextField subject;
	private TextArea message;
	private Label remaining;
	
	FormHelper formHelper;
	private TextField from;
	private TextField provider;
	private MessagingWizardView messagingWizardView;

	public ConfirmDetailsStep(MessagingWizardView messagingWizardView)
	{
		this.messagingWizardView = messagingWizardView;
		
	}

	@Override
	public String getCaption()
	{
		return "Confim Details";
	}

	@Override
	public Component getContent()
	{
		VerticalLayout layout = new VerticalLayout();
		
		MessageDetailsStep details = messagingWizardView.getDetails();
		
		layout.addComponent(new Label("Please review the details before clicking next as messages will be sent immediately."));
		FormLayout formLayout = new FormLayout();
		formHelper = new FormHelper(formLayout, null);
		provider = formHelper.bindTextField("Provider", "provider");
		provider.setValue(details.getProvider().getProviderName());
		provider.setReadOnly(true);
		
		from = formHelper.bindTextField("From", "from");
		from.setValue(details.getFrom());
		from.setReadOnly(true);
		
		subject = formHelper.bindTextField("Subject", "subject");
		subject.setValue(details.getSubject());
		subject.setSizeFull();
		subject.setReadOnly(true);
		
		message = formHelper.bindTextAreaField("Message", "message", 4);
		message.setValue(details.getMessage().getBody());
		message.setReadOnly(true);
		
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
		return new Message(subject.getValue(), message.getValue(), new Phone(from.getValue()));
	}

	
}

