package au.org.scoutmaster.views;

import java.util.List;

import au.org.scoutmaster.application.Menu;
import au.org.scoutmaster.dao.SMSProviderDao;
import au.org.scoutmaster.domain.SMSProvider;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@Menu(display = "ClickATellView")
public class ClickATellView extends CustomComponent implements View, Button.ClickListener
{
	private static final long serialVersionUID = 1L;

	public static final String NAME = "ClickATellView";

	private final TextField user;
	private final TextField apiId;

	private final PasswordField password;

	private final Button saveButton;

	public ClickATellView()
	{
		setSizeFull();
		
		SMSProviderDao daoSMSProvider = new SMSProviderDao();
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

		// Create login button
		saveButton = new Button("Save", this);
		saveButton.setClickShortcut(KeyCode.ENTER);
		saveButton.addStyleName("default");

		// Add both to a panel
		VerticalLayout fields = new VerticalLayout(user, password, apiId, saveButton);
		fields.setCaption("Configure Click A Tell provider settings.");
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, false));
		fields.setSizeUndefined();

		// The view root layout
		VerticalLayout viewLayout = new VerticalLayout(fields);
		viewLayout.setSizeFull();
		viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
		viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
		setCompositionRoot(viewLayout);
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		// focus the username field when user arrives to the login view
		user.focus();
	}

	/**
	 * Save button click so lets save the details.
	 */
	@Override
	public void buttonClick(ClickEvent event)
	{

		SMSProviderDao daoSMSProvider = new SMSProviderDao();
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