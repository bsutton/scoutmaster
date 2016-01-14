package au.org.scoutmaster.views.reports;

import au.com.vaadinutils.jasper.filter.ReportFilterUIBuilder;
import au.com.vaadinutils.jasper.parameter.ReportParameterConstant;
import au.com.vaadinutils.jasper.parameter.ReportParameterTable;
import au.com.vaadinutils.jasper.ui.JasperReportView;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.application.ScoutmasterViewEnum;
import au.org.scoutmaster.domain.Group;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.Raffle_;
import au.org.scoutmaster.jasper.SMJasperReportProperties;
import net.sf.jasperreports.engine.JRException;

@Menu(display = "Raffle Allocations Report", path = "Raffle")
public class RaffleAllocationsReportView extends JasperReportView
{
	public static final class ReportProperties extends SMJasperReportProperties
	{
		private final ReportFilterUIBuilder builder;

		public ReportProperties()
		{
			super(ScoutmasterViewEnum.RaffleBookAllocationWizard);

			final ReportFilterUIBuilder builder = new ReportFilterUIBuilder();

			final Group ourGroup = SMSession.INSTANCE.getGroup();

			builder.addField(new ReportParameterTable<Raffle>("Raffle", "raffleId", Raffle.class, Raffle_.name))
					.addField(new ReportParameterConstant<String>("groupname", ourGroup.getName()));

			ReportParameterConstant<String> param = new ReportParameterConstant<String>("group_id",
					"" + SMSession.INSTANCE.getGroup().getId());
			builder.getReportParameters().add(param);

			this.builder = builder;
		}

		@Override
		public String getReportTitle()
		{
			return "Raffle Allocations";
		}

		@Override
		public String getReportFileName()
		{
			return "RaffleAllocations.jasper";
		}

		@Override
		public ReportFilterUIBuilder getFilterBuilder()
		{
			return this.builder;
		}
	}

	private static final long serialVersionUID = 1L;
	public static final String NAME = "RaffleAllocations";

	public RaffleAllocationsReportView() throws JRException
	{
		super.setReport(new ReportProperties());

	}

	@Override
	public String getDescription()
	{
		return "Generates a list of Allocated Books for the selected Raffle";
	}

}
