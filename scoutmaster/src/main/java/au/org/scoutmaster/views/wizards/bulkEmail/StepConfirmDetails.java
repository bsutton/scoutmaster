package au.org.scoutmaster.views.wizards.bulkEmail;

import java.util.ArrayList;

import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.fields.CKEditorEmailField;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.access.User;
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

	private TextField subject;
	private CKEditorEmailField ckEditorTextField;
	private TextField from;
	private WizardView messagingWizardView;
	private VerticalLayout layout;
	private Label recipientCount;
	private StepEnterDetails details;

	public StepConfirmDetails(WizardView messagingWizardView)
	{
		this.messagingWizardView = messagingWizardView;
		details = messagingWizardView.getDetails();

		layout = new VerticalLayout();
		layout.addComponent(new Label(
				"Please review the details before clicking next as messages will be sent immediately."));
		layout.setSizeFull();

		recipientCount = new Label();
		recipientCount.setContentMode(ContentMode.HTML);
		layout.addComponent(recipientCount);

		from = new TextField("From");
		layout.addComponent(from);
		from.setReadOnly(true);
		from.setWidth("100%");

		subject = new TextField("Subject");
		layout.addComponent(subject);
		subject.setWidth("100%");
		subject.setReadOnly(true);

		ckEditorTextField = new CKEditorEmailField(false);
		layout.addComponent(ckEditorTextField);
		ckEditorTextField.setReadOnly(true);
		ckEditorTextField.setSizeFull();

		layout.setExpandRatio(ckEditorTextField, 1.0f);
		layout.setMargin(true);

	}

	@Override
	public String getCaption()
	{
		return "Confim Details";
	}

	@Override
	public Component getContent()
	{

		recipientCount.setValue("<p><b>" + messagingWizardView.getRecipientStep().getRecipientCount()
				+ " recipients have been selected to recieve the following Email.</b></p>");

		ArrayList<Contact> recipients = messagingWizardView.getRecipientStep().getRecipients();
		User user = (User) SMSession.INSTANCE.getLoggedInUser();

		try
		{
			Contact sampleContact = recipients.get(0);
			from.setReadOnly(false);
			from.setValue(details.getFrom());
			from.setReadOnly(true);
			subject.setReadOnly(false);
			subject.setValue(details.getMessage().expandSubject(user, sampleContact).toString());
			subject.setReadOnly(true);
			ckEditorTextField.setReadOnly(false);
			ckEditorTextField.setValue(details.getMessage().expandBody(user, sampleContact).toString());
			ckEditorTextField.setReadOnly(true);
		}
		catch (VelocityFormatException e)
		{
			SMNotification.show(e, Type.ERROR_MESSAGE);
		}

		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		boolean advance = this.subject.getValue() != null && this.ckEditorTextField.getValue() != null;

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
		return new Message(subject.getValue(), ckEditorTextField.getValue(), new Phone(from.getValue()));
	}

}