package au.org.scoutmaster.views.reports;

import net.sf.jasperreports.engine.JRException;

import org.joda.time.DateTime;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.jasper.JasperManager;
import au.com.vaadinutils.jasper.ReportFilterUIBuilder;
import au.com.vaadinutils.menu.Menu;
import au.com.vaadinutils.reportFilter.ReportView;
import au.org.scoutmaster.jasper.JasperSettingsImpl;

@Menu(display = "Calendar Report", path = "Reports")
public class CalendarReportView extends ReportView
{
	private static final long serialVersionUID = 1L;
	public static final String NAME = "CalendarReport";

	public CalendarReportView() throws JRException
	{
		super("Calendar", new JasperManager(EntityManagerProvider.getEntityManager(), "EventCalendar.jasper",
				new JasperSettingsImpl()));
	}

	@Override
	protected ReportFilterUIBuilder getFilterBuilder()
	{
		ReportFilterUIBuilder builder = new ReportFilterUIBuilder(super.getJasperManager());
		
		builder
				.addDateField("Start Date", "StartDate")
				.addDateField("End Date", "EndDate")
					.setDate(new DateTime().plusMonths(1));
		
		return builder;
	}

	public String getDescription()
	{
		return "Generates a list of Events for the Scout Group";
	}

	
}
