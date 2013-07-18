package au.org.scoutmaster.domain;

import java.util.Hashtable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
		try (Transaction t = new Transaction(em))
		{
			String existingFieldMapping = "AMapping";

			// Create the mapping
			ImportUserMapping userMapping = new ImportUserMapping(existingFieldMapping);

			Hashtable<String, String> mappings = new Hashtable<>();
			mappings.put("christian", "firstname");
			mappings.put("surname", "lastname");
			for (String mapping : mappings.keySet())
			{
				ImportColumnFieldMapping columnMapping = new ImportColumnFieldMapping(mapping, mappings.get(mapping));
				userMapping.addColumnFieldMapping(columnMapping);
			}
			em.persist(userMapping);

			// Now replace the children.

			Query query = em.createNamedQuery("ImportUserMapping.findByName");
			query.setParameter("name", existingFieldMapping);

			userMapping = (ImportUserMapping) query.getSingleResult();
			em.getTransaction().begin();
			userMapping.setName("AName-1");
			mappings = new Hashtable<>();
			mappings.put("christian1", "firstname");
			mappings.put("surname1", "lastname");

			userMapping.clearMappings();
			for (String mapping : mappings.keySet())
			{
				ImportColumnFieldMapping columnMapping = new ImportColumnFieldMapping(mapping, mappings.get(mapping));
				userMapping.addColumnFieldMapping(columnMapping);
			}
			t.commit();
		}
		finally
		{
		}
	}

	@Test
	void cascadeDelete()
	{
		String existingFieldMapping = "AMapping";

		try (Transaction t = new Transaction(em))
		{

			// Create the mapping
			ImportUserMapping userMapping = new ImportUserMapping(existingFieldMapping);

			Hashtable<String, String> mappings = new Hashtable<>();
			mappings.put("christian", "firstname");
			mappings.put("surname", "lastname");
			for (String mapping : mappings.keySet())
			{
				ImportColumnFieldMapping columnMapping = new ImportColumnFieldMapping(mapping, mappings.get(mapping));
				userMapping.addColumnFieldMapping(columnMapping);
			}
			em.persist(userMapping);

			// Now delete the entity and its children
			Query query = em.createNamedQuery("ImportUserMapping.findByName");
			query.setParameter("name", existingFieldMapping);

			userMapping = (ImportUserMapping) query.getSingleResult();
			em.remove(userMapping);
			t.commit();
		}
		finally
		{
		}

	}
}
