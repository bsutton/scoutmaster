package au.org.scoutmaster.jasper;

import au.com.vaadinutils.jasper.JasperEmailSettings;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMTPSettingsDao;
import au.org.scoutmaster.domain.SMTPServerSettings;

public class JasperEmailSettingsImpl implements JasperEmailSettings
{
	private SMTPServerSettings smtpSettings;

	public JasperEmailSettingsImpl()
	{
		SMTPSettingsDao stmpDao = new DaoFactory().getSMTPSettingsDao();
		smtpSettings = stmpDao.findSettings();
	}

	@Override
	public String getSmtpFQDN()
	{
		return smtpSettings.getSmtpFQDN();
	}

	@Override
	public Integer getSmtpPort()
	{
		return smtpSettings.getSmtpPort();
	}

	@Override
	public boolean isAuthRequired()
	{
		return smtpSettings.isAuthRequired();
	}

	@Override
	public String getUsername()
	{
		return smtpSettings.getUsername();
	}

	@Override
	public String getPassword()
	{
		return smtpSettings.getPassword();
	}

	@Override
	public boolean getUseSSL()
	{
		return smtpSettings.getUseSSL();
	}

	@Override
	public String getBounceEmailAddress()
	{
		return smtpSettings.getBounceEmailAddress();
	}

}
