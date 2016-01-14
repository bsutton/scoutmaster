package au.org.scoutmaster.views.reports;

import org.joda.time.DateTime;

import au.com.vaadinutils.jasper.filter.ReportFilterUIBuilder;
import au.com.vaadinutils.jasper.parameter.ReportParameterConstant;
import au.com.vaadinutils.jasper.ui.JasperReportView;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.application.ScoutmasterViewEnum;
import au.org.scoutmaster.jasper.SMJasperReportProperties;
import net.sf.jasperreports.engine.JRException;

@Menu(display = "Calendar Report", path = "Calendar")
public class CalendarReportView extends JasperReportView
{
	static public final class ReportProperties extends SMJasperReportProperties
	{
		private final ReportFilterUIBuilder builder;

		public ReportProperties()
		{
			super(ScoutmasterViewEnum.CalendarReport);
			builder = new ReportFilterUIBuilder();

			builder.addDateField("Date Range", "StartDate", "EndDate").setDateRange(new DateTime(),
					new DateTime().plusMonths(1));

			ReportParameterConstant<String> param = new ReportParameterConstant<String>("group_id",
					"" + SMSession.INSTANCE.getGroup().getId());
			builder.getReportParameters().add(param);
		}

		@Override
		public String getReportTitle()
		{
			return "Calendar";
		}

		@Override
		public String getReportFileName()
		{
			return "EventCalendar.jasper";
		}

		@Override
		public ReportFilterUIBuilder getFilterBuilder()
		{
			return this.builder;
		}
	}

	private static final long serialVersionUID = 1L;
	public static final String NAME = "CalendarReport";

	public CalendarReportView() throws JRException
	{
		super.setReport(new ReportProperties());
	}

	@Override
	public String getDescription()
	{
		return "Generates a list of Events for the Scout Group";
	}

}
