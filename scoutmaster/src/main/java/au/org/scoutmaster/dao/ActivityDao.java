package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;

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
	public List<Activity> findAll()
	{
		return super.findAll(Activity.FIND_ALL);
	}
}
