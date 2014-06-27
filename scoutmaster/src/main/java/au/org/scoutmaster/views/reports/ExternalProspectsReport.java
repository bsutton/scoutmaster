package au.org.scoutmaster.views.reports;

import net.sf.jasperreports.engine.JRException;
import au.com.vaadinutils.jasper.ui.JasperReportView;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.application.ScoutmasterViewEnum;
import au.org.scoutmaster.jasper.SMJasperReportProperties;

@Menu(display = "External Prospects", path = "Members")
public class ExternalProspectsReport extends JasperReportView
{
	private static final long serialVersionUID = 1L;
	public static final String NAME = "ExternalProspects";

	public ExternalProspectsReport() throws JRException
	{
		super(new SMJasperReportProperties("External Prospects", "ExternalProspects.jasper", ScoutmasterViewEnum.ExternalProspects));
	}

	@Override
	public String getDescription()
	{
		return "Generates a list of Contacts that have been Tagged as External Prospects";
	}

}
