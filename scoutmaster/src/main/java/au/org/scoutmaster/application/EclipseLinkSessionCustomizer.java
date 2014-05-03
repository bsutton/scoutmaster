package au.org.scoutmaster.application;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.JNDIConnector;
import org.eclipse.persistence.sessions.Session;

/**
 * This class is required to use JNDI with Eclipselink. This class is referenced
 * in the persistence.xml.
 *
 * e.g. <persistence-unit name="scoutmaster" transaction-type="RESOURCE_LOCAL">
 * <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
 * <non-jta-data-source>java:comp/env/jdbc/scoutmaster-ds</non-jta-data-source>
 *
 * <properties> <property name="eclipselink.session.customizer"
 * value="au.org.scoutmaster.application.EclipseLinkSessionCustomizer" />
 * <property name="eclipselink.ddl-generation" value="none" /> <property
 * name="eclipselink.ddl-generation.output-mode" value="database" /> <property
 * name="eclipselink.orm.throw.exceptions" value="true" /> <property
 * name="eclipselink.logging.level" value="FINE" /> </properties>
 * </persistence-unit>
 *
 * I really don't know exactly what it does.
 *
 * @author bsutton
 *
 */
public class EclipseLinkSessionCustomizer implements SessionCustomizer
{
	@Override
	public void customize(final Session session) throws Exception
	{
		final JNDIConnector connector = (JNDIConnector) session.getLogin().getConnector();
		connector.setLookupType(JNDIConnector.STRING_LOOKUP);
	}

}
