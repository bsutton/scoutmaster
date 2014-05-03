package au.org.scoutmaster.views.reports;

import net.sf.jasperreports.engine.JRException;
import au.com.vaadinutils.jasper.filter.ReportFilterUIBuilder;
import au.com.vaadinutils.jasper.parameter.ReportParameterConstant;
import au.com.vaadinutils.jasper.parameter.ReportParameterTable;
import au.com.vaadinutils.jasper.ui.JasperReportView;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Organisation;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.Raffle_;
import au.org.scoutmaster.jasper.SMJasperReportProperties;

@Menu(display = "Raffle Allocations Report", path = "Raffle")
public class RaffleAllocationsReportView extends JasperReportView
{
	private static final long serialVersionUID = 1L;
	public static final String NAME = "RaffleAllocations";

	public RaffleAllocationsReportView() throws JRException
	{
		final ReportFilterUIBuilder builder = new ReportFilterUIBuilder();

		final Organisation ourGroup = new DaoFactory().getOrganisationDao().findOurScoutGroup();
		builder.addField(new ReportParameterTable<Raffle>("Raffle", "raffleId", Raffle.class, Raffle_.name, false))
		.addField(new ReportParameterConstant<String>("groupname", ourGroup.getName()));
		final SMJasperReportProperties report = new SMJasperReportProperties("Raffle Allocations",
				"RaffleAllocations.jasper", builder);

		super.setReport(report);

	}

	@Override
	public String getDescription()
	{
		return "Generates a list of Allocated Books for the selected Raffle";
	}

}
