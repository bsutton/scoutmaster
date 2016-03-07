package au.org.scoutmaster.domain;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import au.org.scoutmaster.DatabaseProvider;
import liquibase.exception.LiquibaseException;

@RunWith(Suite.class)
@SuiteClasses(
{ ContactTest.class, ImportMatchFieldsTest.class, TagTest.class })
public class AllTests
{
	@BeforeClass
	public static void setUpClass() throws LiquibaseException, SQLException
	{
		DatabaseProvider.initDatabaseProvider(true);
		DatabaseProvider.initLiquibase();
	}

	@AfterClass
	public static void tearDownClass() throws SQLException
	{
		DatabaseProvider.release();
	}

}
