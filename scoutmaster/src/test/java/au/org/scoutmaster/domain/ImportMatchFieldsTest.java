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

import au.org.scoutmaster.dao.ImportUserMappingDao;
import au.org.scoutmaster.filter.EntityManagerProvider;
import au.org.scoutmaster.filter.Transaction;

public class ImportMatchFieldsTest
{
	private EntityManagerFactory entityManagerFactory;
	private EntityManager em;

	@Before
	public void init() throws ServletException
	{
		entityManagerFactory = Persistence.createEntityManagerFactory("scoutmastertest");
		em = entityManagerFactory.createEntityManager();
		EntityManagerProvider.INSTANCE.setCurrentEntityManager(em);
	}

	@After
	public void finalise()
	{
		em.close();
	}

	@Test
	public void test()
	{
		String existingFieldMapping = "AMapping";

		try (Transaction t = new Transaction(em))
		{
			ImportUserMappingDao daoImportUserMapping = new ImportUserMappingDao(em);

			// Create the mapping
			ImportUserMapping userMapping = new ImportUserMapping(existingFieldMapping);

			Hashtable<String, String> mappings = new Hashtable<>();
			mappings.put("christian", "firstname");
			mappings.put("surname", "lastname");
			for (String mapping : mappings.keySet())
			{
				ImportColumnFieldMapping columnMapping = new ImportColumnFieldMapping(mapping, mappings.get(mapping));
				daoImportUserMapping.addColumnFieldMapping(userMapping, columnMapping);
			}
			daoImportUserMapping.persist(userMapping);
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(em))
		{
			ImportUserMappingDao daoImportUserMapping = new ImportUserMappingDao(em);

			// Now replace the children.
			List<ImportUserMapping> userMappings = daoImportUserMapping.findByName(existingFieldMapping);
			Assert.assertTrue(userMappings.size() == 1);
			ImportUserMapping userMapping = userMappings.get(0);

			userMapping.setName("AName-1");
			Hashtable<String, String> mappings = new Hashtable<>();

			mappings = new Hashtable<>();
			mappings.put("christian1", "firstname");
			mappings.put("surname1", "lastname");

			daoImportUserMapping.clearMappings(userMapping);
			for (String mapping : mappings.keySet())
			{
				ImportColumnFieldMapping columnMapping = new ImportColumnFieldMapping(mapping, mappings.get(mapping));
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
		String existingFieldMapping = "AMapping";

		try (Transaction t = new Transaction(em))
		{

			// Create the mapping
			ImportUserMappingDao daoImportUserMapping = new ImportUserMappingDao(em);
			ImportUserMapping userMapping = new ImportUserMapping(existingFieldMapping);

			Hashtable<String, String> mappings = new Hashtable<>();
			mappings.put("christian", "firstname");
			mappings.put("surname", "lastname");
			for (String mapping : mappings.keySet())
			{
				ImportColumnFieldMapping columnMapping = new ImportColumnFieldMapping(mapping, mappings.get(mapping));
				daoImportUserMapping.addColumnFieldMapping(userMapping, columnMapping);
			}
			daoImportUserMapping.persist(userMapping);

			// Now delete the entity and its children
			List<ImportUserMapping> userMappings = daoImportUserMapping.findByName(existingFieldMapping);
			Assert.assertTrue(userMappings.size() == 1);
			daoImportUserMapping.remove(userMappings.get(0));
			t.commit();
		}
		finally
		{
		}

	}
}
