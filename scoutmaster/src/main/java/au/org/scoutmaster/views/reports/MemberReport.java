package au.org.scoutmaster.views.reports;

import java.util.List;

import net.sf.jasperreports.engine.JRException;
import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.jasper.JasperManager;
import au.com.vaadinutils.menu.Menu;
import au.com.vaadinutils.reportFilter.ReportParameter;
import au.com.vaadinutils.reportFilter.ReportView;
import au.org.scoutmaster.jasper.JasperSettingsImpl;

@Menu(display = "Members", path="Reports")
public class MemberReport extends ReportView
{
	private static final long serialVersionUID = 1L;
	public static final String NAME = "Members";

	public  MemberReport() throws JRException
	{
		super("Members", new JasperManager(EntityManagerProvider.getEntityManager(), "MemberReport.jasper", new JasperSettingsImpl()));
	}

	@Override
	protected List<ReportParameter> getFilters()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getDescription()
	{
		return "Generates a list of Members with Phone, Member No. and Email addresses";
	}


}