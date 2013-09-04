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

import org.apache.log4j.Logger;

import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.dao.EntityManagerProvider;

public class ContextListener implements ServletContextListener
{
	private static final String JNDI_SCOUTMASTER_DS = "java:comp/env/jdbc/scoutmaster-ds";
	private static final Logger logger = Logger.getLogger(ContextListener.class);
	private static final String MASTER_XML = "liquibase/db.changelog-master.xml";

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		Liquibase liquibase;
		try
		{
			String masterPath = sce.getServletContext().getRealPath(new File("WEB-INF/classes/", MASTER_XML).getPath());
			logger.info("Initialising liquibase");

			File masterFile = new File(masterPath);
			File baseDir = masterFile.getParentFile().getParentFile();

			liquibase = new Liquibase(MASTER_XML, new FileSystemResourceAccessor(baseDir.getCanonicalPath()),
					new JdbcConnection(getConnection()));
			liquibase.update("");
			logger.info("Liquibase has completed successfully");
		}
		catch (LiquibaseException | NamingException | SQLException | IOException e)
		{
			logger.info("Liquibase failed.");
			logger.error(e, e);
		}
		
		FormHelper.setEntityManagerFactory(EntityManagerProvider.INSTANCE);

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{

	}

	Connection getConnection() throws NamingException, SQLException
	{
		Context context = new InitialContext();
		DataSource ds = (DataSource) context.lookup(JNDI_SCOUTMASTER_DS);
		Connection connection = ds.getConnection();
		return connection;
	}

}
