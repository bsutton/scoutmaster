package au.org.scoutmaster.views.wizards.setup;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.ui.SingleEntityWizardStep;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;
import au.org.scoutmaster.validator.PasswordValidator;
import au.org.scoutmaster.validator.UsernameValidator;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

public class NewAccountStep extends SingleEntityWizardStep<User> implements WizardStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(NewAccountStep.class);

	private TextField username;

	private PasswordField password;

	private PasswordField confirmPassword;

	private TextField emailAddress;

	private TextField confirmEmailAddress;

	public NewAccountStep(final GroupSetupWizardView setupWizardView)
	{
		super(new DaoFactory().getUserDao(), User.class);
	}

	@Override
	public String getCaption()
	{
		return "Create Account";
	}

	@Override
	protected Component getContent(final ValidatingFieldGroup<User> fieldGroup)
	{
		final SMMultiColumnFormLayout<User> formLayout = new SMMultiColumnFormLayout<>(2, fieldGroup);
		formLayout.setWidth("600px");
		formLayout.setColumnFieldWidth(0, 400);
		formLayout.setColumnFieldWidth(1, 20);

		formLayout.colspan(2);
		final Label label = new Label("<h1>Start by creating an account to login to Scoutmaster.</h1>");
		label.setContentMode(ContentMode.HTML);
		formLayout.bindLabel(label);

		this.username = formLayout.bindTextField("Username:", "username");
		this.username.setInputPrompt("Enter a username");
		this.username.addValidator(new UsernameValidator());
		this.username.setRequired(true);

		// Create the password input field
		this.password = formLayout.bindPasswordField("Password:", "password");
		this.password
				.setDescription("Enter a password  containing at least 2 digits and 2 non alphanumeric characters.");
		this.password.addValidator(new PasswordValidator("Password"));
		this.password.setRequired(true);

		// Create the confirm password input field but we don't bind it.
		this.confirmPassword = formLayout.addPasswordField("Confirm Password:");
		this.confirmPassword.addValidator(new PasswordValidator("Confirm Password"));
		this.confirmPassword.setRequired(true);

		// Create the email address input field
		this.emailAddress = formLayout.bindTextField("Email Address:", "emailAddress");
		this.emailAddress.addValidator(new EmailValidator("Enter your email address."));
		this.emailAddress.setRequired(true);
		this.emailAddress.setNullRepresentation("");

		// Create the confirmEmail input field
		this.confirmEmailAddress = formLayout.addTextField("Confirm Email Address:");
		this.confirmEmailAddress.addValidator(new EmailValidator("Enter your email address."));
		this.confirmEmailAddress.setRequired(true);
		this.confirmEmailAddress.setNullRepresentation("");

		// focus the username field when user arrives to the login view
		this.username.focus();

		return formLayout;
	}

	@Override
	public boolean validate()
	{
		boolean valid = false;
		if (this.confirmPassword.getValue().equals(this.password.getValue()))
		{
			if (this.confirmEmailAddress.getValue().equals(this.emailAddress.getValue()))
			{
				valid = super.validate();
			}
			else
			{
				// Non matching email addresses clear the password field
				// and refocuses it
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
		return valid;
	}

	@Override
	protected void initEntity(final User entity)
	{
		// No Op

	}

	@Override
	protected User findEntity()
	{
		User user = null;
		final List<User> users = new DaoFactory().getUserDao().findAll();
		if (users.size() > 1)
		{
			throw new IllegalStateException(
					"More than one user has been found which is not valid during initial setup.");
		}
		if (users.size() == 1)
		{
			user = users.get(0);
		}
		return user;
	}

}
