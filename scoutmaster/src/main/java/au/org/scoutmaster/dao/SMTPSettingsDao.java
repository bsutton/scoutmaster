package au.org.scoutmaster.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.SMTPServerSetting;
import au.org.scoutmaster.forms.EmailAddressType;

public class SMTPSettingsDao extends JpaBaseDao<SMTPServerSetting, Long> implements Dao<SMTPServerSetting, Long>
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(SMTPSettingsDao.class);

	/**
	 * Helper class to store an emailAddress and the Address Type (To, BCC, CC).
	 *
	 * @author bsutton
	 *
	 */
	static public class EmailTarget
	{

		private final String emailAddress;
		private final EmailAddressType type;

		public EmailTarget(final EmailAddressType type, final String emailAddress)
		{
			this.type = type;
			this.emailAddress = emailAddress;
		}
	}

	public SMTPSettingsDao()
	{
		// inherit the default per request em.
	}

	public SMTPServerSetting findSettings()
	{
		SMTPServerSetting settings = null;
		final List<SMTPServerSetting> list = findAll();

		if (list.size() > 1)
		{
			throw new IllegalStateException("Found more than 1 EMailServerSetting");
		}
		else if (list.size() == 1)
		{
			settings = list.get(0);
		}
		else if (list.size() == 0)
		{
			// If not initialised before then do it now.
			settings = new SMTPServerSetting();
			settings.setAuthRequired(false);
			settings.setSmtpPort(25);
			settings.setSmtpFQDN("localhost");
			persist(settings);
		}

		return settings;
	}

	@Override
	public JPAContainer<SMTPServerSetting> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

	public void sendEmail(final SMTPServerSetting settings, final String fromAddress, final String bounceEmailAddress,
			final EmailTarget target, final String subject, final String body,
			final HashSet<? extends DataSource> attachedFiles) throws EmailException
	{
		final ArrayList<EmailTarget> list = new ArrayList<>();
		list.add(target);

		sendEmail(settings, fromAddress, bounceEmailAddress, list, subject, body, attachedFiles);

	}

	public void sendEmail(SMTPServerSetting settings, String fromEmailAddress, String bounceEmailAddress,
			EmailTarget emailTarget, String subject, String body) throws EmailException
	{
		sendEmail(settings, fromEmailAddress, bounceEmailAddress, emailTarget, subject, body,
				new HashSet<DataSource>());

	}

	public void sendEmail(SMTPServerSetting settings, String fromAddress, String bounceEmailAddress, EmailTarget target,
			String subject, String bodyText, ByteArrayDataSource byteArrayDataSource)
	{
		final HashSet<DataSource> attachedFiles = new HashSet<>();
		attachedFiles.add(byteArrayDataSource);

		sendEmail(settings, fromAddress, bounceEmailAddress, target, subject, bodyText, byteArrayDataSource);

	}

	/**
	 * Simple method to send a single email using the EMailServerSettings.
	 *
	 * @param settings
	 * @param fromAddress
	 * @param firstAddress
	 * @param object2
	 * @param object
	 * @param subject
	 * @param body
	 * @param attachedFiles
	 * @param string
	 * @throws EmailException
	 */
	public void sendEmail(final SMTPServerSetting settings, final String fromAddress, String bounceEmailAddress,
			final List<EmailTarget> targets, final String subject, final String body,
			final HashSet<? extends DataSource> attachedFiles) throws EmailException
	{
		final HtmlEmail email = new HtmlEmail();

		email.setDebug(true);
		email.setHostName(settings.getSmtpFQDN());
		email.setSmtpPort(settings.getSmtpPort());
		email.setSSLCheckServerIdentity(false);
		if (settings.isAuthRequired())
		{
			email.setAuthentication(settings.getUsername(), settings.getPassword());
		}
		if (settings.getUseSSL())
		{
			email.setSslSmtpPort(settings.getSmtpPort().toString());
			email.setSSLOnConnect(true);
			email.setSSLCheckServerIdentity(false);
		}
		email.setFrom(fromAddress);
		email.setBounceAddress(bounceEmailAddress);

		for (final EmailTarget target : targets)
		{
			addEmailAddress(email, target.emailAddress, target.type);
		}

		email.setSubject(subject);
		email.setHtmlMsg(body);
		email.setTextMsg("Your email client does not support HTML messages");
		if (attachedFiles != null)
		{
			for (final DataSource attachedFile : attachedFiles)
			{
				email.attach(attachedFile, attachedFile.getName(), attachedFile.getContentType());
			}
		}

		email.send();

	}

	private void addEmailAddress(final HtmlEmail email, final String firstAddress, final EmailAddressType firstType)
			throws EmailException
	{
		switch (firstType)
		{
			case To:
				email.addTo(firstAddress);
				break;
			case BCC:
				email.addBcc(firstAddress);
				break;
			case CC:
				email.addCc(firstAddress);
				break;
		}
	}

}
