package au.org.scoutmaster.ui;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

import au.com.vaadinutils.dao.RunnableUI;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMTPSettingsDao;
import au.org.scoutmaster.domain.SMTPServerSetting;
import au.org.scoutmaster.util.SMNotification;

public class EmailWorker extends RunnableUI
{
	private Logger logger = LogManager.getLogger();

	private SendEmailWorkingDialog dialog;

	public EmailWorker(UI ui, SendEmailWorkingDialog dialog)
	{
		super(ui);
		this.dialog = dialog;
	}

	@Override
	public void run(UI ui)
	{

		final SMTPSettingsDao settingsDao = new DaoFactory().getSMTPSettingsDao();
		final SMTPServerSetting settings = settingsDao.findSettings();

		final Email email = new SimpleEmail();
		email.setHostName(settings.getSmtpFQDN());
		email.setSmtpPort(settings.getSmtpPort());
		email.setSSLCheckServerIdentity(false);
		if (settings.isAuthRequired())
		{
			email.setAuthenticator(new DefaultAuthenticator(settings.getUsername(), settings.getPassword()));
		}
		email.setSSLOnConnect(true);
		try
		{
			email.setFrom(dialog.getFromEmailAddress());
			email.setSubject(dialog.getSubject());
			email.setMsg(dialog.getMessage());
			email.addTo(dialog.getToEmailAddresses().toArray(new String[]
			{}));
			email.send();
		}
		catch (final EmailException e)
		{
			logger.error(e, e);
			SMNotification.show(e, Type.ERROR_MESSAGE);
		}
	}
}
