package au.org.scoutmaster.views.reports;

import au.com.vaadinutils.jasper.filter.ReportFilterUIBuilder;
import au.com.vaadinutils.jasper.ui.JasperReportView;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.application.ScoutmasterViewEnum;
import au.org.scoutmaster.jasper.SMJasperReportProperties;
import net.sf.jasperreports.engine.JRException;

@Menu(display = "Member Invoice", path = "Reports")
public class MembershipInvoiceReport extends JasperReportView
{
	public static final class ReportProperties extends SMJasperReportProperties
	{
		public ReportProperties()
		{
			super(ScoutmasterViewEnum.Invoice);
		}

		@Override
		public String getReportTitle()
		{
			return "Member Invoice";
		}

		@Override
		public String getReportFileName()
		{
			return "Member.jasper";
		}

		@Override
		public ReportFilterUIBuilder getFilterBuilder()
		{
			return new ReportFilterUIBuilder();
		}
	}

	private static final long serialVersionUID = 1L;
	public static final String NAME = "Member Invoice";

	public MembershipInvoiceReport() throws JRException
	{
		super(new ReportProperties());
	}
}
