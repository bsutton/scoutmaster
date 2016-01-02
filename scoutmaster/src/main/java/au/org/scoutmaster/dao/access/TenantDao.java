package au.org.scoutmaster.dao.access;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.Dao;
import au.org.scoutmaster.domain.access.Tenant;

public class TenantDao extends JpaBaseDao<Tenant, Long> implements Dao<Tenant, Long>
{

	public TenantDao()
	{
		// inherit the default per request em.
	}

	@Override
	public Tenant findById(final Long id)
	{
		final Tenant user = JpaBaseDao.getEntityManager().find(this.entityClass, id);
		return user;
	}

	@Override
	public JPAContainer<Tenant> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

}
