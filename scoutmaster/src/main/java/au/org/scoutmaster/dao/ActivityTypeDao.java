package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import au.org.scoutmaster.domain.Activity;
import au.org.scoutmaster.domain.ActivityType;

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

	@Override
	public ActivityType findById(Long id)
	{
		ActivityType activityType = entityManager.find(this.entityClass, id);
		return activityType;
	}

	@Override
	public List<ActivityType> findAll()
	{
		Query query = entityManager.createNamedQuery(Activity.FIND_ALL);
		@SuppressWarnings("unchecked")
		List<ActivityType> list = query.getResultList();
		return list;
	}
}
