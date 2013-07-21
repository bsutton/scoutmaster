package au.org.scoutmaster.application;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.JNDIConnector;
import org.eclipse.persistence.sessions.Session;

/**
 * This class is required to use JNDI with Eclipselink.
 * This class is referenced in the persistence.xml.
 * 
 * I really don't know exactly what it does.
 * 
 * @author bsutton
 *
 */
public class EclipseLinkSessionCustomizer implements SessionCustomizer
{
	public void customize(Session session) throws Exception
	{
		JNDIConnector connector = (JNDIConnector) session.getLogin().getConnector();
		connector.setLookupType(JNDIConnector.STRING_LOOKUP);
	}

}
