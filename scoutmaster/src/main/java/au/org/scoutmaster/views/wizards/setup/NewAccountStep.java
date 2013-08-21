package au.org.scoutmaster.views.wizards.setup;

import org.vaadin.teemu.wizards.WizardStep;

import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.access.UserDao;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.validator.PasswordValidator;
import au.org.scoutmaster.validator.UsernameValidator;

import com.vaadin.data.validator.EmailValidator;
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
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class NewAccountStep implements WizardStep, ClickListener
{
	private static final long serialVersionUID = 1L;

	private TextField user;

	private PasswordField password;

	private PasswordField confirmPassword;

	private TextField emailAddress;

	private TextField confirmEmailAddress;

	private Button createButton;

	private boolean created = false;

	private VerticalLayout viewLayout;

	public NewAccountStep(SetupWizardView setupWizardView)
	{
		// Create the user input field
		user = new TextField("Username:");
		user.setWidth("300px");
		user.setRequired(true);
		user.setInputPrompt("Your username");
		user.addValidator(new UsernameValidator());
		user.setImmediate(true);
		user.setInvalidAllowed(false);

		// Create the password input field
		password = new PasswordField("Password:");
		password.setWidth("300px");
		password.addValidator(new PasswordValidator("Password"));
		password.setRequired(true);
		password.setNullRepresentation("");

		// Create the password input field
		confirmPassword = new PasswordField("Confirm Password:");
		confirmPassword.setWidth("300px");
		confirmPassword.addValidator(new PasswordValidator("Confirm Password"));
		confirmPassword.setRequired(true);
		confirmPassword.setNullRepresentation("");

		// Create the password input field
		emailAddress = new TextField("Email Address:");
		emailAddress.setWidth("300px");
		emailAddress.addValidator(new EmailValidator("Enter your email address."));
		emailAddress.setRequired(true);
		emailAddress.setNullRepresentation("");

		// Create the password input field
		confirmEmailAddress = new TextField("Confirm Email Address:");
		confirmEmailAddress.setWidth("300px");
		confirmEmailAddress.addValidator(new EmailValidator("Enter your email address."));
		confirmEmailAddress.setRequired(true);
		confirmEmailAddress.setNullRepresentation("");

		createButton = new Button("Create", this);
		createButton.setClickShortcut(KeyCode.ENTER);
		createButton.addStyleName("default");

		// Add both to a panel
		Label label = new Label("<H1>Start by creating an account to login to Scoutmaster.</H1>");
		label.setContentMode(ContentMode.HTML);

		VerticalLayout fields = new VerticalLayout(label, user, password, confirmPassword);
		fields.addComponent(createButton);
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, false));
		fields.setSizeUndefined();

		viewLayout = new VerticalLayout(fields);
		viewLayout.setSizeFull();
		viewLayout.setMargin(true);

	}

	@Override
	public String getCaption()
	{
		return "Create Account";
	}

	@Override
	public Component getContent()
	{

		// focus the username field when user arrives to the login view
		user.focus();

		return viewLayout;
	}

	@Override
	public void buttonClick(ClickEvent event)
	{
		if (user.isValid() && password.isValid() && confirmPassword.isValid())
		{
			String username = user.getValue();
			String password = this.password.getValue();
			String confirmPassword = this.confirmPassword.getValue();
			String emailAddress = this.emailAddress.getValue();
			String confirmEmailAddress = this.confirmEmailAddress.getValue();

			UserDao daoUser = new DaoFactory().getUserDao();
			if (daoUser.findByName(username) != null)
			{
				// This should never happen on a first time setup!
				Notification.show("The user " + username + " already exists. Choose another one.");
			}
			else
			{
				if (confirmPassword.equals(password))
				{
					if (confirmEmailAddress.equals(emailAddress))
					{

						User user = new User(username, password);
						user.setEmailAddress(emailAddress);
						daoUser.persist(user);

						// Store the current user in the service session
						UI.getCurrent().getSession().setAttribute("user", user);
						this.createButton.setVisible(false);
						Notification.show("Your user account has been created.", Type.TRAY_NOTIFICATION);
						created = true;
					}
					else
					{
						// Non matching email addresses clear the password field
						// and refocuses it
						this.confirmEmailAddress.setValue(null);
						this.confirmEmailAddress.setValue(null);
						this.confirmEmailAddress.focus();
						Notification.show("The email and confirm email fields do not match.");
					}

				}
				else
				{

					// Non matching password field
					this.password.setValue(null);
					this.confirmPassword.setValue(null);
					this.password.focus();
					Notification.show("The password and confirm password fields do not match.");

				}
			}
		}
		else
		{
			// user.setComponentError(new UserError("I dont like you"));
			Notification.show("Invalid username or password");
		}
	}

	@Override
	public boolean onAdvance()
	{
		return created;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

}
