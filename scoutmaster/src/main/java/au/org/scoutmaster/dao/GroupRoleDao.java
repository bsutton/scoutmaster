package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.GroupRole;

public class GroupRoleDao extends JpaBaseDao<GroupRole, Long> implements Dao<GroupRole, Long>
{
	@SuppressWarnings("unused")
	static private Logger logger = LogManager.getLogger(GroupRoleDao.class);

	public GroupRoleDao()
	{
		// inherit the default per request em.
	}

	

	@SuppressWarnings("unchecked")
	public List<GroupRole> findByName(final String name)
	{
		final Query query = JpaBaseDao.getEntityManager().createNamedQuery(GroupRole.FIND_BY_NAME);
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
