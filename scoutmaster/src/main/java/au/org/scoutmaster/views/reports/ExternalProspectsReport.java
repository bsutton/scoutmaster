package au.org.scoutmaster.views.reports;

import java.util.List;

import net.sf.jasperreports.engine.JRException;
import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.jasper.JasperManager;
import au.com.vaadinutils.menu.Menu;
import au.com.vaadinutils.reportFilter.ReportParameter;
import au.com.vaadinutils.reportFilter.ReportView;
import au.org.scoutmaster.jasper.JasperSettingsImpl;

@Menu(display = "External Prospects", path="Reports")
public class ExternalProspectsReport extends ReportView
{
	private static final long serialVersionUID = 1L;
	public static final String NAME = "ExternalProspects";

	public  ExternalProspectsReport() throws JRException
	{
		super("External Prospects", new JasperManager(EntityManagerProvider.getEntityManager(), "ExternalProspects.jasper", new JasperSettingsImpl()));
	}

	@Override
	protected List<ReportParameter> getFilters()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getDescription()
	{
		return "Generates a list of Contacts that have been Tagged as External Prospects";
	}


}
