package au.org.scoutmaster.application;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.access.SessionHistory;
import au.org.scoutmaster.domain.access.User;


public class SessionListener implements HttpSessionListener {

	private static Logger logger = LogManager.getLogger();

    @Override
    public void sessionCreated(HttpSessionEvent arg0) {
    	logger.debug("Session created");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent arg0) {
    	logger.debug("Session destroyed");
    	
    	
    	/**
    	 * NOTE: this won't work until we move to tomcat 8 as vaadin
    	 * uses a FakeHttpSession which is something to do with atmosphere.
    	 * Tomcat 8 resolves this issue.
    	 * 
    	 * In the mean time we have no session history.
    	 */
    	/**
    	 * Create a record of the users session.
    	 */
		JpaBaseDao<SessionHistory, Long> daoSession = DaoFactory.getGenericDao(SessionHistory.class);
    	HttpSession session = arg0.getSession();
    	User user = (User) session.getAttribute(SMSession.USER);
    	
    	SessionHistory sessionHistory = new SessionHistory();
    	sessionHistory.setUser(user);
    	sessionHistory.setEnd(new Date());
		sessionHistory.setStart(new Date(session.getCreationTime()));
    	daoSession.persist(sessionHistory);
    }

}