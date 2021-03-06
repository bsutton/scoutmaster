package au.org.scoutmaster.jasper;

import java.io.File;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.vaadin.server.VaadinSession;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.jasper.JasperManager.OutputFormat;
import au.com.vaadinutils.jasper.parameter.ReportParameter;
import au.com.vaadinutils.jasper.ui.CleanupCallback;
import au.com.vaadinutils.jasper.ui.JasperReportProperties;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.application.ScoutmasterViewEnum;

public abstract class SMJasperReportProperties implements JasperReportProperties
{

	private EntityManager em;
	private Connection connection;
	private final ScoutmasterViewEnum reportView;

	// public SMJasperReportProperties(final String reportTitle, final String
	// reportFilename,
	// ScoutmasterViewEnum reportView)
	// {
	// this.reportTitle = reportTitle;
	// this.reportFilename = reportFilename;
	// this.builder = null;
	// this.reportView = reportView;
	// }
	//
	public SMJasperReportProperties(ScoutmasterViewEnum reportView)
	{
		this.reportView = reportView;
	}

	/**
	 * By default a NOOP you need to overload this method if you need to prepare
	 * data for you report.
	 */
	@Override
	public List<ReportParameter<?>> prepareData(Collection<ReportParameter<?>> params, String reportFileName,
			CleanupCallback cleanupCallback) throws Exception
	{
		return null;
	}

	@Override
	public String getHeaderFooterTemplateName()
	{
		return null;
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
		this.em.getTransaction().commit();
		this.em.close();

	}

	@Override
	public void initDBConnection()
	{
		this.em = EntityManagerProvider.createEntityManager();
		this.em.getTransaction().begin();
		this.connection = this.em.unwrap(Connection.class);

	}

	@Override
	public Connection getConnection()
	{
		return this.connection;
	}

	@Override
	public OutputFormat getDefaultFormat()
	{
		return OutputFormat.HTML;
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

	@Override
	public Class<? extends JasperReportProperties> getReportClass()
	{
		return this.getClass();
	}

	@Override
	public String getUserEmailAddress()
	{
		return SMSession.INSTANCE.getLoggedInUser().getEmailAddress();
	}

	@Override
	public Enum<?> getReportIdentifier()
	{
		return this.reportView;
	}

	@Override
	public String generateDynamicHeaderImage(int pageWidth, int height, String reportTitle)
	{
		// NOOP
		return null;
	}

	@Override
	public Map<String, Object> getCustomReportParameterMap()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDynamicJrxmlFileName()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
