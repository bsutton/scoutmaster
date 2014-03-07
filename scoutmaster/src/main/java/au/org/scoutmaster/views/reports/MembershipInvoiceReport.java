package au.org.scoutmaster.views.reports;

import net.sf.jasperreports.engine.JRException;
import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.jasper.JasperManager;
import au.com.vaadinutils.menu.Menu;
import au.com.vaadinutils.reportFilter.ReportView;
import au.org.scoutmaster.jasper.JasperSettingsImpl;

@Menu(display = "Member", path="Reports")
public class MembershipInvoiceReport extends ReportView
{
	private static final long serialVersionUID = 1L;
	public static final String NAME = "Member";

	public  MembershipInvoiceReport() throws JRException
	{
		super("Member", new JasperManager(EntityManagerProvider.getEntityManager(), "Member.jasper", new JasperSettingsImpl()));
	}
}
