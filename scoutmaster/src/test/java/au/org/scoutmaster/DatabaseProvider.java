package au.org.scoutmaster;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;

public class DatabaseProvider
{
	private static final String SCOUTMASTERTEST_DB_NAME = "scoutmastertest";
	private static final String MASTER_XML = "src/main/resources/liquibase/db.changelog-master.xml";
	private static Logger logger = LogManager.getLogger(DatabaseProvider.class);
	private static String connectionString;
	private static String username;
	private static String password;

	public static void initDatabaseProvider()
	{

		DatabaseProvider.connectionString = "jdbc:mysql://localhost?sessionVariables=storage_engine=InnoDB";
		DatabaseProvider.username = "scoutmaster";
		DatabaseProvider.password = "master$4scout";

		try
		{
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			final Connection conn = DatabaseProvider.getConnection();

			final Statement stmt = conn.createStatement();

			String sql = "DROP DATABASE if exists " + DatabaseProvider.SCOUTMASTERTEST_DB_NAME;
			stmt.executeUpdate(sql);
			DatabaseProvider.logger.info("Database dropped successfully...");

			sql = "CREATE DATABASE " + DatabaseProvider.SCOUTMASTERTEST_DB_NAME;
			stmt.executeUpdate(sql);
			DatabaseProvider.logger.info("Database created successfully...");

			// Now the connection string to include the db name (can't include
			// it to start with as it shouldn't exists as yet.
			DatabaseProvider.connectionString = "jdbc:mysql://localhost/" + DatabaseProvider.SCOUTMASTERTEST_DB_NAME
					+ "?sessionVariables=storage_engine=InnoDB";
		}
		catch (final SQLException e)
		{
			DatabaseProvider.logger.error(e, e);
		}

	}

	public static Connection getConnection() throws SQLException
	{
		final Connection conn = DriverManager.getConnection(DatabaseProvider.connectionString,
				DatabaseProvider.username, DatabaseProvider.password);
		return conn;

	}

	public static void initLiquibase() throws LiquibaseException, SQLException
	{
		final Liquibase liquibase = new Liquibase(DatabaseProvider.MASTER_XML, new FileSystemResourceAccessor(),
				new JdbcConnection(DatabaseProvider.getConnection()));
		liquibase.update("");

	}

	@After
	public void finalise() throws LiquibaseException, SQLException
	{
		final Liquibase liquibase = new Liquibase(DatabaseProvider.MASTER_XML, new FileSystemResourceAccessor(),
				new JdbcConnection(DatabaseProvider.getConnection()));
		liquibase.update("");
	}

	public static void release() throws SQLException
	{
		DriverManager.deregisterDriver(new com.mysql.jdbc.Driver());

	}

}
