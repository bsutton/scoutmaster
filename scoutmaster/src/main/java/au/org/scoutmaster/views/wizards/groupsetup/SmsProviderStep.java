package au.org.scoutmaster.views.wizards.groupsetup;

import org.marre.sms.SmsException;
import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

import au.com.vaadinutils.editors.InputDialog;
import au.com.vaadinutils.editors.Recipient;
import au.com.vaadinutils.listener.ClickEventLogged;
import au.com.vaadinutils.listener.ProgressListener;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMSProviderDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.ui.SimpleFormLayout;
import au.org.scoutmaster.util.SMNotification;
import au.org.scoutmaster.views.wizards.bulkSMS.Message;
import au.org.scoutmaster.views.wizards.bulkSMS.SMSTransmission;

public class SmsProviderStep implements WizardStep, ClickListener, ProgressListener<SMSTransmission>
{
	private static final long serialVersionUID = 1L;
	private TextField senderId;
	private SimpleFormLayout form;
	private TextField user;
	private PasswordField password;
	private TextField apiId;

	public SmsProviderStep(final GroupSetupWizardView setupWizardView)
	{
	}

	@Override
	public String getCaption()
	{
		return "SMS Provider";
	}

	@Override
	public Component getContent()
	{
		form = new SimpleFormLayout();
		form.setMargin(true);

		final Label label = new Label("<h1>Configure SMS (Text message) provider settings</h1>");
		label.setContentMode(ContentMode.HTML);
		form.addComponent(label);

		Label optionalInfo = new Label(
				"Scoutmaster allows you to optionally send Text Messages (SMS), which is great for sending out reminders etc.<br>"
						+ "To send Text messages, you need to have an account with Click A Tell, who are an SMS provider.<br>"
						+ "Click A Tell charge around 6c every time you send a Text message. <br><br>"
						+ "<b>You don't need to send Text message when using Scoutmaster</b>, but it is a 'nice to have' feature.<br>"
						+ "When managing our local group, I often find that members will read a text message when they don't necessarily read emails.</br></br>"
						+ "<b>Disclaimer</b>:</br>"
						+ "The Scoutmaster team is in no way associated with Click A Tell and we don't make any money when you use Click A Tell.</br>"
						+ "</br>"
						+ "If you want to use Click A Tell, you need to go and create a Click A Tell account at: "
						+ "<a target=\"_blank\" href=\"https://www.clickatell.com/signup\">https://www.clickatell.com/signup</a><br>"
						+ "</br>"
						+ "If you aren't certain that you want to send Text messages or just want to get on and try out Scoutmaster, just click 'Next'"
						+ " and you can come back and enable SMS messaging at a later stage.");

		optionalInfo.setContentMode(ContentMode.HTML);
		form.addComponent(optionalInfo);

		user = new TextField("Username:");
		form.addComponent(user);
		user.setDescription("SMS Provider Username");

		password = new PasswordField("Password:");
		form.addComponent(password);
		password.setDescription("SMS Provider Password");

		apiId = new TextField("Api Id:");
		form.addComponent(apiId);
		apiId.setDescription("SMS Provider API key");
		// apiId.addValidator(new
		// StringLengthValidator("Please enter an API Key.", 1, 32, false));

		this.senderId = new TextField("Sender ID");
		form.addComponent(senderId);
		apiId.setDescription("Mobile phone to send message from. Must include country code.");

		final Button test = new Button("Test");
		form.addComponent(test);
		test.addClickListener(new ClickEventLogged.ClickAdaptor(this));

		// focus the username field when user arrives to the login view
		user.focus();

		return form;
	}

	@Override
	public void buttonClick(final ClickEvent event)
	{
		// test that the SMS Provider configuration works.

		// First check that the entered details are valid.
		if (form.validate())
		{
			SMSProvider provider = new SMSProvider();
			provider.setProviderName("ClickATell"); // the only one we provide
													// at the moment.
			provider.setDescription("ClickATell SMS Provider");
			provider.setDefaultSenderID(senderId.getValue());
			provider.setUsername(user.getValue());
			provider.setPassword(password.getValue());
			provider.setApiId(apiId.getValue());
			provider.setDefaultProvider(true);
			provider.setActive(true);
			new InputDialog(UI.getCurrent(), "Test SMS Provider Settings.",
					"Enter your Mobile No. to recieve a test SMS.", new Recipient()
					{
						@Override
						public boolean onOK(final String input)
						{
							final Phone recipient = new Phone(input);
							final Message message = new Message("Test SMS Subject",
									"Test SMS Message from Scoutmaster setup wizard.",
									new Phone(SmsProviderStep.this.senderId.getValue()));

							final Contact contact = new Contact();
							contact.setFirstname("Test");
							contact.setLastname("SMS");

							final SMSTransmission transmission = new SMSTransmission(null, contact, message, recipient);
							final SMSProviderDao daoSMSProvider = new DaoFactory().getSMSProviderDao();

							try
							{
								daoSMSProvider.sendNoLogging(provider, transmission, SmsProviderStep.this);
							}
							catch (final SmsException e)
							{
								Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
							}
							catch (final Throwable e)
							{
								Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
							}
							return true;
						}

						@Override
						public boolean onCancel()
						{
							Notification.show("Test Cancelled", Type.TRAY_NOTIFICATION);
							return true;

						}
					}).addValidator(new StringLengthValidator("Please input your mobile no.", 8, 12, false));

		}

	}

	@Override
	public void progress(final int count, final int max, final SMSTransmission status)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void complete(final int sent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void itemError(final Exception e, final SMSTransmission status)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void exception(final Exception e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onAdvance()
	{
		boolean valid = form.validate();
		if (!valid)
			SMNotification.show("Please fix the form errors", Type.ERROR_MESSAGE);
		return valid;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

}
