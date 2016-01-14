package au.org.scoutmaster.views.wizards.groupmaintenance;

import java.util.List;

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
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.editors.InputDialog;
import au.com.vaadinutils.editors.Recipient;
import au.com.vaadinutils.listener.ClickEventLogged;
import au.com.vaadinutils.listener.ProgressListener;
import au.com.vaadinutils.ui.SingleEntityWizardStep;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMSProviderDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.domain.SMSProvider_;
import au.org.scoutmaster.views.wizards.bulkSMS.Message;
import au.org.scoutmaster.views.wizards.bulkSMS.SMSTransmission;

public class SmsProviderStep extends SingleEntityWizardStep<SMSProvider>
		implements WizardStep, ClickListener, ProgressListener<SMSTransmission>
{
	private static final long serialVersionUID = 1L;
	private TextField senderId;
	private VerticalLayout layout;
	private TextField user;
	private PasswordField password;
	private TextField apiId;

	public SmsProviderStep(final GroupMaintenanceWizardView setupWizardView)
	{
		super(new DaoFactory().getSMSProviderDao(), SMSProvider.class);
	}

	@Override
	public String getCaption()
	{
		return "SMS Provider";
	}

	@Override
	public Component getContent(final ValidatingFieldGroup<SMSProvider> fieldGroup)
	{
		if (layout == null)
		{
			layout = new VerticalLayout();
			layout.setMargin(true);
			final MultiColumnFormLayout<SMSProvider> formLayout = new MultiColumnFormLayout<>(1, fieldGroup);
			formLayout.setColumnFieldWidth(0, 250);

			final Label label = new Label("<h1>Configure Click A Tell provider settings</h1>");
			label.setContentMode(ContentMode.HTML);
			layout.addComponent(label);
			layout.addComponent(formLayout);

			user = formLayout.bindTextField("Username:", SMSProvider_.username);
			user.setDescription("SMS Provider Username");
			// user.addValidator(new
			// StringLengthValidator("Please enter a username.", 1, 32, false));

			password = formLayout.bindPasswordField("Password:", SMSProvider_.password);
			password.setDescription("SMS Provider Password");
			// password.addValidator(new
			// StringLengthValidator("Please enter a password.", 1, 32, false));

			apiId = formLayout.bindTextField("Api Id:", SMSProvider_.ApiId);
			apiId.setDescription("SMS Provider API key");
			// apiId.addValidator(new
			// StringLengthValidator("Please enter an API Key.", 1, 32, false));

			this.senderId = formLayout.bindTextField("Sender ID", SMSProvider_.defaultSenderID);
			apiId.setDescription("Mobile phone to send message from. Must include country code.");

			final Button test = new Button("Test");
			layout.addComponent(test);
			test.addClickListener(new ClickEventLogged.ClickAdaptor(this));

		}

		// focus the username field when user arrives to the login view
		user.focus();

		return layout;
	}

	@Override
	protected void initEntity(final SMSProvider entity)
	{
		entity.setProviderName("ClickATell");
		entity.setActive(true);
		entity.setDefaultProvider(true);
	}

	@Override
	protected SMSProvider findEntity()
	{
		SMSProvider provider = null;

		final SMSProviderDao daoSMSProvider = new DaoFactory().getSMSProviderDao();
		final List<SMSProvider> providers = daoSMSProvider.findByName("ClickATell");
		if (providers.size() > 1)
		{
			throw new IllegalArgumentException("Found more passwordthan one ClickATell SMSProvider");
		}

		if (providers.size() == 1)
		{
			provider = providers.get(0);
		}

		return provider;
	}

	@Override
	public void buttonClick(final ClickEvent event)
	{
		// test that the SMS Provider configuration works.

		// First check that the entered details are valid.
		if (super.validate())
		{
			final SMSProvider provider = super.getEntity();
			if (provider == null)
			{
				Notification.show(
						"Can't find the SMS Provider, this usually means the install did not complete correctly.",
						Type.ERROR_MESSAGE);
			}
			else
			{

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

								final SMSTransmission transmission = new SMSTransmission(null, contact, message,
										recipient);
								final SMSProviderDao daoSMSProvider = new DaoFactory().getSMSProviderDao();

								try
								{
									daoSMSProvider.send(provider, transmission, SmsProviderStep.this);
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

}
