package au.org.scoutmaster.views.reports;

import net.sf.jasperreports.engine.JRException;

import org.joda.time.DateTime;

import au.com.vaadinutils.jasper.filter.ReportFilterUIBuilder;
import au.com.vaadinutils.jasper.ui.JasperReportView;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.jasper.SMJasperReportProperties;

@Menu(display = "Calendar Report", path = "Calendar")
public class CalendarReportView extends JasperReportView
{
	private static final long serialVersionUID = 1L;
	public static final String NAME = "CalendarReport";

	public CalendarReportView() throws JRException
	{
		final ReportFilterUIBuilder builder = new ReportFilterUIBuilder();

		builder.addDateField("Start Date", "StartDate").addDateField("End Date", "EndDate")
		.setDate(new DateTime().plusMonths(1));
		final SMJasperReportProperties report = new SMJasperReportProperties("Calendar", "EventCalendar.jasper",
				builder);

		super.setReport(report);

	}

	@Override
	public String getDescription()
	{
		return "Generates a list of Events for the Scout Group";
	}

}
