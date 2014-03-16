package au.org.scoutmaster.dao.access;

import javax.persistence.EntityManager;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.Dao;
import au.org.scoutmaster.domain.access.SessionHistory;

public class SessionHistoryDao extends JpaBaseDao<SessionHistory, Long> implements Dao<SessionHistory, Long>
{
	public SessionHistoryDao()
	{
		// inherit the default per request em.
	}

	public SessionHistoryDao(EntityManager em)
	{
		super(em);
	}
}
