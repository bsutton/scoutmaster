package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import au.com.vaadinutils.dao.JpaBaseDao;
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

	public JPAContainer<Organisation> createVaadinContainer()
	{

		JPAContainer<Organisation> contactContainer = super.createVaadinContainer();
		contactContainer.addNestedContainerProperty("location.street");
		contactContainer.addNestedContainerProperty("location.city");
		contactContainer.addNestedContainerProperty("location.state");
		contactContainer.addNestedContainerProperty("location.postcode");

		contactContainer.addNestedContainerProperty("phone1.phoneNo");
		contactContainer.addNestedContainerProperty("phone1.primaryPhone");
		contactContainer.addNestedContainerProperty("phone1.phoneType");

		contactContainer.addNestedContainerProperty("phone2.phoneNo");
		contactContainer.addNestedContainerProperty("phone2.primaryPhone");
		contactContainer.addNestedContainerProperty("phone2.phoneType");

		contactContainer.addNestedContainerProperty("phone3.phoneNo");
		contactContainer.addNestedContainerProperty("phone3.primaryPhone");
		contactContainer.addNestedContainerProperty("phone3.phoneType");

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
