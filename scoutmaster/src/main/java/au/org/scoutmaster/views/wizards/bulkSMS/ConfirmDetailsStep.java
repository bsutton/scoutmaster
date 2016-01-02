package au.org.scoutmaster.views.wizards.bulkSMS;

import java.util.ArrayList;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.crud.CrudEntity;
import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.util.SMNotification;
import au.org.scoutmaster.util.VelocityFormatException;

public class ConfirmDetailsStep implements WizardStep
{

	private final TextField subject;
	private final TextArea message;
	private final TextField from;
	private final TextField provider;
	private final BulkSMSWizardView messagingWizardView;
	private final VerticalLayout layout;
	private final Label recipientCount;
	private final MessageDetailsStep details;

	public ConfirmDetailsStep(final BulkSMSWizardView messagingWizardView)
	{
		this.messagingWizardView = messagingWizardView;
		this.details = messagingWizardView.getDetails();

		this.layout = new VerticalLayout();
		this.layout.addComponent(
				new Label("Please review the details before clicking next as messages will be sent immediately."));
		this.layout.setWidth("100%");

		this.recipientCount = new Label();
		this.recipientCount.setContentMode(ContentMode.HTML);
		this.layout.addComponent(this.recipientCount);

		final MultiColumnFormLayout<CrudEntity> formLayout = new MultiColumnFormLayout<>(1, null);
		formLayout.setColumnFieldWidth(0, 500);
		this.provider = formLayout.bindTextField("Provider", "provider");
		this.provider.setReadOnly(true);

		this.from = formLayout.bindTextField("From", "from");
		this.from.setReadOnly(true);

		this.subject = formLayout.bindTextField("Subject", "subject");
		this.subject.setSizeFull();
		this.subject.setReadOnly(true);

		this.message = formLayout.bindTextAreaField("Message", "message", 4);
		this.message.setReadOnly(true);
		this.message.setWidth("100%");

		this.layout.addComponent(formLayout);
		this.layout.setMargin(true);
	}

	@Override
	public String getCaption()
	{
		return "Confim Details";
	}

	@Override
	public Component getContent()
	{

		this.recipientCount.setValue("<p><b>" + this.messagingWizardView.getRecipientStep().getRecipientCount()
				+ " recipients have been selected to recieve the following SMS.</b></p>");

		final ArrayList<Contact> recipients = this.messagingWizardView.getRecipientStep().getRecipients();
		final Contact sampleContact = recipients.get(0);
		final User user = SMSession.INSTANCE.getLoggedInUser();

		try
		{
			this.provider.setReadOnly(false);
			this.provider.setValue(this.details.getProvider().getProviderName());
			this.provider.setReadOnly(true);
			this.from.setReadOnly(false);
			this.from.setValue(this.details.getFrom());
			this.from.setReadOnly(true);
			this.subject.setReadOnly(false);
			this.subject.setValue(this.details.getSubject());
			this.subject.setReadOnly(true);
			this.message.setReadOnly(false);
			this.message.setValue(this.details.getMessage().expandBody(user, sampleContact).toString());
			this.message.setReadOnly(true);
		}
		catch (final VelocityFormatException e)
		{
			SMNotification.show(e, Type.ERROR_MESSAGE);
		}

		return this.layout;
	}

	@Override
	public boolean onAdvance()
	{
		final boolean advance = this.subject.getValue() != null && this.message.getValue() != null;

		if (!advance)
		{
			Notification.show("Please enter a Subject and a Message then click Next");
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
		return new Message(this.subject.getValue(), this.message.getValue(), new Phone(this.from.getValue()));
	}

}
