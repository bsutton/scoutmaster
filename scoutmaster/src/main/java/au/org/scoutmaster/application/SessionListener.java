package au.org.scoutmaster.application;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.Transaction;
import au.org.scoutmaster.domain.access.SessionHistory;
import au.org.scoutmaster.domain.access.User;

public class SessionListener implements HttpSessionListener
{

	private static Logger logger = LogManager.getLogger();

	@Override
	public void sessionCreated(final HttpSessionEvent arg0)
	{
		SessionListener.logger.debug("Session created");
	}

	@Override
	public void sessionDestroyed(final HttpSessionEvent arg0)
	{
		SessionListener.logger.debug("Session destroyed");

		/**
		 * NOTE: this won't work until we move to tomcat 8 as vaadin uses a
		 * FakeHttpSession which is something to do with atmosphere. Tomcat 8
		 * resolves this issue.
		 *
		 * In the mean time we have no session history.
		 */
		/**
		 * Create a record of the users session.
		 */
		final EntityManager em = EntityManagerProvider.createEntityManager();
		try (Transaction t = new Transaction(em))
		{
			final JpaBaseDao<SessionHistory, Long> daoSession = new DaoFactory(em).getSessionHistoryDao();
			final HttpSession session = arg0.getSession();
			final User user = (User) session.getAttribute(SMSession.USER);

			final SessionHistory sessionHistory = new SessionHistory();
			sessionHistory.setUser(user);
			sessionHistory.setEnd(new Date());
			sessionHistory.setStart(new Date(session.getCreationTime()));
			daoSession.persist(sessionHistory);
			t.commit();
		}
		finally
		{
		}
	}

}