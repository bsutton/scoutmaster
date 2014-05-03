package au.org.scoutmaster.views.reports;

import net.sf.jasperreports.engine.JRException;
import au.com.vaadinutils.jasper.ui.JasperReportView;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.jasper.SMJasperReportProperties;

@Menu(display = "Address List", path = "Members")
public class MemberAddressReport extends JasperReportView
{
	private static final long serialVersionUID = 1L;
	public static final String NAME = "MemberAddress";

	public MemberAddressReport() throws JRException
	{
		super(new SMJasperReportProperties("Member Address", "MemberAddress.jasper"));
	}

	@Override
	public String getDescription()
	{
		return "Generates a list of Member Addresses ";
	}

}
