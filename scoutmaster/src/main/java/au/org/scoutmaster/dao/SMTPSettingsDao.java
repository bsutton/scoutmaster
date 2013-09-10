package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import au.org.scoutmaster.domain.SMTPServerSettings;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class SMTPSettingsDao extends JpaBaseDao<SMTPServerSettings, Long> implements
		Dao<SMTPServerSettings, Long>
{
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(SMTPSettingsDao.class);

	public SMTPSettingsDao()
	{
		// inherit the default per request em.
	}

	public SMTPSettingsDao(EntityManager em)
	{
		super(em);
	}

	@Override
	public List<SMTPServerSettings> findAll()
	{
		return super.findAll(SMTPServerSettings.FIND_ALL);
	}

	public SMTPServerSettings findSettings()
	{
		SMTPServerSettings settings = null;
		List<SMTPServerSettings> list = findAll();

		if (list.size() > 1)
			throw new IllegalStateException("Found more than 1 EMailServerSetting");
		else if (list.size() == 1)
			settings = list.get(0);
		else if (list.size() == 0)
		{
			// If not initialised before then do it now.
			settings = new SMTPServerSettings();
			settings.setAuthRequired(false);
			settings.setSmtpPort(25);
			settings.setSmtpFQDN("localhost");
			this.persist(settings);
		}

		return settings;
	}

	@Override
	public JPAContainer<SMTPServerSettings> makeJPAContainer()
	{
		return super.makeJPAContainer(SMTPServerSettings.class);
	}

	/**
	 * Simple method to send a single email using the EMailServerSettings.
	 * 
	 * @param settings
	 * @param fromAddress
	 * @param toAddress
	 * @param subject
	 * @param body
	 * @param string 
	 * @throws EmailException
	 */
	public void sendEmail(SMTPServerSettings settings, String fromAddress, String toAddress, String ccAddress, String subject,
			String body) throws EmailException
	{
		HtmlEmail email = new HtmlEmail();
	
		email.setDebug(true);
		email.setHostName(settings.getSmtpFQDN());
		email.setSmtpPort(settings.getSmtpPort());
		if (settings.isAuthRequired())
			email.setAuthentication(settings.getUsername(), settings.getPassword());
		if (settings.getUseSSL())
		{
			email.setSslSmtpPort(settings.getSmtpPort().toString());
			email.setSSLOnConnect(true);
			email.setSSLCheckServerIdentity(false);
		}
		email.setFrom(fromAddress);
		email.setBounceAddress(settings.getBounceEmailAddress());
		email.addTo(toAddress);
		if (ccAddress != null && ccAddress.length() > 0)
			email.addCc(ccAddress);
		email.setSubject(subject);
		email.setHtmlMsg(body);
		email.setTextMsg("Your email client does not support HTML messages");
		email.send();

	}

}
