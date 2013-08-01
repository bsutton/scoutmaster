package au.org.scoutmaster.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

import org.apache.log4j.Logger;
import org.junit.After;

public class DatabaseProvider
{
	private static final String SCOUTMASTERTEST_DB_NAME = "scoutmastertest";
	private static final String MASTER_XML = "src/main/resources/liquibase/db.changelog-master.xml";
	private static Logger logger = Logger.getLogger(DatabaseProvider.class);
	private static String connectionString;
	private static String username;
	private static String password;

	public static void initDatabaseProvider()
	{

		connectionString = "jdbc:mysql://localhost/" + SCOUTMASTERTEST_DB_NAME + "?sessionVariables=storage_engine=InnoDB";
		username = "scoutmaster";
		password = "master$4scout";

		try
		{
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			Connection conn = getConnection();

			Statement stmt = conn.createStatement();

			String sql = "DROP DATABASE " + SCOUTMASTERTEST_DB_NAME;
			stmt.executeUpdate(sql);
			logger.info("Database dropped successfully...");

			sql = "CREATE DATABASE " + SCOUTMASTERTEST_DB_NAME;
			stmt.executeUpdate(sql);
			logger.info("Database created successfully...");

		}
		catch (SQLException e)
		{
			logger.error(e, e);
		}

	}

	public static Connection getConnection() throws SQLException
	{
		Connection conn = DriverManager.getConnection(connectionString, username, password);
		return conn;

	}

	static void initLiquibase() throws LiquibaseException, SQLException
	{
		Liquibase liquibase = new Liquibase(MASTER_XML,
				new FileSystemResourceAccessor(), new JdbcConnection(getConnection()));
		liquibase.update("");

	}

	@After
	public void finalise() throws LiquibaseException, SQLException
	{
		Liquibase liquibase = new Liquibase(MASTER_XML,
				new FileSystemResourceAccessor(), new JdbcConnection(getConnection()));
		liquibase.update("");
	}

	public static void release() throws SQLException
	{
		DriverManager.deregisterDriver(new com.mysql.jdbc.Driver());

	}

}
