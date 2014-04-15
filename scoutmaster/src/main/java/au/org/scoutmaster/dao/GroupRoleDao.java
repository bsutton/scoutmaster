package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.GroupRole;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class GroupRoleDao extends JpaBaseDao<GroupRole, Long> implements Dao<GroupRole, Long>
{
	@SuppressWarnings("unused")
	static private Logger logger = LogManager.getLogger(GroupRoleDao.class);

	public GroupRoleDao()
	{
		// inherit the default per request em.
	}

	public GroupRoleDao(final EntityManager em)
	{
		super(em);
	}

	@SuppressWarnings("unchecked")
	public List<GroupRole> findByName(final String name)
	{
		final Query query = this.entityManager.createNamedQuery(GroupRole.FIND_BY_NAME);
		query.setParameter("name", name);
		final List<GroupRole> results = query.getResultList();
		return results;
	}

	@Override
	public JPAContainer<GroupRole> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}
}
