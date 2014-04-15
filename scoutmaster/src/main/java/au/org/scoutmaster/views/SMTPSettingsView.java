package au.org.scoutmaster.views;

import au.com.vaadinutils.listener.ClickEventLogged;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMTPSettingsDao;
import au.org.scoutmaster.domain.SMTPServerSettings;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@Menu(display = "SMTP Settings", path = "Admin")
public class SMTPSettingsView extends CustomComponent implements View, ValueChangeListener, ClickListener
{
	private static final long serialVersionUID = 1L;

	public static final String NAME = "SMTPSettingsView";

	private final TextField smtpFQDN;

	private final TextField smtpPort;

	private final CheckBox authRequired;

	private final TextField username;

	private final PasswordField password;

	private final TextField fromEmailAddress;

	private final Button saveButton;

	public SMTPSettingsView()
	{
		setSizeFull();

		final SMTPSettingsDao daoEMailServerSettings = new DaoFactory().getSMTPSettingsDao();
		final SMTPServerSettings settings = daoEMailServerSettings.findSettings();

		if (settings == null)
		{
			throw new IllegalStateException("The email Server Settings are missing from the database.");
		}

		// Create the user input field
		this.smtpFQDN = new TextField("SMTP FQDN:");
		this.smtpFQDN.setWidth("300px");
		this.smtpFQDN.setRequired(true);
		this.smtpFQDN.setDescription("SMTP Server FQDN or IP address");
		this.smtpFQDN.setImmediate(true);
		this.smtpFQDN.setValue(settings.getSmtpFQDN());

		this.smtpPort = new TextField("SMTP Port:");
		this.smtpPort.setWidth("300px");
		this.smtpPort.setRequired(true);
		this.smtpPort.setDescription("SMTP Server FQDN or IP address");
		this.smtpPort.setImmediate(true);
		this.smtpPort.setValue(settings.getSmtpPort().toString());
		this.smtpPort.addValidator(new IntegerRangeValidator("The port no. must be an integer in the range 1 to 65535",
				1, 65535));

		this.authRequired = new CheckBox("SMTP Authentication Requried");
		this.authRequired.setValue(settings.isAuthRequired());
		this.authRequired.addValueChangeListener(this);

		this.username = new TextField("SMTP Username:");
		this.username.setWidth("300px");
		this.username.setRequired(true);
		this.username.setDescription("SMTP username if authentication is used");
		this.username.setImmediate(true);
		this.username.setValue(settings.getUsername());

		// Create the password input field
		this.password = new PasswordField("Password:");
		this.password.setWidth("300px");
		this.password.setRequired(true);
		this.password.setNullRepresentation("");
		this.password.setDescription("SMS Provider Password");
		this.password.setValue(settings.getPassword());

		// Create the user input field
		this.fromEmailAddress = new TextField("From Email Address:");
		this.fromEmailAddress.setWidth("300px");
		this.fromEmailAddress.setRequired(true);
		this.fromEmailAddress.setDescription("Default From Address to use when sending bulk emails.");
		this.fromEmailAddress.setImmediate(true);
		this.fromEmailAddress.addValidator(new EmailValidator("Enter a valid email address."));
		this.fromEmailAddress.setValue(settings.getFromEmailAddress());

		// Create login button
		this.saveButton = new Button("Save", new ClickEventLogged.ClickAdaptor(this));
		this.saveButton.setClickShortcut(KeyCode.ENTER);
		this.saveButton.addStyleName("default");

		// Add both to a panel
		final VerticalLayout fields = new VerticalLayout(this.smtpFQDN, this.authRequired, this.username,
				this.password, this.fromEmailAddress, this.saveButton);
		fields.setCaption("Configure SMTP mail settings.");
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, false));
		fields.setSizeUndefined();

		// The view root layout
		final VerticalLayout viewLayout = new VerticalLayout(fields);
		viewLayout.setSizeFull();
		viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
		viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
		setCompositionRoot(viewLayout);
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		// focus the username field when user arrives to the login view
		this.smtpFQDN.focus();
	}

	/**
	 * Save button click so lets save the details.
	 */
	@Override
	public void buttonClick(final ClickEvent event)
	{
		final SMTPSettingsDao daoEMailServerSettings = new DaoFactory().getSMTPSettingsDao();
		final SMTPServerSettings settings = daoEMailServerSettings.findSettings();

		if (settings == null)
		{
			throw new IllegalStateException("The email Server Settings are missing from the database.");
		}

		settings.setSmtpFQDN(this.smtpFQDN.getValue());
		settings.setAuthRequired(this.authRequired.getValue());
		settings.setUsername(this.username.getValue());
		settings.setPassword(this.password.getValue());
		settings.setFromEmailAddress(this.fromEmailAddress.getValue());

		daoEMailServerSettings.persist(settings);
		Notification.show("SMTP Server details have been saved.", Type.TRAY_NOTIFICATION);

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

}