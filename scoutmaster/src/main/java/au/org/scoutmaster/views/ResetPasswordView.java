package au.org.scoutmaster.views;

import au.com.vaadinutils.listener.ClickEventLogged;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.access.UserDao;
import au.org.scoutmaster.domain.access.User;
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
		user = new TextField("Username:");
		user.setWidth("300px");
		user.setRequired(true);
		user.setInputPrompt("Your username");
		user.addValidator(new UsernameValidator());
		user.setInvalidAllowed(false);

		// Create the password input field
		passwordField = new PasswordField("Password:");
		passwordField.setWidth("300px");
		passwordField.addValidator(new PasswordValidator("Password"));
		passwordField.setRequired(true);
		passwordField.setValue("");
		passwordField.setNullRepresentation("");

		// Create the password input field
		confirmField = new PasswordField("Confirm Password:");
		confirmField.setWidth("300px");
		confirmField.addValidator(new PasswordValidator("Confirm Password"));
		confirmField.setRequired(true);
		confirmField.setValue("");
		confirmField.setNullRepresentation("");

		// Create login button
		loginButton = new Button("Login", new ClickEventLogged.ClickAdaptor(this));

		// Add both to a panel
		VerticalLayout fields = new VerticalLayout(user, passwordField, confirmField, loginButton);
		fields.setCaption("Please enter your username and new password.");
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

	
	@Override
	public void buttonClick(ClickEvent event)
	{

		//
		// Validate the fields using the navigator. By using validators for the
		// fields we reduce the amount of queries we have to make to the database
		// for wrongly entered passwords
		//
		if (!user.isValid() || !passwordField.isValid())
		{
			return;
		}

		String username = user.getValue();
		String password = this.passwordField.getValue();
		String confirm = this.confirmField.getValue();

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
			UserDao daoUser = new DaoFactory().getUserDao();

			User user = daoUser.findByName(username);
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
				Notification.show("The entered username does not exist. Your username is your email address.", Type.ERROR_MESSAGE);
				this.user.focus();
			}

		}
	}
}