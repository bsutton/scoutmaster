package au.org.scoutmaster.views.reports;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.jasper.JasperManager;
import au.com.vaadinutils.menu.Menu;
import au.com.vaadinutils.reportFilter.ReportParameter;
import au.com.vaadinutils.reportFilter.ReportParameterDate;
import au.com.vaadinutils.reportFilter.ReportParameterString;
import au.com.vaadinutils.reportFilter.ReportView;
import au.org.scoutmaster.jasper.JasperSettingsImpl;

@Menu(display = "Calendar Report", path = "Reports")
public class CalendarReport extends ReportView
{
	private static final long serialVersionUID = 1L;
	public static final String NAME = "CalendarReport";

	public CalendarReport() throws JRException
	{
		super("Calendar", new JasperManager(EntityManagerProvider.getEntityManager(), "EventCalendar.jasper",
				new JasperSettingsImpl()));
	}

	@Override
	protected List<ReportParameter> getFilters()
	{
		List<ReportParameter> rparams = new ArrayList<>();

		JasperManager manager = super.getJasperManager();

		JRParameter[] params = manager.getParameters();

		for (JRParameter param : params)
		{
			if (!param.isSystemDefined() && param.isForPrompting())
				switch (param.getValueClassName())
				{
					case "java.util.Date":
						rparams.add(new ReportParameterDate(param.getDescription(), param.getName()));
						break;

					case "java.util.String":
						rparams.add(new ReportParameterString(param.getDescription(), param.getName()));
						break;
				}

		}

		return rparams;
	}

	public String getDescription()
	{
		return "Generates a list of Events for the Scout Group";
	}

}
