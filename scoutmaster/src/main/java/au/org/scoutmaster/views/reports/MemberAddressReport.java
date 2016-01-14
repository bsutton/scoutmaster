package au.org.scoutmaster.views.reports;

import au.com.vaadinutils.jasper.filter.ReportFilterUIBuilder;
import au.com.vaadinutils.jasper.parameter.ReportParameterConstant;
import au.com.vaadinutils.jasper.ui.JasperReportView;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.application.ScoutmasterViewEnum;
import au.org.scoutmaster.jasper.SMJasperReportProperties;
import net.sf.jasperreports.engine.JRException;

@Menu(display = "Address List", path = "Members")
public class MemberAddressReport extends JasperReportView
{
	public static final class ReportProperties extends SMJasperReportProperties
	{
		public ReportProperties()
		{
			super(ScoutmasterViewEnum.MemberAddress);
		}

		@Override
		public String getReportTitle()
		{
			return "Member Address";
		}

		@Override
		public String getReportFileName()
		{
			return "MemberAddress.jasper";
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

	private static final long serialVersionUID = 1L;
	public static final String NAME = "MemberAddress";

	public MemberAddressReport() throws JRException
	{
		super(new ReportProperties());
	}

	@Override
	public String getDescription()
	{
		return "Generates a list of Member Addresses ";
	}

}
