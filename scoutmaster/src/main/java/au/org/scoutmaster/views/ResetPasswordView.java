package au.org.scoutmaster.views;

import au.com.vaadinutils.listener.ClickEventLogged;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.security.UserDao;
import au.org.scoutmaster.domain.security.User;
import au.org.scoutmaster.validator.PasswordValidator;
import au.org.scoutmaster.validator.UsernameValidator;

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

public class ResetPasswordView extends CustomComponent implements View, Button.ClickListener
{
	private static final long serialVersionUID = 1L;

	public static final String NAME = "ResetPassword";

	private final TextField user;

	private final PasswordField passwordField;

	private final PasswordField confirmField;

	private final Button loginButton;

	public ResetPasswordView()
	{
		setSizeFull();

		// Create the user input field
		this.user = new TextField("Username:");
		this.user.setWidth("300px");
		this.user.setRequired(true);
		this.user.setInputPrompt("Your username");
		this.user.addValidator(new UsernameValidator());
		this.user.setInvalidAllowed(false);

		// Create the password input field
		this.passwordField = new PasswordField("Password:");
		this.passwordField.setWidth("300px");
		this.passwordField.addValidator(new PasswordValidator("Password"));
		this.passwordField.setRequired(true);
		this.passwordField.setValue("");
		this.passwordField.setNullRepresentation("");

		// Create the password input field
		this.confirmField = new PasswordField("Confirm Password:");
		this.confirmField.setWidth("300px");
		this.confirmField.addValidator(new PasswordValidator("Confirm Password"));
		this.confirmField.setRequired(true);
		this.confirmField.setValue("");
		this.confirmField.setNullRepresentation("");

		// Create login button
		this.loginButton = new Button("Login", new ClickEventLogged.ClickAdaptor(this));

		// Add both to a panel
		final VerticalLayout fields = new VerticalLayout(this.user, this.passwordField, this.confirmField,
				this.loginButton);
		fields.setCaption("Please enter your username and new password.");
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
		this.user.focus();
	}

	@Override
	public void buttonClick(final ClickEvent event)
	{

		//
		// Validate the fields using the navigator. By using validators for the
		// fields we reduce the amount of queries we have to make to the
		// database
		// for wrongly entered passwords
		//
		if (!this.user.isValid() || !this.passwordField.isValid())
		{
			return;
		}

		final String username = this.user.getValue();
		final String password = this.passwordField.getValue();
		final String confirm = this.confirmField.getValue();

		//
		// Validate username and password with database here. For examples sake
		// I use a dummy username and password.
		//
		if (!password.equals(confirm))
		{
			Notification.show("The password and confirm passwords do not match", Type.ERROR_MESSAGE);
			this.passwordField.focus();
		}
		else
		{
			final UserDao daoUser = new DaoFactory().getUserDao();

			final User user = daoUser.findByName(username);
			if (user != null)
			{
				daoUser.updatePassword(user, password);
				// Store the current user in the service session
				getSession().setAttribute("user", username);

				// Navigate to main view
				getUI().getNavigator().navigateTo(ContactView.NAME);
			}
			else
			{
				Notification.show("The entered username does not exist. Your username is your email address.",
						Type.ERROR_MESSAGE);
				this.user.focus();
			}

		}
	}
}