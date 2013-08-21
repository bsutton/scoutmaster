package au.org.scoutmaster.views.wizards.setup;

import org.vaadin.teemu.wizards.WizardStep;

import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.EMailServerSettingsDao;
import au.org.scoutmaster.domain.EMailServerSettings;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.shared.ui.MarginInfo;
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
import com.vaadin.ui.VerticalLayout;

public class SmtpStep implements WizardStep, ClickListener, ValueChangeListener
{
	private static final long serialVersionUID = 1L;

	private TextField smtpFQDN;

	private TextField smtpPort;

	private CheckBox authRequired;

	private TextField username;

	private PasswordField password;

	private TextField fromEmailAddress;

	private Button saveButton;

	public SmtpStep(SetupWizardView setupWizardView)
	{
	}

	@Override
	public String getCaption()
	{
		return "SMTP Settings";
	}

	@Override
	public Component getContent()
	{

		EMailServerSettingsDao daoEMailServerSettings = new DaoFactory().getEMailServerSettingsDao();
		EMailServerSettings settings = daoEMailServerSettings.findSettings();

		if (settings == null)
			throw new IllegalStateException("The email Server Settings are missing from the database.");

		// Create the user input field
		smtpFQDN = new TextField("SMTP FQDN:");
		smtpFQDN.setWidth("300px");
		smtpFQDN.setRequired(true);
		smtpFQDN.setDescription("SMTP Server FQDN or IP address");
		smtpFQDN.setImmediate(true);
		smtpFQDN.setValue(settings.getSmtpFQDN());

		smtpPort = new TextField("SMTP Port:");
		smtpPort.setWidth("300px");
		smtpPort.setRequired(true);
		smtpPort.setDescription("SMTP Port No.");
		smtpPort.setImmediate(true);
		smtpPort.setValue(settings.getSmtpPort().toString());
		smtpPort.addValidator(new IntegerRangeValidator("The port no. must be an integer in the range 1 to 65535", 1,
				65535));

		authRequired = new CheckBox("SMTP Authentication Requried");
		authRequired.setValue(settings.isAuthRequired());
		authRequired.addValueChangeListener(this);

		username = new TextField("SMTP Username:");
		username.setWidth("300px");
		username.setRequired(true);
		username.setDescription("SMTP username if authentication is used");
		username.setImmediate(true);
		username.setValue(settings.getUsername());
		username.setNullRepresentation("");

		// Create the password input field
		password = new PasswordField("Password:");
		password.setWidth("300px");
		password.setRequired(true);
		password.setNullRepresentation("");
		password.setDescription("SMS Provider Password");
		password.setValue(settings.getPassword());

		// Username and password fields must exist before we call this.
		authRequired.setValue(settings.isAuthRequired());
		username.setVisible(settings.isAuthRequired());
		password.setVisible(settings.isAuthRequired());



		// Create the user input field
		fromEmailAddress = new TextField("From Email Address:");
		fromEmailAddress.setWidth("300px");
		fromEmailAddress.setRequired(true);
		fromEmailAddress.setDescription("Default From Address to use when sending bulk emails.");
		fromEmailAddress.setImmediate(true);
		fromEmailAddress.addValidator(new EmailValidator("Enter a valid email address."));
		fromEmailAddress.setValue(settings.getFromAddress());
		fromEmailAddress.setNullRepresentation("");

		// Create login button
		saveButton = new Button("Save", this);
		saveButton.setClickShortcut(KeyCode.ENTER);
		saveButton.addStyleName("default");

		// Add both to a panel
		VerticalLayout fields = new VerticalLayout(smtpFQDN, smtpPort, authRequired, username, password, fromEmailAddress,
				saveButton);
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, false));
		fields.setSizeUndefined();

		// The view root layout
		VerticalLayout viewLayout = new VerticalLayout();
		viewLayout.setMargin(true);
		
		Label title = new Label("<H1>Configure SMTP mail settings.</H1>");
		title.setContentMode(ContentMode.HTML);
		viewLayout.addComponent(title);
		viewLayout.addComponents(fields);

		// focus the username field when user arrives to the login view
		smtpFQDN.focus();

		return viewLayout;
	}

	@Override
	public boolean onAdvance()
	{
		return true;
	}

	@Override
	public boolean onBack()
	{
		return true;
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
	public void buttonClick(ClickEvent event)
	{
		EMailServerSettingsDao daoEMailServerSettings = new DaoFactory().getEMailServerSettingsDao();
		EMailServerSettings settings = daoEMailServerSettings.findSettings();

		if (settings == null)
			throw new IllegalStateException("The email Server Settings are missing from the database.");

		settings.setSmtpFQDN(smtpFQDN.getValue());
		settings.setSmtpPort(Integer.valueOf(smtpPort.getValue()));
		settings.setAuthRequired(authRequired.getValue());
		settings.setUsername(username.getValue());
		settings.setPassword(password.getValue());
		settings.setFromEmailAddress(fromEmailAddress.getValue());

		daoEMailServerSettings.persist(settings);
		Notification.show("SMTP Server details have been saved.", Type.TRAY_NOTIFICATION);

	}

}
