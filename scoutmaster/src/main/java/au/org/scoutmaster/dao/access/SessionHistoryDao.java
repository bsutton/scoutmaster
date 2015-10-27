package au.org.scoutmaster.dao.access;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.Dao;
import au.org.scoutmaster.domain.access.SessionHistory;

public class SessionHistoryDao extends JpaBaseDao<SessionHistory, Long> implements Dao<SessionHistory, Long>
{
	public SessionHistoryDao()
	{
		// inherit the default per request em.
	}


}
