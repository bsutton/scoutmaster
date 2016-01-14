package au.org.scoutmaster.application;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

import javax.mail.util.ByteArrayDataSource;

import au.com.vaadinutils.dao.EntityManagerThread;
import au.com.vaadinutils.errorHandling.ErrorSettings;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMTPSettingsDao;
import au.org.scoutmaster.domain.Group;
import au.org.scoutmaster.domain.SMTPServerSetting;
import au.org.scoutmaster.forms.EmailAddressType;

final class ErrorString implements ErrorSettings
{

	private NavigatorUI navigator;

	public ErrorString(NavigatorUI navigatorUI)
	{
		navigator = navigatorUI;
	}

	@Override
	public void sendEmail(String emailAddress, String subject, String bodyText, ByteArrayOutputStream attachment,
			String filename, String MIMEType)
	{
		new EntityManagerThread<Void>(new Callable<Void>()
		{

			@Override
			public Void call() throws Exception
			{
				final SMTPSettingsDao daoSMTPSettings = new DaoFactory().getSMTPSettingsDao();
				final SMTPServerSetting settings = daoSMTPSettings.findSettings();

				if (attachment != null)
				{

					daoSMTPSettings.sendEmail(settings, "error@scoutmaster.org.au",
							SMSession.INSTANCE.getLoggedInUser().getEmailAddress(),
							new SMTPSettingsDao.EmailTarget(EmailAddressType.To, emailAddress), subject, bodyText,
							new ByteArrayDataSource(attachment.toByteArray(), MIMEType));
				}
				else
				{
					daoSMTPSettings.sendEmail(settings, "error@scoutmaster.org.au",
							SMSession.INSTANCE.getLoggedInUser().getEmailAddress(),
							new SMTPSettingsDao.EmailTarget(EmailAddressType.To, emailAddress), subject, bodyText);

				}

				return null;
			}

		});

	}

	@Override
	public String getViewName()
	{
		return navigator.getCurrentView().getClass().getName();
	}

	@Override
	public String getUserName()
	{
		if (SMSession.INSTANCE.getLoggedInUser() != null)
			return SMSession.INSTANCE.getLoggedInUser().getFullname();
		else
			return "No user logged in";
	}

	@Override
	public String getUserEmail()
	{
		if (SMSession.INSTANCE.getLoggedInUser() != null)
			return SMSession.INSTANCE.getLoggedInUser().getEmailAddress();
		else
			return "errors@scoutmaster.org.au";
	}

	@Override
	public String getTargetEmailAddress()
	{
		final Group group = SMSession.INSTANCE.getGroup();
		String email = (group != null && group.getPrimaryContact() != null ? group.getPrimaryContact().getEmail()
				: null);
		if (email == null)
			email = "bsutton" + "@" + "noojee.com.au";

		return email;
	}

	@Override
	public String getSystemName()
	{
		try
		{
			return InetAddress.getLocalHost().getHostName();
		}
		catch (UnknownHostException e)
		{
			return "Unknown hostname";
		}
	}

	@Override
	public String getSupportCompanyName()
	{
		final Group group = SMSession.INSTANCE.getGroup();

		return (group != null ? group.getName() : "Scoutmaster System");
	}

	@Override
	public String getBuildVersion()
	{

		return "Unknown";
	}
}