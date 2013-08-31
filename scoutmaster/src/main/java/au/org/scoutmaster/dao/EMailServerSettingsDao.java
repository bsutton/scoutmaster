package au.org.scoutmaster.dao;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import au.org.scoutmaster.domain.EMailServerSettings;

import com.sun.mail.util.MailSSLSocketFactory;
import com.vaadin.addon.jpacontainer.JPAContainer;

public class EMailServerSettingsDao extends JpaBaseDao<EMailServerSettings, Long> implements
		Dao<EMailServerSettings, Long>
{
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(EMailServerSettingsDao.class);

	public EMailServerSettingsDao()
	{
		// inherit the default per request em.
	}

	public EMailServerSettingsDao(EntityManager em)
	{
		super(em);
	}

	@Override
	public List<EMailServerSettings> findAll()
	{
		return super.findAll(EMailServerSettings.FIND_ALL);
	}

	public EMailServerSettings findSettings()
	{
		EMailServerSettings settings = null;
		List<EMailServerSettings> list = findAll();

		if (list.size() > 1)
			throw new IllegalStateException("Found more than 1 EMailServerSetting");
		else if (list.size() == 1)
			settings = list.get(0);
		else if (list.size() == 0)
		{
			// If not initialised before then do it now.
			settings = new EMailServerSettings();
			settings.setAuthRequired(false);
			settings.setSmtpPort(25);
			settings.setSmtpFQDN("localhost");
			this.persist(settings);
		}

		return settings;
	}

	@Override
	public JPAContainer<EMailServerSettings> makeJPAContainer()
	{
		return super.makeJPAContainer(EMailServerSettings.class);
	}

	/**
	 * Simple method to send a single email using the EMailServerSettings.
	 * 
	 * @param settings
	 * @param fromAddress
	 * @param toAddress
	 * @param subject
	 * @param body
	 * @throws EmailException
	 */
	public void sendEmail(EMailServerSettings settings, String fromAddress, String toAddress, String subject,
			String body) throws EmailException
	{
		Email email = new SimpleEmail();
	
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
		email.setSubject(subject);
		email.setMsg(body);
		
		email.send();

	}

}
