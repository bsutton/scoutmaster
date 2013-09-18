package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import au.org.scoutmaster.domain.Organisation;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class OrganisationDao extends JpaBaseDao<Organisation, Long> implements Dao<Organisation, Long>
{

	public OrganisationDao()
	{
		// inherit the default per request em.
	}

	public OrganisationDao(EntityManager em)
	{
		super(em);
	}


	public JPAContainer<Organisation> makeJPAContainer()
	{
		
		JPAContainer<Organisation> contactContainer = super.makeJPAContainer(Organisation.class);
		contactContainer.addNestedContainerProperty("location.street");
		contactContainer.addNestedContainerProperty("location.city");
		contactContainer.addNestedContainerProperty("location.state");
		contactContainer.addNestedContainerProperty("location.postcode");

		return contactContainer;
	}

	public Organisation findOurScoutGroup()
	{
		Organisation entity = null;
		Query query = entityManager.createNamedQuery(Organisation.FIND_OUR_SCOUT_GROUP);

		@SuppressWarnings("unchecked")
		List<Organisation> entities = query.getResultList();
		if (entities.size() > 0)
			entity = entities.get(0);
		return entity;

	}

}
