package au.org.scoutmaster.views.wizards.groupmaintenance;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.crud.splitFields.SplitLabel;
import au.com.vaadinutils.ui.SingleEntityWizardStep;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.security.User;
import au.org.scoutmaster.validator.PasswordValidator;
import au.org.scoutmaster.validator.UsernameValidator;

public class NewAccountStep extends SingleEntityWizardStep<User> implements WizardStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(NewAccountStep.class);

	private TextField username;

	private PasswordField password;

	private PasswordField confirmPassword;

	private TextField emailAddress;

	private TextField confirmEmailAddress;

	private VerticalLayout layout;

	public NewAccountStep(final GroupMaintenanceWizardView setupWizardView)
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
		if (layout == null)
		{
			layout = new VerticalLayout();
			layout.setWidth("100%");
			layout.setMargin(true);

			final Label label = new SplitLabel("<h1>Start by creating an account to login to Scoutmaster.</h1>");
			label.setContentMode(ContentMode.HTML);
			layout.addComponent(label);

			VerticalLayout vertical = new VerticalLayout();
			vertical.setMargin(true);
			vertical.setWidth("600");
			layout.addComponent(vertical);

			// final SMMultiColumnFormLayout<User> formLayout = new
			// SMMultiColumnFormLayout<>(2, fieldGroup);
			// formLayout.setWidth("600px");
			// formLayout.setColumnFieldWidth(0, 400);
			// formLayout.setColumnFieldWidth(1, 20);
			//
			// formLayout.colspan(2);

			// formLayout.bindLabel(label);

			vertical.addComponent(new Label("Username:"));

			// this.username = formLayout.bindTextField("Username:",
			// "username");

			this.username = new TextField();
			fieldGroup.bind(this.username, "username");
			vertical.addComponent(this.username);
			this.username.setInputPrompt("Enter a username");
			this.username.addValidator(new UsernameValidator());
			this.username.setRequired(true);
			this.username.setNullRepresentation("");
			this.username.setImmediate(true);

			// Create the password input field
			// this.password = formLayout.bindPasswordField("Password:",
			// "password");

			vertical.addComponent(new Label("Password:"));

			// this.username = formLayout.bindTextField("Username:",
			// "username");

			this.password = new PasswordField();
			vertical.addComponent(this.password);
			// fieldGroup.bind(this.password, "password");
			this.password.setDescription(PasswordValidator.validationRule);
			this.password.addValidator(new PasswordValidator("Password"));
			this.password.setRequired(true);
			this.password.setValue("");
			this.password.setImmediate(true);

			// Create the confirm password input field but we don't bind it.
			// this.confirmPassword = formLayout.addPasswordField("Confirm
			// Password:");

			vertical.addComponent(new Label("Confirm Password:"));

			this.confirmPassword = new PasswordField();
			vertical.addComponent(this.confirmPassword);

			this.confirmPassword.addValidator(new PasswordValidator("Confirm Password"));
			this.confirmPassword.setRequired(true);
			this.confirmPassword.setDescription("Confirm your password");

			// Create the email address input field
			// this.emailAddress = formLayout.bindTextField("Email Address:",
			// "emailAddress");

			vertical.addComponent(new Label("Email Address:"));

			this.emailAddress = new TextField();
			vertical.addComponent(this.emailAddress);
			fieldGroup.bind(this.emailAddress, "emailAddress");
			this.emailAddress.setValue("");
			this.emailAddress.setWidth("400");

			this.emailAddress.addValidator(new EmailValidator("Enter your email address."));
			this.emailAddress.setInputPrompt("Enter your email address.");
			this.emailAddress.setRequired(true);
			this.emailAddress.setNullRepresentation("");
			this.emailAddress.setImmediate(true);

			// Create the confirmEmail input field
			// this.confirmEmailAddress = formLayout.addTextField("Confirm Email
			// Address:");

			vertical.addComponent(new Label("Confirm Email Address:"));

			this.confirmEmailAddress = new TextField();
			vertical.addComponent(this.confirmEmailAddress);

			this.confirmEmailAddress.addValidator(new EmailValidator("Enter your email address."));
			this.confirmEmailAddress.setRequired(true);
			this.confirmEmailAddress.setNullRepresentation("");
			this.confirmEmailAddress.setInputPrompt("Confirm your email address.");
			this.confirmEmailAddress.setImmediate(true);

			this.confirmEmailAddress.setWidth("400");

			// focus the username field when user arrives to the login view
			this.username.focus();
		}

		return layout;
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
				// Non matching email addresses
				// and refocuses it
				this.confirmEmailAddress.focus();
				Notification.show("The email and confirm email fields do not match.");
			}
		}
		else
		{
			// Non matching password field
			this.password.setValue("");
			this.confirmPassword.setValue("");
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
