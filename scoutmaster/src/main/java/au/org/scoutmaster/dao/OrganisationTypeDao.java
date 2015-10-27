package au.org.scoutmaster.dao;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.OrganisationType;

public class OrganisationTypeDao extends JpaBaseDao<OrganisationType, Long> implements Dao<OrganisationType, Long>
{

	public OrganisationTypeDao()
	{
		// inherit the default per request em.
	}



	@Override
	public JPAContainer<OrganisationType> createVaadinContainer()
	{

		final JPAContainer<OrganisationType> contactContainer = super.createVaadinContainer();

		return contactContainer;
	}

}
