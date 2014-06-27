package au.org.scoutmaster.views.reports;

import net.sf.jasperreports.engine.JRException;
import au.com.vaadinutils.jasper.ui.JasperReportView;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.application.ScoutmasterViewEnum;
import au.org.scoutmaster.jasper.SMJasperReportProperties;

@Menu(display = "Member", path = "Reports")
public class MembershipInvoiceReport extends JasperReportView
{
	private static final long serialVersionUID = 1L;
	public static final String NAME = "Member";

	public MembershipInvoiceReport() throws JRException
	{
		super(new SMJasperReportProperties("Member", "Member.jasper", ScoutmasterViewEnum.Invoice));
	}
}
