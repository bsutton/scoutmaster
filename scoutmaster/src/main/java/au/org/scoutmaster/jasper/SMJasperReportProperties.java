package au.org.scoutmaster.jasper;

import java.io.File;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.jasper.JasperManager.OutputFormat;
import au.com.vaadinutils.jasper.filter.ReportFilterUIBuilder;
import au.com.vaadinutils.jasper.parameter.ReportParameter;
import au.com.vaadinutils.jasper.ui.CleanupCallback;
import au.com.vaadinutils.jasper.ui.JasperReportProperties;
import au.org.scoutmaster.application.SMSession;

import com.vaadin.server.VaadinSession;

public class SMJasperReportProperties implements JasperReportProperties
{

	private EntityManager em;
	private Connection connection;
	private final String reportTitle;
	private final String reportFilename;
	private final ReportFilterUIBuilder builder;

	public SMJasperReportProperties(final String reportTitle, final String reportFilename)
	{
		this.reportTitle = reportTitle;
		this.reportFilename = reportFilename;
		this.builder = null;
	}

	public SMJasperReportProperties(final String reportTitle, final String reportFilename,
			final ReportFilterUIBuilder builder)
	{
		this.reportTitle = reportTitle;
		this.reportFilename = reportFilename;
		this.builder = builder;
	}

	@Override
	public String getReportTitle()
	{
		return this.reportTitle;
	}

	@Override
	public String getReportFileName()
	{
		return this.reportFilename;
	}

	@Override
	public ReportFilterUIBuilder getFilterBuilder()
	{
		return this.builder;
	}

	@Override
	/**
	 * By default a NOOP you need to overload this method if you need to prepare data for you report.
	 */
	public List<ReportParameter<?>> prepareData(final Collection<ReportParameter<?>> params,
			final CleanupCallback cleanupCallback) throws Exception
			{
		return null;
			}

	@Override
	public String getHeaderFooterTemplateName()
	{
		return "SMStandardHeaderFooter.jasper";
	}

	@Override
	public String getUsername()
	{
		return SMSession.INSTANCE.getLoggedInUser().getFullname();
	}

	@Override
	public File getReportFolder()
	{
		return new File(getDocumentBase(), "jasperreports");
	}

	@Override
	public CleanupCallback getCleanupCallback()
	{
		// NOOP
		return null;
	}

	@Override
	public void prepareForOutputFormat(final OutputFormat outputFormat)
	{
		// NOOP

	}

	@Override
	public void closeDBConnection()
	{
		this.em.close();

	}

	@Override
	public void initDBConnection()
	{
		this.em = EntityManagerProvider.createEntityManager();

	}

	@Override
	public Connection getConnection()
	{
		if (this.connection == null)
		{
			this.connection = this.em.unwrap(Connection.class);
		}
		return this.connection;
	}

	@Override
	public OutputFormat getDefaultFormat()
	{
		return OutputFormat.HTML;
	}

	@Override
	public String generateDynamicHeaderImage(final int pageWidth, final String reportTitle)
	{
		// NOOP
		return null;
	}

	@Override
	public boolean isDevMode()
	{
		return false;
	}

	private File getDocumentBase()
	{
		return VaadinSession.getCurrent().getService().getBaseDirectory();
	}
}
