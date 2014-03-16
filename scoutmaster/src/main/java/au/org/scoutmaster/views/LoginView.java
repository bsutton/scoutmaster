package au.org.scoutmaster.views;

import java.io.File;

import org.joda.time.DateTime;

import au.com.vaadinutils.listener.ClickEventLogged;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.access.LoginAttemptDao;
import au.org.scoutmaster.dao.access.UserDao;
import au.org.scoutmaster.domain.access.LoginAttempt;
import au.org.scoutmaster.domain.access.User;

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

	private final TextField usernameField;

	private final PasswordField passwordField;

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
		usernameField = new TextField("Username:");
		usernameField.setWidth("300px");
		usernameField.setRequired(true);
		usernameField.setInputPrompt("Your username");
		usernameField.setImmediate(true);
		usernameField.setInvalidAllowed(false);
		fields.addComponent(usernameField);

		// Create the password input field
		passwordField = new PasswordField("Password:");
		passwordField.setWidth("300px");

		passwordField.setRequired(true);
		passwordField.setNullRepresentation("");

		fields.addComponent(passwordField);

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
		usernameField.focus();
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
			String username = this.usernameField.getValue();
			String password = this.passwordField.getValue();

			UserDao daoUser = new DaoFactory().getUserDao();
			LoginAttemptDao daoLoginAttempt = new DaoFactory().getLoginAttemptDao();

			// check the user hasn't exceed there login attempts.
			if (!daoLoginAttempt.hasExceededAttempts(username))
			{
				User user = daoUser.findByName(username);
				if (user != null && user.isEnabled() && user.isValidPassword(password))
				{
					// Store the current user in the service session
					SMSession.INSTANCE.setLoggedInUser(user);

					LoginAttempt attempt = new LoginAttempt(user, true);
					daoLoginAttempt.persist(attempt);

					// Navigate to main view
					getUI().getNavigator().navigateTo("");
				}
				else
				{
					LoginAttempt attempt = new LoginAttempt(user, false);
					daoLoginAttempt.persist(attempt);

					// Wrong password clear the password field and refocuses it
					this.usernameField.focus();
					Notification.show("Invalid username or password!", Type.TRAY_NOTIFICATION);
				}
			}
			else
			{
				this.usernameField.focus();
				DateTime blockedUntil = daoLoginAttempt.blockedUtil(username);
				// round up to the nearest minute.
				blockedUntil = blockedUntil.plusMinutes(1);
				blockedUntil = blockedUntil.minusSeconds(blockedUntil.getSecondOfMinute());
				Notification.show("Login Attempts have been exceed."
						,  "You are now blocked until " + blockedUntil.toString("h:mm") + ".", Type.TRAY_NOTIFICATION);
			}
		}
	}

}