package au.org.scoutmaster.views.wizards.setup;

import java.util.List;

import org.vaadin.teemu.wizards.WizardStep;

import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMSProviderDao;
import au.org.scoutmaster.domain.SMSProvider;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.shared.ui.MarginInfo;
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
import com.vaadin.ui.VerticalLayout;

public class SmsProviderStep implements WizardStep, ClickListener
{
	private static final long serialVersionUID = 1L;
	private TextField user;
	private TextField apiId;

	private PasswordField password;

	private Button saveButton;

	public SmsProviderStep(SetupWizardView setupWizardView)
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

		SMSProviderDao daoSMSProvider = new DaoFactory().getSMSProviderDao();
		List<SMSProvider> provider = daoSMSProvider.findByName("ClickATell");

		if (provider.size() != 1)
			throw new IllegalStateException("The ClickATell Provider is missing from the database.");

		SMSProvider clickATellProvider = provider.get(0);

		// Create the user input field
		user = new TextField("Username:");
		user.setWidth("300px");
		user.setRequired(true);
		user.setDescription("SMS Provider Username");
		user.setImmediate(true);
		user.setValue(clickATellProvider.getUsername());
		user.setNullRepresentation("");

		// Create the password input field
		password = new PasswordField("Password:");
		password.setWidth("300px");
		password.setRequired(true);
		password.setNullRepresentation("");
		password.setDescription("SMS Provider Password");
		password.setValue(clickATellProvider.getPassword());
		

		// Create the user input field
		apiId = new TextField("Api Id:");
		apiId.setWidth("300px");
		apiId.setRequired(true);
		apiId.setDescription("SMS Providier API key");
		apiId.setImmediate(true);
		apiId.setValue(clickATellProvider.getApiId());
		apiId.setNullRepresentation("");

		// Create login button
		saveButton = new Button("Save", this);
		saveButton.setClickShortcut(KeyCode.ENTER);
		saveButton.addStyleName("default");

		// Add both to a panel
		VerticalLayout fields = new VerticalLayout(user, password, apiId, saveButton);
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, false));
		//fields.setSizeUndefined();
		

		// The view root layout
		VerticalLayout viewLayout = new VerticalLayout();
	//	viewLayout.setSizeFull();
		viewLayout.setMargin(true);
		Label title = new Label("<H1>Configure Click A Tell provider settings.</H1>");
		title.setContentMode(ContentMode.HTML);
		viewLayout.addComponent(title);
		viewLayout.addComponents(fields);
		
		//viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);

		// focus the username field when user arrives to the login view
		user.focus();

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
	public void buttonClick(ClickEvent event)
	{

		SMSProviderDao daoSMSProvider = new DaoFactory().getSMSProviderDao();
		List<SMSProvider> provider = daoSMSProvider.findByName("ClickATell");

		if (provider.size() != 1)
			throw new IllegalStateException("The ClickATell Provider is missing from the database.");

		SMSProvider clickATellProvider = provider.get(0);

		clickATellProvider.setUsername(user.getValue());
		clickATellProvider.setPassword(password.getValue());
		clickATellProvider.setApiId(apiId.getValue());
		clickATellProvider.setActive(true);
		clickATellProvider.setDefaultProvider(true);

		daoSMSProvider.persist(clickATellProvider);
		Notification.show("Provider details have been saved.", Type.TRAY_NOTIFICATION);

	}

}
