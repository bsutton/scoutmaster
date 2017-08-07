package au.org.scoutmaster.views.wizards.bulkEmail;

import java.util.ArrayList;

import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.fields.CKEditorEmailField;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.security.User;
import au.org.scoutmaster.util.SMNotification;
import au.org.scoutmaster.util.VelocityFormatException;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class StepConfirmDetails implements WizardStep
{

	private final TextField subject;
	private final CKEditorEmailField ckEditorTextField;
	private final TextField from;
	private final BulkEmailWizardView messagingWizardView;
	private final VerticalLayout layout;
	private final Label recipientCount;
	private final StepEnterDetails details;

	public StepConfirmDetails(final BulkEmailWizardView messagingWizardView)
	{
		this.messagingWizardView = messagingWizardView;
		this.details = messagingWizardView.getDetails();

		this.layout = new VerticalLayout();
		this.layout.addComponent(new Label(
				"Please review the details before clicking next as messages will be sent immediately."));
		this.layout.setSizeFull();

		this.recipientCount = new Label();
		this.recipientCount.setContentMode(ContentMode.HTML);
		this.layout.addComponent(this.recipientCount);

		this.from = new TextField("From");
		this.layout.addComponent(this.from);
		this.from.setReadOnly(true);
		this.from.setWidth("100%");

		this.subject = new TextField("Subject");
		this.layout.addComponent(this.subject);
		this.subject.setWidth("100%");
		this.subject.setReadOnly(true);

		this.ckEditorTextField = new CKEditorEmailField(false);
		this.layout.addComponent(this.ckEditorTextField);
		this.ckEditorTextField.setReadOnly(true);
		this.ckEditorTextField.setSizeFull();

		this.layout.setExpandRatio(this.ckEditorTextField, 1.0f);
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
				+ " recipients have been selected to recieve the following Email.</b></p>");

		final ArrayList<Contact> recipients = this.messagingWizardView.getRecipientStep().getRecipients();
		final User user = SMSession.INSTANCE.getLoggedInUser();

		try
		{
			final Contact sampleContact = recipients.get(0);
			this.from.setReadOnly(false);
			this.from.setValue(this.details.getFrom());
			this.from.setReadOnly(true);
			this.subject.setReadOnly(false);
			this.subject.setValue(this.details.getMessage().expandSubject(user, sampleContact).toString());
			this.subject.setReadOnly(true);
			this.ckEditorTextField.setReadOnly(false);
			this.ckEditorTextField.setValue(this.details.getMessage().expandBody(user, sampleContact).toString());
			this.ckEditorTextField.setReadOnly(true);
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
		final boolean advance = this.subject.getValue() != null && this.ckEditorTextField.getValue() != null;

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
		return new Message(this.subject.getValue(), this.ckEditorTextField.getValue(), new Phone(this.from.getValue()));
	}

}
