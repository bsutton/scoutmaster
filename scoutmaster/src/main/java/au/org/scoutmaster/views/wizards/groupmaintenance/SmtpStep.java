package au.org.scoutmaster.views.wizards.groupmaintenance;

import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

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
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.editors.InputDialog;
import au.com.vaadinutils.editors.Recipient;
import au.com.vaadinutils.listener.ClickEventLogged;
import au.com.vaadinutils.ui.SingleEntityWizardStep;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMTPSettingsDao;
import au.org.scoutmaster.domain.SMTPServerSetting;
import au.org.scoutmaster.domain.SMTPServerSetting_;
import au.org.scoutmaster.forms.EmailAddressType;
import au.org.scoutmaster.util.SMNotification;

public class SmtpStep extends SingleEntityWizardStep<SMTPServerSetting>
		implements WizardStep, ValueChangeListener, ClickListener
{
	private static Logger logger = LogManager.getLogger(SmtpStep.class);
	private static final long serialVersionUID = 1L;

	private final TextField smtpFQDN;

	private final TextField smtpPort;

	private final CheckBox authRequired;

	private final TextField username;

	private final PasswordField password;

	private final TextField fromEmailAddress;

	private final CheckBox useSSL;

	private final TextField bounceEmailAddress;
	private final VerticalLayout layout;

	public SmtpStep(final GroupMaintenanceWizardView setupWizardView)
	{
		super(new DaoFactory().getSMTPSettingsDao(), SMTPServerSetting.class);

		this.layout = new VerticalLayout();
		this.layout.setMargin(true);
		final MultiColumnFormLayout<SMTPServerSetting> formLayout = new MultiColumnFormLayout<>(1, getFieldGroup());
		formLayout.setColumnFieldWidth(0, 250);

		final Label label = new Label("<h1>Configure SMTP mail settings.</h1>");
		label.setContentMode(ContentMode.HTML);
		this.layout.addComponent(label);
		this.layout.addComponent(formLayout);

		// Create the user input fields
		this.smtpFQDN = formLayout.bindTextField("SMTP FQDN:", SMTPServerSetting_.smtpFQDN);
		this.smtpFQDN.setDescription("SMTP Server FQDN or IP address");

		this.smtpPort = formLayout.bindTextField("SMTP Port:", SMTPServerSetting_.smtpPort);
		this.smtpPort.setDescription("SMTP Port No.");
		this.smtpPort.addValidator(
				new IntegerRangeValidator("The port no. must be an integer in the range 1 to 65535", 1, 65535));

		this.authRequired = formLayout.bindBooleanField("Authentication Requried", SMTPServerSetting_.authRequired);
		this.authRequired.addValueChangeListener(this);

		this.username = formLayout.bindTextField("Username:", SMTPServerSetting_.username);
		this.username.setDescription("SMTP username if authentication is used");
		this.username.setVisible(false);

		// Create the password input field
		this.password = formLayout.bindPasswordField("Password:", SMTPServerSetting_.password);
		this.password.setDescription("SMS Provider Password");
		this.password.setVisible(false);

		this.useSSL = formLayout.bindBooleanField("Use SSL", SMTPServerSetting_.useSSL);
		this.useSSL.setDescription("Enables an SSL connection to your SMTP server if it supports it.");

		this.fromEmailAddress = formLayout.bindTextField("From Email Address:", SMTPServerSetting_.fromEmailAddress);
		this.fromEmailAddress.setDescription("Default From Address to use when sending bulk emails.");
		this.fromEmailAddress.addValidator(new EmailValidator("Enter a valid email address."));

		this.bounceEmailAddress = formLayout.bindTextField("Bounce Email Address:",
				SMTPServerSetting_.bounceEmailAddress);
		this.bounceEmailAddress.setDescription("Email Address that bounced emails should be sent to.");
		this.bounceEmailAddress.addValidator(new EmailValidator("Enter a valid email address."));

		final Button test = new Button("Test");
		this.layout.addComponent(test);
		test.addClickListener(new ClickEventLogged.ClickAdaptor(this));

		// focus the fqnd field when user arrives to the login view
		this.smtpFQDN.focus();

	}

	@Override
	public String getCaption()
	{

		return "SMTP Settings";
	}

	@Override
	public Component getContent(final ValidatingFieldGroup<SMTPServerSetting> fieldGroup)
	{

		return this.layout;
	}

	@Override
	public void valueChange(final ValueChangeEvent event)
	{
		if (event.getProperty() == this.authRequired)
		{
			final Boolean authRequired = (Boolean) event.getProperty().getValue();

			this.username.setVisible(authRequired);
			this.password.setVisible(authRequired);
		}
	}

	@Override
	protected void initEntity(final SMTPServerSetting entity)
	{
		entity.setAuthRequired(false);
	}

	@Override
	protected SMTPServerSetting findEntity()
	{
		SMTPServerSetting setting = null;
		final List<SMTPServerSetting> settings = new DaoFactory().getSMTPSettingsDao().findAll();
		if (settings.size() > 1)
		{
			throw new IllegalStateException(
					"More than one EmailServerSetting has been found which is not valid during initial setup.");
		}
		if (settings.size() == 1)
		{
			setting = settings.get(0);
		}

		if (setting != null)
		{
			this.username.setVisible(setting.isAuthRequired());
			this.password.setVisible(setting.isAuthRequired());
		}

		return setting;
	}

	@Override
	public void buttonClick(final ClickEvent event)
	{
		// test that the SMS Provider configuration works.

		// First check that the entered details are valid.
		if (super.validate())
		{
			final SMTPServerSetting settings = super.getEntity();
			if (settings == null)
			{
				Notification.show(
						"Can't find the SMTP Settings, this usually means the install did not complete correctly.",
						Type.ERROR_MESSAGE);
			}
			else
			{
				final CompositeValidator validator = new CompositeValidator();
				validator.addValidator(new EmailValidator("Please input your email address"));
				validator.addValidator(new StringLengthValidator("Please enter an email address.", 6, 255, false));
				InputDialog d = new InputDialog(UI.getCurrent(), "Test SMTP Settings.",
						"Enter your email address to recieve a test Email", new Recipient()
						{
							@Override
							public boolean onOK(final String input)
							{
								final String toEmailAddress = input;

								final SMTPSettingsDao daoSMTPSettings = new DaoFactory().getSMTPSettingsDao();

								try
								{
									final StringBuilder sb = new StringBuilder();
									sb.append(
											"If you receive this email then your Scoutmaster email settings are all correct.\n\n");
									sb.append("So welcome to Scoutmaster.\n\n");
									sb.append("May you live long and recruit many.\n");

									daoSMTPSettings.sendEmail(settings, settings.getFromEmailAddress(),
											SMSession.INSTANCE.getLoggedInUser().getEmailAddress(),
											new SMTPSettingsDao.EmailTarget(EmailAddressType.To, toEmailAddress),
											"Test email from Scoutmaster setup", sb.toString());

									SMNotification.show("An email has been sent to: " + toEmailAddress
											+ " please check that the email arrived.", Type.HUMANIZED_MESSAGE);

								}
								catch (final EmailException e)
								{
									SmtpStep.logger.error(e, e);
									SMNotification.show(e, Type.ERROR_MESSAGE);
								}
								return true;
							}

							@Override
							public boolean onCancel()
							{
								return true;
							}
						});
				d.addValidator(validator);

				d.setWidth("600");
				d.setHeight("250");

			}
		}

	}
}
