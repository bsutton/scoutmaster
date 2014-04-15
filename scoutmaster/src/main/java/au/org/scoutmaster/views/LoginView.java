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

	private final Button forgottenButton;

	public LoginView()
	{
		setSizeFull();

		// The view root layout
		final VerticalLayout viewLayout = new VerticalLayout();
		viewLayout.setSizeFull();
		viewLayout.setStyleName(Reindeer.LAYOUT_BLACK);

		final HorizontalLayout logo = new HorizontalLayout();
		logo.setWidth("100%");

		final String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		final FileResource resource = new FileResource(new File(basepath + "/WEB-INF/images/scoutmaster-logo.png"));

		final Image image = new Image(null, resource);
		image.setAlternateText("Scoutmaster Logo");
		logo.addComponent(image);
		logo.setComponentAlignment(image, Alignment.TOP_RIGHT);
		viewLayout.addComponent(logo);

		final VerticalLayout fields = new VerticalLayout();
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, true));
		fields.setSizeUndefined();

		// Add both to a panel
		final Label label = new Label("<H1>Login to Scoutmaster</H1>");
		label.setContentMode(ContentMode.HTML);

		fields.addComponent(label);

		// Create the user input field
		this.usernameField = new TextField("Username:");
		this.usernameField.setWidth("300px");
		this.usernameField.setRequired(true);
		this.usernameField.setInputPrompt("Your username");
		this.usernameField.setImmediate(true);
		this.usernameField.setInvalidAllowed(false);
		fields.addComponent(this.usernameField);

		// Create the password input field
		this.passwordField = new PasswordField("Password:");
		this.passwordField.setWidth("300px");

		this.passwordField.setRequired(true);
		this.passwordField.setNullRepresentation("");

		fields.addComponent(this.passwordField);

		// Buttons
		final HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);

		// Create login button
		this.loginButton = new Button("Login", new ClickEventLogged.ClickAdaptor(this));
		this.loginButton.setClickShortcut(KeyCode.ENTER);
		this.loginButton.addStyleName(Reindeer.BUTTON_DEFAULT);
		buttons.addComponent(this.loginButton);
		buttons.setComponentAlignment(this.loginButton, Alignment.MIDDLE_LEFT);

		this.forgottenButton = new Button("Forgotten Password", new ClickEventLogged.ClickAdaptor(this));
		buttons.addComponent(this.forgottenButton);
		buttons.setComponentAlignment(this.forgottenButton, Alignment.MIDDLE_RIGHT);
		fields.addComponent(buttons);

		viewLayout.addComponent(fields);
		viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);

		setCompositionRoot(viewLayout);

		viewLayout.addComponent(fields);
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		// focus the username field when user arrives to the login view
		this.usernameField.focus();
	}

	@Override
	public void buttonClick(final ClickEvent event)
	{

		if (event.getButton() == this.forgottenButton)
		{
			UI.getCurrent().getNavigator().navigateTo(ForgottenPasswordView.NAME);
		}
		else
		{
			final String username = this.usernameField.getValue();
			final String password = this.passwordField.getValue();

			final UserDao daoUser = new DaoFactory().getUserDao();
			final LoginAttemptDao daoLoginAttempt = new DaoFactory().getLoginAttemptDao();

			// check the user hasn't exceed there login attempts.
			if (!daoLoginAttempt.hasExceededAttempts(username))
			{
				final User user = daoUser.findByName(username);
				if (user != null && user.isEnabled() && user.isValidPassword(password))
				{
					// Store the current user in the service session
					SMSession.INSTANCE.setLoggedInUser(user);

					final LoginAttempt attempt = new LoginAttempt(user, true);
					daoLoginAttempt.persist(attempt);

					// Navigate to main view
					getUI().getNavigator().navigateTo("");
				}
				else
				{
					final LoginAttempt attempt = new LoginAttempt(user, false);
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
				Notification.show("Login Attempts have been exceed.",
						"You are now blocked until " + blockedUntil.toString("h:mm") + ".", Type.TRAY_NOTIFICATION);
			}
		}
	}

}