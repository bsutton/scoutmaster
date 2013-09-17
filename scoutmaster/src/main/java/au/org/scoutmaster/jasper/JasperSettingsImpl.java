package au.org.scoutmaster.jasper;

import java.io.File;
import java.util.Properties;

import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMTPSettingsDao;
import au.org.scoutmaster.domain.SMTPServerSettings;

import com.vaadin.server.VaadinSession;

public class JasperSettingsImpl implements JasperSettings
{
	private SMTPServerSettings smtpSettings;

	public JasperSettingsImpl()
	{
		SMTPSettingsDao stmpDao = new DaoFactory().getSMTPSettingsDao();
		smtpSettings = stmpDao.findSettings();
	}

	@Override
	public String getReportDir()
	{
		return null;
	}

	@Override
	public File getDocumentBase()
	{
		return VaadinSession.getCurrent().getService().getBaseDirectory();
	}

	@Override
	public File getInitParameterRealPath(String paramName)
	{
		 Properties parameters = VaadinSession.getCurrent().getConfiguration().getInitParameters();
		 
		 String value = (String) parameters.get(paramName);
		 
		 return new File(VaadinSession.getCurrent().getService().getBaseDirectory(), value);
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
