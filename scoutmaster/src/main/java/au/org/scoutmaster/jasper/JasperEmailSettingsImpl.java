package au.org.scoutmaster.jasper;

import au.com.vaadinutils.jasper.JasperEmailSettings;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMTPSettingsDao;
import au.org.scoutmaster.domain.SMTPServerSettings;

public class JasperEmailSettingsImpl implements JasperEmailSettings
{
	private final SMTPServerSettings smtpSettings;

	public JasperEmailSettingsImpl()
	{
		final SMTPSettingsDao stmpDao = new DaoFactory().getSMTPSettingsDao();
		this.smtpSettings = stmpDao.findSettings();
	}

	@Override
	public String getSmtpFQDN()
	{
		return this.smtpSettings.getSmtpFQDN();
	}

	@Override
	public Integer getSmtpPort()
	{
		return this.smtpSettings.getSmtpPort();
	}

	@Override
	public boolean isAuthRequired()
	{
		return this.smtpSettings.isAuthRequired();
	}

	@Override
	public String getUsername()
	{
		return this.smtpSettings.getUsername();
	}

	@Override
	public String getPassword()
	{
		return this.smtpSettings.getPassword();
	}

	@Override
	public boolean getUseSSL()
	{
		return this.smtpSettings.getUseSSL();
	}

	@Override
	public String getBounceEmailAddress()
	{
		return this.smtpSettings.getBounceEmailAddress();
	}

}
