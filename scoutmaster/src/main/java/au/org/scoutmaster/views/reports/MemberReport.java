package au.org.scoutmaster.views.reports;

import net.sf.jasperreports.engine.JRException;
import au.com.vaadinutils.jasper.ui.JasperReportView;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.application.ScoutmasterViewEnum;
import au.org.scoutmaster.jasper.SMJasperReportProperties;

@Menu(display = "Phone List", path = "Members")
public class MemberReport extends JasperReportView
{
	private static final long serialVersionUID = 1L;
	public static final String NAME = "Members";

	public MemberReport() throws JRException
	{
		super(new SMJasperReportProperties("Members", "MemberReport.jasper", ScoutmasterViewEnum.Member));
	}

	@Override
	public String getDescription()
	{
		return "Generates a list of Members with Phone, Member No. and Email addresses";
	}
}
