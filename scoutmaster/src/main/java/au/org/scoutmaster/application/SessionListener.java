package au.org.scoutmaster.application;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.dao.EntityManagerThread;
import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.DaoFactory;
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

		EntityManagerThread<Void> thread = new EntityManagerThread<Void>(new Callable<Void>()
		{
			@Override
			public Void call()
			{
				/**
				 * Create a record of the users session.
				 */

				final JpaBaseDao<SessionHistory, Long> daoSession = new DaoFactory().getSessionHistoryDao();
				final HttpSession session = arg0.getSession();
				final User user = SMSession.INSTANCE.getLoggedInUser();

				final SessionHistory sessionHistory = new SessionHistory();
				sessionHistory.setUser(user);
				sessionHistory.setEnd(new Date());
				sessionHistory.setStart(new Date(session.getCreationTime()));
				daoSession.persist(sessionHistory);
				return null;

			}
		});

		try
		{
			thread.get();
		}
		catch (InterruptedException | ExecutionException e)
		{
			logger.error(e, e);
		}
	}

}