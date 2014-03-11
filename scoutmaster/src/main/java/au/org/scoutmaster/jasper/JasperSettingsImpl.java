package au.org.scoutmaster.jasper;

import java.io.File;
import java.util.Properties;

import au.com.vaadinutils.jasper.JasperSettings;

import com.vaadin.server.VaadinSession;

public class JasperSettingsImpl implements JasperSettings
{

	public JasperSettingsImpl()
	{
	}

	@Override
	public String getReportDir()
	{
		return "jasperreports";
	}

	@Override
	public File getDocumentBase()
	{
		return VaadinSession.getCurrent().getService().getBaseDirectory();
	}

	
	@Override
	public File getReportFile(String reportName)
	{
		return new File(new File(getDocumentBase(), getReportDir()), reportName);
	}

}
