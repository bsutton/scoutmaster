package au.org.scoutmaster.dao;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.ScoutGroup;

public class GroupDao extends JpaBaseDao<ScoutGroup, Long> implements Dao<ScoutGroup, Long>
{

	public GroupDao()
	{
		// inherit the default per request em.
	}

	@Override
	public ScoutGroup findById(final Long id)
	{
		final ScoutGroup group = JpaBaseDao.getEntityManager().find(this.entityClass, id);
		return group;
	}

	public ScoutGroup findByName(final String groupName)
	{
		return super.findSingleBySingleParameter(ScoutGroup.FIND_BY_NAME, "name", groupName);
	}

	@Override
	public JPAContainer<ScoutGroup> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

}
