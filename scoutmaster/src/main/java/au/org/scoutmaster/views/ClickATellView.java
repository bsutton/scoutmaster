package au.org.scoutmaster.views;

import au.org.scoutmaster.domain.access.User;

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class ClickATellView extends CustomComponent implements View, Button.ClickListener
{
	private static final long serialVersionUID = 1L;

	public static final String NAME = "login";

	private final TextField user;

	private final PasswordField password;

	private final Button loginButton;

	public ClickATellView()
	{
		setSizeFull();

		// Create the user input field
		user = new TextField("User:");
		// HACK: during development
		user.setValue("bsutton@noojee.com.au");
		user.setWidth("300px");
		user.setRequired(true);
		user.setInputPrompt("Your username (eg. joe@email.com)");
		user.addValidator(new EmailValidator("Username must be an email address"));
		user.setImmediate(true);
		user.setInvalidAllowed(false);

		// Create the password input field
		password = new PasswordField("Password:");
		password.setWidth("300px");
		password.addValidator(new PasswordValidator());
		password.setRequired(true);
		// HACK: during development
		password.setValue("password");
		password.setNullRepresentation("");

		// Create login button
		loginButton = new Button("Login", this);
		loginButton.setClickShortcut(KeyCode.ENTER);
		loginButton.addStyleName("default");

		// Add both to a panel
		VerticalLayout fields = new VerticalLayout(user, password, loginButton);
		fields.setCaption("Please login to access Scoutmaster.");
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

		//
		// Validate the fields using the navigator. By using validors for the
		// fields we reduce the amount of queries we have to use to the database
		// for wrongly entered passwords
		//
		if (user.isValid() && password.isValid())
		{
			String username = user.getValue();
			String password = this.password.getValue();

			User user = User.findUser(username);
			if (user.validatePassword(password))
			{
				// Store the current user in the service session
				getSession().setAttribute("user", user);

				// Navigate to main view
				getUI().getNavigator().navigateTo(ContactView.NAME);

			}
			else
			{

				// Wrong password clear the password field and refocuses it
				this.password.setValue(null);
				this.password.focus();
			}
		}
		else
		{
			//user.setComponentError(new UserError("I dont like you"));
			Notification.show("Invalid username or password");
		}
	}

	
}