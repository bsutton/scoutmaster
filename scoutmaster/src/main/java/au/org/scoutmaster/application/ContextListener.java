package au.org.scoutmaster.application;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContextListener implements ServletContextListener
{
	private static final String JNDI_SCOUTMASTER_DS = "java:comp/env/jdbc/scoutmaster-ds";
	private static final Logger logger = LogManager.getLogger(ContextListener.class);
	private static final String MASTER_XML = "liquibase/db.changelog-master.xml";

	@Override
	public void contextInitialized(final ServletContextEvent sce)
	{
		Liquibase liquibase;
		try (Connection conn = getConnection())
		{
			final String masterPath = sce.getServletContext().getRealPath(
					new File("WEB-INF/classes/", ContextListener.MASTER_XML).getPath());
			ContextListener.logger.info("Initialising liquibase");

			final File masterFile = new File(masterPath);
			final File baseDir = masterFile.getParentFile().getParentFile();

			liquibase = new Liquibase(ContextListener.MASTER_XML, new FileSystemResourceAccessor(
					baseDir.getCanonicalPath()), new JdbcConnection(conn));
			liquibase.update("");

			ContextListener.logger.info("Liquibase has completed successfully");

		}
		catch (LiquibaseException | NamingException | SQLException | IOException e)
		{
			ContextListener.logger.info("Liquibase failed.");
			ContextListener.logger.error(e, e);
		}

	}

	@Override
	public void contextDestroyed(final ServletContextEvent sce)
	{

	}

	Connection getConnection() throws NamingException, SQLException
	{
		final Context context = new InitialContext();
		final DataSource ds = (DataSource) context.lookup(ContextListener.JNDI_SCOUTMASTER_DS);
		final Connection connection = ds.getConnection();
		return connection;
	}

}
