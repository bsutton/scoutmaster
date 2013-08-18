package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import au.org.scoutmaster.domain.Activity;

public class ActivityDao extends JpaBaseDao<Activity, Long> implements Dao<Activity, Long>
{

	public ActivityDao()
	{
		// inherit the default per request em. 
	}
	public ActivityDao(EntityManager em)
	{
		super(em);
	}

	@Override
	public Activity findById(Long id)
	{
		Activity activity = entityManager.find(this.entityClass, id);
		return activity;
	}

	@Override
	public List<Activity> findAll()
	{
		Query query = entityManager.createNamedQuery(Activity.FIND_ALL);
		@SuppressWarnings("unchecked")
		List<Activity> list = query.getResultList();
		return list;
	}
}
