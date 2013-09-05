package au.org.scoutmaster.views.wizards.setup;

import java.util.List;

import org.marre.sms.SmsException;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.editors.InputDialog;
import au.com.vaadinutils.listener.ClickAdaptorLogged;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMSProviderDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.util.ProgressListener;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;
import au.org.scoutmaster.views.wizards.messaging.Message;
import au.org.scoutmaster.views.wizards.messaging.SMSTransmission;

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

public class SmsProviderStep extends SingleEntityStep<SMSProvider> implements WizardStep, ClickListener, ProgressListener<SMSTransmission>
{
	private static final long serialVersionUID = 1L;

	public SmsProviderStep(SetupWizardView setupWizardView)
	{
		super(setupWizardView, new DaoFactory().getSMSProviderDao(), SMSProvider.class);
	}

	@Override
	public String getCaption()
	{
		return "SMS Provider";
	}

	@Override
	public Component buildEditor(ValidatingFieldGroup<SMSProvider> fieldGroup)
	{
		SMMultiColumnFormLayout<SMSProvider> formLayout = new SMMultiColumnFormLayout<>(1, fieldGroup, 60);
		formLayout.setWidth("600px");

		Label label = new Label("<h1>Configure Click A Tell provider settings</h1>");
		label.setContentMode(ContentMode.HTML);
		formLayout.bindLabel(label);

		// Create the user input field
		TextField user = formLayout.bindTextField("Username:", "username");
		user.setDescription("SMS Provider Username");
		//user.addValidator(new StringLengthValidator("Please enter a username.", 1, 32, false));

		// Create the password input field
		PasswordField password = formLayout.bindPasswordField("Password:", "password");
		password.setDescription("SMS Provider Password");
		//password.addValidator(new StringLengthValidator("Please enter a password.", 1, 32, false));

		// Create the user input field
		TextField apiId = formLayout.bindTextField("Api Id:", "ApiId");
		apiId.setDescription("SMS Provider API key");
		//apiId.addValidator(new StringLengthValidator("Please enter an API Key.", 1, 32, false));

		Button test = new Button("Test");
		formLayout.addComponent(test);
		test.addClickListener(new ClickAdaptorLogged(this));

		// focus the username field when user arrives to the login view
		user.focus();

		return formLayout;
	}

	@Override
	protected void initEntity(SMSProvider entity)
	{
		entity.setProviderName("ClickATell");
		entity.setActive(true);
		entity.setDefaultProvider(true);
	}

	@Override
	protected SMSProvider findEntity()
	{
		SMSProvider provider = null;

		SMSProviderDao daoSMSProvider = new DaoFactory().getSMSProviderDao();
		List<SMSProvider> providers = daoSMSProvider.findByName("ClickATell");
		if (providers.size() > 1)
			throw new IllegalArgumentException("Found more passwordthan one ClickATell SMSProvider");

		if (providers.size() == 1)
			provider = providers.get(0);

		return provider;
	}

	@Override
	public void buttonClick(ClickEvent event)
	{
		// test that the SMS Provider configuration works.

		// First check that the entered details are valid.
		if (super.validate())
		{
			final SMSProvider provider = super.getEntity();
			if (provider == null)
				Notification.show(
						"Can't find the SMS Provider, this usually means the install did not complete correctly.",
						Type.ERROR_MESSAGE);
			else
			{

				new InputDialog(UI.getCurrent(), "Test SMS Provider Settings.", "Enter your Mobile No. to recieve a test SMS.",
						new InputDialog.Recipient()
						{
							public void onOK(String input)
							{
								Phone phone = new Phone(input);
								Message message = new Message("Test SMS Subject", "Test SMS Message from Scoutmaster setup wizard.", phone);

								Contact contact = new Contact();
								contact.setFirstname("Test");
								contact.setLastname("SMS");

								SMSTransmission transmission = new SMSTransmission(contact, message, phone);
								SMSProviderDao daoSMSProvider = new DaoFactory().getSMSProviderDao();

								try
								{
									daoSMSProvider.send(provider, transmission, SmsProviderStep.this);
								}
								catch (SmsException e)
								{
									Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
								}
								catch (Throwable e)
								{
									Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
								}
							}

							@Override
							public void onCancel()
							{
								Notification.show("Test Cancelled", Type.TRAY_NOTIFICATION);
								
							}
						}).addValidator(new StringLengthValidator("Please input your mobile no.", 8, 12, false));

			}
		}

	}

	@Override
	public void progress(int count, int max, SMSTransmission status)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void complete()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemError(Exception e, SMSTransmission status)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exception(Exception e)
	{
		// TODO Auto-generated method stub
		
	}

}
