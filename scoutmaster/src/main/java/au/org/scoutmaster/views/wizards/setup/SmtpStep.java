package au.org.scoutmaster.views.wizards.setup;

import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.editors.InputDialog;
import au.com.vaadinutils.listener.ClickAdaptorLogged;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.EMailServerSettingsDao;
import au.org.scoutmaster.domain.EMailServerSettings;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;
import au.org.scoutmaster.util.SMNotification;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.validator.CompositeValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public class SmtpStep extends SingleEntityStep<EMailServerSettings> implements WizardStep, ValueChangeListener,
		ClickListener
{
	private static Logger logger = Logger.getLogger(SmtpStep.class);
	private static final long serialVersionUID = 1L;

	private TextField smtpFQDN;

	private TextField smtpPort;

	private CheckBox authRequired;

	private TextField username;

	private PasswordField password;

	private TextField fromEmailAddress;

	private CheckBox useSSL;

	private TextField bounceEmailAddress;

	public SmtpStep(SetupWizardView setupWizardView)
	{
		super(setupWizardView, new DaoFactory().getEMailServerSettingsDao(), EMailServerSettings.class);
	}

	@Override
	public String getCaption()
	{

		return "SMTP Settings";
	}

	@Override
	public Component buildEditor(ValidatingFieldGroup<EMailServerSettings> fieldGroup)
	{
		SMMultiColumnFormLayout<EMailServerSettings> formLayout = new SMMultiColumnFormLayout<>(1, fieldGroup, 60);
		formLayout.setWidth("500px");

		Label label = new Label("<h1>Configure SMTP mail settings.</h1>");
		label.setContentMode(ContentMode.HTML);
		formLayout.bindLabel(label);

		// Create the user input field
		smtpFQDN = formLayout.bindTextField("SMTP FQDN:", "smtpFQDN");
		smtpFQDN.setDescription("SMTP Server FQDN or IP address");

		smtpPort = formLayout.bindTextField("SMTP Port:", "smtpPort");
		smtpPort.setDescription("SMTP Port No.");
		smtpPort.addValidator(new IntegerRangeValidator("The port no. must be an integer in the range 1 to 65535", 1,
				65535));

		authRequired = formLayout.bindBooleanField("SMTP Authentication Requried", "authRequired");
		authRequired.addValueChangeListener(this);

		username = formLayout.bindTextField("SMTP Username:", "username");
		username.setDescription("SMTP username if authentication is used");

		// Create the password input field
		password = formLayout.bindPasswordField("Password:", "password");
		password.setDescription("SMS Provider Password");
		
		useSSL = formLayout.bindBooleanField("Use SSL", "useSSL");
		useSSL.setDescription("Enables an SSL connection to your SMTP server if it supports it.");


		fromEmailAddress = formLayout.bindTextField("From Email Address:", "fromEmailAddress");
		fromEmailAddress.setDescription("Default From Address to use when sending bulk emails.");
		fromEmailAddress.addValidator(new EmailValidator("Enter a valid email address."));

		bounceEmailAddress = formLayout.bindTextField("Bounce Email Address:", "bounceEmailAddress");
		bounceEmailAddress.setDescription("Email Address that bounced emails should be sent to.");
		bounceEmailAddress.addValidator(new EmailValidator("Enter a valid email address."));

		Button test = new Button("Test");
		formLayout.addComponent(test);
		test.addClickListener(new ClickAdaptorLogged(this));

		// focus the fqnd field when user arrives to the login view
		smtpFQDN.focus();

		return formLayout;
	}

	@Override
	public void valueChange(ValueChangeEvent event)
	{
		if (event.getProperty() == this.authRequired)
		{
			Boolean authRequired = (Boolean) event.getProperty().getValue();

			username.setVisible(authRequired);
			password.setVisible(authRequired);
		}
	}

	@Override
	protected void initEntity(EMailServerSettings entity)
	{
		entity.setAuthRequired(false);
		username.setVisible(false);
		password.setVisible(false);
	}

	@Override
	protected EMailServerSettings findEntity()
	{
		EMailServerSettings setting = null;
		List<EMailServerSettings> settings = new DaoFactory().getEMailServerSettingsDao().findAll();
		if (settings.size() > 1)
			throw new IllegalStateException(
					"More than one EmailServerSetting has been found which is not valid during initial setup.");
		if (settings.size() == 1)
			setting = settings.get(0);

		if (setting != null)
		{
			username.setVisible(setting.isAuthRequired());
			password.setVisible(setting.isAuthRequired());
		}

		return setting;
	}

	@Override
	public void buttonClick(ClickEvent event)
	{
		// test that the SMS Provider configuration works.

		// First check that the entered details are valid.
		if (super.validate())
		{
			final EMailServerSettings settings = super.getEntity();
			if (settings == null)
				Notification.show(
						"Can't find the SMTP Settings, this usually means the install did not complete correctly.",
						Type.ERROR_MESSAGE);
			else
			{
				CompositeValidator validator = new CompositeValidator();
				validator.addValidator(new EmailValidator("Please input your email address"));
				validator.addValidator(new StringLengthValidator("Please enter an email address.", 6, 255, false));
				new InputDialog(UI.getCurrent(), "Test SMTP Settings.",
						"Enter your email address to recieve a test Email", new InputDialog.Recipient()
						{
							public void onOK(String input)
							{
								String toEmailAddress = input;

								EMailServerSettingsDao daoSMTPSettings = new DaoFactory().getEMailServerSettingsDao();

								try
								{
									StringBuilder sb = new StringBuilder();
									sb.append("If you receive this email then your Scoutmaster email settings are all correct.\n\n");
									sb.append("So welcome to Scoutmaster.\n\n");
									sb.append("May you live long and recruit many.\n");

									daoSMTPSettings.sendEmail(settings, settings.getFromEmailAddress(), toEmailAddress,
											"Test email from Scoutmaster setup", sb.toString());

									SMNotification.show("An email has been sent to: " + toEmailAddress
											+ " please check that the email arrived.", Type.HUMANIZED_MESSAGE);

								}
								catch (EmailException e)
								{
									logger.error(e,e);
									SMNotification.show(e, Type.ERROR_MESSAGE);
								}
							}

							@Override
							public void onCancel()
							{
								// TODO Auto-generated method stub

							}
						}).addValidator(validator);

			}
		}

	}
}
