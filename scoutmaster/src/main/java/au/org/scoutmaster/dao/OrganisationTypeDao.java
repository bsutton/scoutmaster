package au.org.scoutmaster.dao;

import javax.persistence.EntityManager;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.OrganisationType;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class OrganisationTypeDao extends JpaBaseDao<OrganisationType, Long> implements Dao<OrganisationType, Long>
{

	public OrganisationTypeDao()
	{
		// inherit the default per request em.
	}

	public OrganisationTypeDao(final EntityManager em)
	{
		super(em);
	}

	@Override
	public JPAContainer<OrganisationType> createVaadinContainer()
	{

		final JPAContainer<OrganisationType> contactContainer = super.createVaadinContainer();

		return contactContainer;
	}

}
