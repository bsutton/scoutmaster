package au.com.noojee.scouts.domain;

import java.util.Hashtable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;

import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

public class ImportMatchFieldsTest
{

	@Test
	public void test()
	{
		JPAContainer<ImportUserMapping> userMappings = JPAContainerFactory.make(ImportUserMapping.class, "scouts");

		EntityProvider<ImportUserMapping> ep = userMappings.getEntityProvider();
		EntityManager em = ep.getEntityManager();

		try
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
			userMappings.addEntity(userMapping);
			userMappings.commit();

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
			em.getTransaction().commit();
		}
		finally
		{
			if (em != null)
				em.close();
		}
	}

	@Test
	void cascadeDelete()
	{
		JPAContainer<ImportUserMapping> userMappings = JPAContainerFactory.make(ImportUserMapping.class, "scouts");

		String existingFieldMapping = "AMapping";
		EntityProvider<ImportUserMapping> ep = userMappings.getEntityProvider();
		EntityManager em = ep.getEntityManager();

		try
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
			userMappings.addEntity(userMapping);
			userMappings.commit();

			// Now delete the entity and its children
			Query query = em.createNamedQuery("ImportUserMapping.findByName");
			query.setParameter("name", existingFieldMapping);

			userMapping = (ImportUserMapping) query.getSingleResult();
			em.getTransaction().begin();
			em.remove(userMapping);
			em.getTransaction().commit();
		}
		finally
		{
			if (em != null)
			{
				em.close();
			}
		}

	}
}
