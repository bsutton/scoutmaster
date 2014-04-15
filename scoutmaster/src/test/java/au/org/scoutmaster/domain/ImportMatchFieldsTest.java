package au.org.scoutmaster.domain;

import java.util.Hashtable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.ImportUserMappingDao;
import au.org.scoutmaster.dao.Transaction;

public class ImportMatchFieldsTest
{
	private EntityManagerFactory entityManagerFactory;
	private EntityManager em;

	@Before
	public void init() throws ServletException
	{
		this.entityManagerFactory = Persistence.createEntityManagerFactory("scoutmastertest");
		this.em = this.entityManagerFactory.createEntityManager();
		EntityManagerProvider.setCurrentEntityManager(this.em);
	}

	@After
	public void finalise()
	{
		this.em.close();
	}

	@Test
	public void test()
	{
		final String existingFieldMapping = "AMapping";

		try (Transaction t = new Transaction(this.em))
		{
			final ImportUserMappingDao daoImportUserMapping = new DaoFactory(this.em).getImportUserMappingDao();

			// Create the mapping
			final ImportUserMapping userMapping = new ImportUserMapping(existingFieldMapping);

			final Hashtable<String, String> mappings = new Hashtable<>();
			mappings.put("christian", "firstname");
			mappings.put("surname", "lastname");
			for (final String mapping : mappings.keySet())
			{
				final ImportColumnFieldMapping columnMapping = new ImportColumnFieldMapping(mapping,
						mappings.get(mapping));
				daoImportUserMapping.addColumnFieldMapping(userMapping, columnMapping);
			}
			daoImportUserMapping.persist(userMapping);
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(this.em))
		{
			final ImportUserMappingDao daoImportUserMapping = new DaoFactory(this.em).getImportUserMappingDao();

			// Now replace the children.
			final List<ImportUserMapping> userMappings = daoImportUserMapping.findByName(existingFieldMapping);
			Assert.assertTrue(userMappings.size() == 1);
			final ImportUserMapping userMapping = userMappings.get(0);

			userMapping.setName("AName-1");
			Hashtable<String, String> mappings = new Hashtable<>();

			mappings = new Hashtable<>();
			mappings.put("christian1", "firstname");
			mappings.put("surname1", "lastname");

			daoImportUserMapping.clearMappings(userMapping);
			for (final String mapping : mappings.keySet())
			{
				final ImportColumnFieldMapping columnMapping = new ImportColumnFieldMapping(mapping,
						mappings.get(mapping));
				daoImportUserMapping.addColumnFieldMapping(userMapping, columnMapping);
			}
			daoImportUserMapping.merge(userMapping);
			t.commit();
		}
		finally
		{
		}
	}

	@Test
	public void cascadeDelete()
	{
		final String existingFieldMapping = "AMapping";

		try (Transaction t = new Transaction(this.em))
		{

			// Create the mapping
			final ImportUserMappingDao daoImportUserMapping = new DaoFactory(this.em).getImportUserMappingDao();
			final ImportUserMapping userMapping = new ImportUserMapping(existingFieldMapping);

			final Hashtable<String, String> mappings = new Hashtable<>();
			mappings.put("christian", "firstname");
			mappings.put("surname", "lastname");
			for (final String mapping : mappings.keySet())
			{
				final ImportColumnFieldMapping columnMapping = new ImportColumnFieldMapping(mapping,
						mappings.get(mapping));
				daoImportUserMapping.addColumnFieldMapping(userMapping, columnMapping);
			}
			daoImportUserMapping.persist(userMapping);

			// Now delete the entity and its children
			final List<ImportUserMapping> userMappings = daoImportUserMapping.findByName(existingFieldMapping);
			Assert.assertTrue(userMappings.size() == 1);
			daoImportUserMapping.remove(userMappings.get(0));
			t.commit();
		}
		finally
		{
		}

	}
}
