package au.org.scoutmaster.dao;

import javax.persistence.EntityManager;

import au.org.scoutmaster.domain.ActivityType;
import au.org.scoutmaster.domain.ActivityType_;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class ActivityTypeDao extends JpaBaseDao<ActivityType, Long> implements Dao<ActivityType, Long>
{

	public ActivityTypeDao()
	{
		// inherit the default per request em. 
	}
	public ActivityTypeDao(EntityManager em)
	{
		super(em);
	}

	
	public ActivityType findByName(String name)
	{
		return super.findSingleBySingleParameter(ActivityType.FIND_BY_NAME, ActivityType_.name, name);
	}
	@Override
	public JPAContainer<ActivityType> makeJPAContainer()
	{
		return super.makeJPAContainer(ActivityType.class);
	}
}
