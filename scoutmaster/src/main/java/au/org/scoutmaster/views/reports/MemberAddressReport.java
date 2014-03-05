package au.org.scoutmaster.views.reports;

import java.util.List;

import net.sf.jasperreports.engine.JRException;
import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.jasper.JasperManager;
import au.com.vaadinutils.menu.Menu;
import au.com.vaadinutils.reportFilter.FilterReport;
import au.com.vaadinutils.reportFilter.ReportParameter;
import au.org.scoutmaster.jasper.JasperSettingsImpl;

@Menu(display = "Member Address", path="Reports")
public class MemberAddressReport extends FilterReport
{
	private static final long serialVersionUID = 1L;
	public static final String NAME = "MemberAddress";

	public  MemberAddressReport() throws JRException
	{
		super("Member Address", new JasperManager(EntityManagerProvider.getEntityManager(), "MemberAddress.jasper", new JasperSettingsImpl()));
	}

	@Override
	protected List<ReportParameter> getFilters()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
