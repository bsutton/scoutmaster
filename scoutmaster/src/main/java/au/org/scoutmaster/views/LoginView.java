package au.org.scoutmaster.views;

import java.io.File;

import au.com.vaadinutils.listener.ClickEventLogged;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.access.UserDao;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.validator.UsernameValidator;

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class LoginView extends CustomComponent implements View, Button.ClickListener
{
	private static final long serialVersionUID = 1L;

	public static final String NAME = "login";

	private final TextField user;

	private final PasswordField password;

	private final Button loginButton;

	private Button forgottenButton;

	public LoginView()
	{
		setSizeFull();

		VerticalLayout fields = new VerticalLayout();
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, true));
		fields.setSizeUndefined();

		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource resource = new FileResource(new File(basepath + "/WEB-INF/images/scoutmaster-logo.png"));

		Image image = new Image(null, resource);
		image.setAlternateText("Scoutmaster Logo");
		fields.addComponent(image);

		// Add both to a panel
		Label label = new Label("<H1>Login to Scoutmaster.</H1>");
		label.setContentMode(ContentMode.HTML);

		fields.addComponent(label);

		// Create the user input field
		user = new TextField("Username:");
		user.setWidth("300px");
		user.setRequired(true);
		user.setInputPrompt("Your username");
		user.setImmediate(true);
		user.setInvalidAllowed(false);
		user.addValidator(new UsernameValidator());
		fields.addComponent(user);

		// Create the password input field
		password = new PasswordField("Password:");
		password.setWidth("300px");

		password.setRequired(true);
		password.setNullRepresentation("");
		password.addValidator(new PasswordValidator());

		fields.addComponent(password);

		// Buttons
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);

		// Create login button
		loginButton = new Button("Login", new ClickEventLogged.ClickAdaptor(this));
		loginButton.setClickShortcut(KeyCode.ENTER);
		loginButton.addStyleName(Reindeer.BUTTON_DEFAULT);
		buttons.addComponent(loginButton);
		buttons.setComponentAlignment(loginButton, Alignment.MIDDLE_LEFT);

		forgottenButton = new Button("Forgotten Password", new ClickEventLogged.ClickAdaptor(this));
		buttons.addComponent(forgottenButton);
		buttons.setComponentAlignment(forgottenButton, Alignment.MIDDLE_RIGHT);
		fields.addComponent(buttons);

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

	//
	// Validator for validating the passwords
	//
	private static final class PasswordValidator extends AbstractValidator<String>
	{
		private static final long serialVersionUID = 1L;

		public PasswordValidator()
		{
			super("The password provided is not valid");
		}

		@Override
		protected boolean isValidValue(String value)
		{
			//
			// Password must be at least 8 characters long and contain at least
			// one number
			//
			if (value != null && (value.length() < 8 || !value.matches(".*")))
			{
				return false;
			}
			return true;
		}

		@Override
		public Class<String> getType()
		{
			return String.class;
		}
	}

	@Override
	public void buttonClick(ClickEvent event)
	{

		if (event.getButton() == forgottenButton)
		{
			UI.getCurrent().getNavigator().navigateTo(ForgottenPasswordView.NAME);
		}
		else
		{
			//
			// Validate the fields using the navigator. By using validors for
			// the
			// fields we reduce the amount of queries we have to use to the
			// database
			// for wrongly entered passwords
			//
			if (user.isValid() && password.isValid())
			{
				String username = user.getValue();
				String password = this.password.getValue();

				UserDao daoUser = new DaoFactory().getUserDao();

				User user = daoUser.findByName(username);
				if (user.isValidPassword(password))
				{
					// Store the current user in the service session
					SMSession.INSTANCE.setLoggedInUser(user);

					// Navigate to main view
					getUI().getNavigator().navigateTo(ContactView.NAME);
				}
				else
				{

					// Wrong password clear the password field and refocuses it
					this.user.focus();
					Notification.show("Invalid username or password!", Type.TRAY_NOTIFICATION);
				}
			}
			else
			{
				// user.setComponentError(new UserError("I dont like you"));
				this.user.focus();
				Notification.show("Invalid username or password", Type.TRAY_NOTIFICATION);
			}
		}
	}

}