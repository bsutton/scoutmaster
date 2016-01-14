package au.org.scoutmaster.views.reports;

import au.com.vaadinutils.jasper.filter.ReportFilterUIBuilder;
import au.com.vaadinutils.jasper.parameter.ReportParameterConstant;
import au.com.vaadinutils.jasper.ui.JasperReportView;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.application.ScoutmasterViewEnum;
import au.org.scoutmaster.jasper.SMJasperReportProperties;
import net.sf.jasperreports.engine.JRException;

@Menu(display = "Phone List", path = "Members")
public class MemberReport extends JasperReportView
{
	private static final long serialVersionUID = 1L;
	public static final String NAME = "Members";

	public MemberReport() throws JRException
	{

		super(new ReportProperties());
	}

	@Override
	public String getDescription()
	{
		return "Generates a list of Members with Phone, Member No. and Email addresses";
	}

	static public final class ReportProperties extends SMJasperReportProperties
	{
		public ReportProperties()
		{
			super(ScoutmasterViewEnum.Member);
		}

		@Override
		public String getReportTitle()
		{
			return "Members";
		}

		@Override
		public String getReportFileName()
		{
			return "MemberReport.jasper";
		}

		@Override
		public ReportFilterUIBuilder getFilterBuilder()
		{
			ReportFilterUIBuilder builder = new ReportFilterUIBuilder();

			ReportParameterConstant<String> param = new ReportParameterConstant<String>("group_id",
					"" + SMSession.INSTANCE.getGroup().getId());
			builder.getReportParameters().add(param);

			return builder;
		}
	}
}
