package au.org.scoutmaster.views.wizards.groupsetup;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;
import org.xml.sax.SAXException;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.access.UserDao;
import au.org.scoutmaster.domain.Address;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Group;
import au.org.scoutmaster.domain.GroupRole;
import au.org.scoutmaster.domain.GroupRole_;
import au.org.scoutmaster.domain.PreferredEmail;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.ui.SimpleFormLayout;
import au.org.scoutmaster.util.SMNotification;
import au.org.scoutmaster.validator.PasswordValidator;
import au.org.scoutmaster.validator.UsernameValidator;
import au.org.scoutmaster.xml.GroupSetup;

public class NewAccountStep implements WizardStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(NewAccountStep.class);

	private TextField username;

	private PasswordField password;

	private PasswordField confirmPassword;

	private TextField emailAddress;

	private TextField confirmEmailAddress;

	private SimpleFormLayout form;

	private TextField firstname;

	private TextField lastname;

	private ComboBox groupRoleField;

	private GroupSetupWizardView wizard;

	public NewAccountStep(final GroupSetupWizardView setupWizardView)
	{
		this.wizard = setupWizardView;
	}

	@Override
	public String getCaption()
	{
		return "Create Account";
	}

	@Override
	public Component getContent()
	{
		// ensure that we always reload the GroupSetup as the Group type may
		// have changed.
		try
		{
			this.wizard.getGroupDetailStep().getGroupSetup();
		}
		catch (IOException | SAXException e)
		{
			SMNotification.show(e, Type.ERROR_MESSAGE);
		}
		if (form == null)
		{
			form = new SimpleFormLayout();
			form.setWidth("100%");
			form.setMargin(true);
			form.setLabelWidth(200);

			final Label label = new Label("<h1>Start by creating an account so you can login to Scoutmaster.</h1>");
			label.setContentMode(ContentMode.HTML);
			form.addComponent(label);

			this.username = new TextField("Username");
			form.addComponent(this.username);
			this.username.setInputPrompt("Enter a username");
			this.username.addValidator(new UsernameValidator());
			this.username.setRequired(true);
			this.username.setNullRepresentation("");
			this.username.setImmediate(true);
			this.username.addValidator(value -> {
				// tell the user if their username is unique.
				UserDao userDao = new DaoFactory().getUserDao();
				String usernameString = ((String) value).trim();
				if (usernameString.length() > 0)
				{
					if (userDao.findByName(usernameString) != null)
						throw new Validator.InvalidValueException("Username already exists. Please choose another.");
				}
			});

			this.password = new PasswordField("Password:");
			form.addComponent(this.password);
			this.password.setDescription(PasswordValidator.validationRule);
			this.password.addValidator(new PasswordValidator("Password"));
			this.password.setRequired(true);
			this.password.setValue("");
			this.password.setImmediate(true);

			this.confirmPassword = new PasswordField("Confirm Password:");
			form.addComponent(this.confirmPassword);
			this.confirmPassword.addValidator(new PasswordValidator("Confirm Password", this.password));
			this.confirmPassword.setRequired(true);
			this.confirmPassword.setDescription("Confirm your password");

			StringBuilder emailPrivacy = new StringBuilder();
			emailPrivacy.append("<p>Scoutmaster needs your email address for the following reasons:</p>");
			emailPrivacy.append("<ul>");
			emailPrivacy
					.append("<li>If you forget your password, we use your email address to reset your password</li>");
			emailPrivacy.append("<li>Notifications of outages or planned maintenance of Scoutmaster.</li>");
			emailPrivacy.append("<li>Notifications of updates to Scoutmaster (i.e. new releases).</li>");
			emailPrivacy.append(
					"<li>System notifications. e.g. Scoutmaster wants to remind you that you haven't completed a task you created.</li>");
			emailPrivacy.append(
					"<li>Invitations to help with Scoutmaster development (e.g. participate in testing of new versions of Scoutmaster)");
			emailPrivacy.append("<li>Ask your permission to change the terms of what we use your email address for.");
			emailPrivacy.append(
					"<p>The last option is included in case we have forgotten some critical form of communication required to run scoutmaster</p>");
			emailPrivacy.append("<p>Scoutmaster will use your email address for no other purposes.</p>");
			emailPrivacy.append(
					"<p>Scoutmaster is a NOT FOR PROFIT. We don't advertise and we don't sell/share your information with any third party.</p>");
			emailPrivacy.append("</ul>");

			Label privacy = new Label(emailPrivacy.toString());
			privacy.setContentMode(ContentMode.HTML);
			form.addComponent(privacy);
			// form.addComponent(new Label("Email Address:"));
			this.emailAddress = new TextField("Email Address:");
			form.addComponent(this.emailAddress);
			this.emailAddress.setValue("");
			// this.emailAddress.setWidth("400");

			this.emailAddress.addValidator(new EmailValidator("The entered email address is not valid."));
			this.emailAddress.setInputPrompt("Enter your email address.");
			this.emailAddress.setRequired(true);
			this.emailAddress.setNullRepresentation("");
			this.emailAddress.setImmediate(true);

			// form.addComponent(new Label("Confirm Email Address:"));

			this.confirmEmailAddress = new TextField("Confirm Email Address:");
			form.addComponent(this.confirmEmailAddress);

			this.confirmEmailAddress.addValidator(new EmailValidator("The entered email address is not valid."));
			this.confirmEmailAddress.setRequired(true);
			this.confirmEmailAddress.setNullRepresentation("");
			this.confirmEmailAddress.setInputPrompt("Confirm your email address.");
			this.confirmEmailAddress.setImmediate(true);
			this.confirmEmailAddress.addValidator(value -> {
				if (!this.confirmEmailAddress.getValue().equals(this.emailAddress.getValue()))
					throw new Validator.InvalidValueException(
							"The entered Email Address and Confirm Email Address are not identical.");
			});

			this.firstname = new TextField("Firstname");
			form.addComponent(firstname);
			this.firstname.setRequired(true);

			this.lastname = new TextField("Lastname");
			form.addComponent(lastname);
			this.lastname.setRequired(true);

			this.groupRoleField = new ComboBox("Which Role best describes your role in the group.");
			form.addComponent(groupRoleField);
			this.groupRoleField.setRequired(true);

			// focus the username field when user arrives to the login view
			this.username.focus();
		}

		loadGroupRoles();

		return form;

	}

	private void loadGroupRoles()
	{
		GroupSetup group;
		try
		{
			group = this.wizard.getGroupDetailStep().getGroupSetup();

			for (GroupRole role : group.getGroupRoles())
			{

				groupRoleField.addItem(role);
			}
		}
		catch (IOException | SAXException e)
		{
			// Nothing we can do about this so let it go through to the keeper.
			throw new RuntimeException(e);
		}

	}

	@Override
	public boolean onAdvance()
	{
		boolean valid = form.validate();
		if (!valid)
			SMNotification.show("Please fix the form errors", Type.ERROR_MESSAGE);
		return valid;
	}

	@Override
	public boolean onBack()
	{
		// TODO Auto-generated method stub
		return true;
	}

	public void createUser(Group group)
	{
		// So looks like they are going to register so lets create the user.

		JpaBaseDao<User, Long> dao = DaoFactory.getGenericDao(User.class);

		User user = new User();
		user.setUsername(this.username.getValue());
		user.setFirstname(this.firstname.getValue());
		user.setLastname(this.lastname.getValue());
		user.setPassword(this.password.getValue());
		user.setGroup(group);

		dao.persist(user);
		dao.flush();
		SMSession.INSTANCE.setLoggedInUser(user);

		// We also create a Contact for this user.
		JpaBaseDao<Contact, Long> daoContact = DaoFactory.getGenericDao(Contact.class);
		JpaBaseDao<GroupRole, Long> daoGroupRole = DaoFactory.getGenericDao(GroupRole.class);
		JpaBaseDao<Address, Long> daoAddress = DaoFactory.getGenericDao(Address.class);

		Contact contact = new Contact();
		contact.setFirstname(this.firstname.getValue());
		contact.setLastname(this.lastname.getValue());
		GroupRole groupRole = (GroupRole) groupRoleField.getValue();
		// The GroupRole stored in the groupRoleField is is a detached state so
		// we need to get the real group role.
		groupRole = daoGroupRole.findOneByAttribute(GroupRole_.name, groupRole.getName());

		contact.setRole(groupRole);
		contact.setHomeEmail(this.emailAddress.getValue());
		contact.setPreferredEmail(PreferredEmail.HOME);

		Address empty = new Address();
		daoAddress.persist(empty);
		contact.setAddress(empty);
		//
		// daoSectionType.findOneByAttribute(SectionType_.name, "");
		// SectionType section;
		//
		daoContact.persist(contact);
		daoContact.flush();

	}

}
