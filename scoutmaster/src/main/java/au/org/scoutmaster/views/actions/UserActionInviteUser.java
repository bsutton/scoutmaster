package au.org.scoutmaster.views.actions;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.server.VaadinServletService;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.CrudAction;
import au.com.vaadinutils.editors.InputDialog;
import au.com.vaadinutils.editors.Recipient;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.ForgottenPasswordResetDao;
import au.org.scoutmaster.dao.SMTPSettingsDao;
import au.org.scoutmaster.dao.access.UserDao;
import au.org.scoutmaster.domain.ForgottenPasswordReset;
import au.org.scoutmaster.domain.Group;
import au.org.scoutmaster.domain.SMTPServerSetting;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.util.RandomString;
import au.org.scoutmaster.util.SMNotification;
import au.org.scoutmaster.views.ResetPasswordView;

public class UserActionInviteUser implements CrudAction<User>
{
	private static final long serialVersionUID = 1L;
	Logger logger = LogManager.getLogger(UserActionInviteUser.class);

	@Override
	public boolean isDefault()
	{
		return false;
	}

	@Override
	public void exec(final BaseCrudView<User> crud, final EntityItem<User> entity)
	{
		final InputDialog dialog = new InputDialog(UI.getCurrent(), "Invite User",
				"Enter user's email address to invite them:", new Recipient()
				{

					@Override
					public boolean onOK(final String emailAddress)
					{
						new InputDialog(UI.getCurrent(), "Select Username",
								"Enter the Username to assign this new user.", new Recipient()
						{

							@Override
							public boolean onOK(final String username)
							{
								// Email the user a token.
								inviteUser(username, emailAddress);
								return true;
							}

							@Override
							public boolean onCancel()
							{
								return true;
							}

						});
						return true;

					}

					private void inviteUser(final String username, final String emailAddress)
					{
						final SMTPSettingsDao settingsDao = new DaoFactory().getSMTPSettingsDao();
						final SMTPServerSetting settings = settingsDao.findSettings();

						// first check that the username doesn't already exists

						final UserDao daoUser = new DaoFactory().getDao(UserDao.class);
						if (daoUser.findByName(username) != null)
						{
							SMNotification.show("The selected username already exists. Please select a new one",
									Type.ERROR_MESSAGE);
						}
						else
						{

							// Create the new user account.
							final User user = new User(username,
									new RandomString(RandomString.Type.ALPHANUMERIC, 32).nextString());
							user.setEmailAddress(emailAddress);
							user.setGroup(SMSession.INSTANCE.getGroup());
							daoUser.persist(user);

							// Now notify the user.
							final Email email = new SimpleEmail();
							email.setHostName(settings.getSmtpFQDN());
							email.setSmtpPort(settings.getSmtpPort());
							email.setSSLCheckServerIdentity(false);
							if (settings.isAuthRequired())
							{
								email.setAuthenticator(
										new DefaultAuthenticator(settings.getUsername(), settings.getPassword()));
							}
							email.setSSLOnConnect(true);
							try
							{
								email.setFrom(settings.getFromEmailAddress());
								email.setSubject("[Scoutmaster] Invitation to login");

								final ForgottenPasswordResetDao forgottenPasswordResetDao = new DaoFactory()
										.getForgottenPasswordResetDao();
								final ForgottenPasswordReset reset = forgottenPasswordResetDao
										.createReset(emailAddress);

								forgottenPasswordResetDao.persist(reset);

								final StringBuffer url = VaadinServletService.getCurrentServletRequest()
										.getRequestURL();

								final User loggedInUser = SMSession.INSTANCE.getLoggedInUser();
								final Group scoutGroup = SMSession.INSTANCE.getGroup();

								final StringBuilder sb = new StringBuilder();
								sb.append("You have been invited by " + loggedInUser.getFullname()
										+ " to join the Scoutmaster Group management system for " + scoutGroup.getName()
										+ ".\n\n");
								sb.append(
										"Use the following link within the next 24 hours to activate your account.\n\n");

								sb.append(
										url + "/" + ResetPasswordView.NAME + "?resetid=" + reset.getResetid() + "\n\n");
								sb.append("You have been allocated the username '" + user.getUsername()
										+ "' which you will need to use each time you login.");
								email.setMsg(sb.toString());
								email.addTo(emailAddress);
								email.send();
							}
							catch (final IllegalArgumentException e)
							{
								SMNotification.show(e, Type.ERROR_MESSAGE);
							}
							catch (final EmailException e)
							{
								UserActionInviteUser.this.logger.error(e, e);
								SMNotification.show(e, Type.ERROR_MESSAGE);
							}
						}

					}

					@Override
					public boolean onCancel()
					{
						return true;
					}
				});
		dialog.addValidator(new EmailValidator("You must enter a valid email address."));
	}

	@Override
	public String toString()
	{
		return "Invite User";
	}

	@Override
	public boolean showPreparingDialog()
	{
		return false;
	}

}
