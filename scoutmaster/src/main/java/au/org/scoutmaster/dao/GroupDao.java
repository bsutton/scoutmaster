package au.org.scoutmaster.dao;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.Group;

public class GroupDao extends JpaBaseDao<Group, Long> implements Dao<Group, Long>
{

	public GroupDao()
	{
		// inherit the default per request em.
	}

	@Override
	public Group findById(final Long id)
	{
		final Group group = JpaBaseDao.getEntityManager().find(this.entityClass, id);
		return group;
	}

	public Group findByName(final String groupName)
	{
		return super.findSingleBySingleParameter(Group.FIND_BY_NAME, "name", groupName);
	}

	@Override
	public JPAContainer<Group> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

}
