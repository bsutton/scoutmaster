package au.org.scoutmaster.dao;

import javax.persistence.EntityManager;

import au.org.scoutmaster.domain.OrganisationType;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class OrganisationTypeDao extends JpaBaseDao<OrganisationType, Long> implements Dao<OrganisationType, Long>
{

	public OrganisationTypeDao()
	{
		// inherit the default per request em.
	}

	public OrganisationTypeDao(EntityManager em)
	{
		super(em);
	}

	public JPAContainer<OrganisationType> makeJPAContainer()
	{

		JPAContainer<OrganisationType> contactContainer = super.makeJPAContainer(OrganisationType.class);

		return contactContainer;
	}

}
