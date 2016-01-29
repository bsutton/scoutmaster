package au.org.scoutmaster.servlets;

import javax.persistence.EntityManager;

import org.atmosphere.cpr.Action;
import org.atmosphere.cpr.AtmosphereInterceptorAdapter;
import org.atmosphere.cpr.AtmosphereResource;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.dao.Transaction;

/**
 * Designed to inject the EntityManager into requests that arrive via websockets
 * (Vaadin Push) as these do not go through the standard servlet filter
 * mechanism.
 *
 * This class is installed by adding a parameter to the VaadinServlet mapping in
 * web.xml.
 *
 * e.g. <servlet> <servlet-name>Vaadin Application Servlet</servlet-name>
 * <servlet-class>com.vaadin.server.VaadinServlet</servlet-class>
 * <init-param> <description>Vaadin UI to display</description>
 * <param-name>UI</param-name>
 * <param-value>au.org.scoutmaster.application.NavigatorUI</param-value>
 * </init-param>
 * <init-param> <param-name>org.atmosphere.cpr.AtmosphereInterceptor
 * </param-name> <!-- comma-separated list of fully-qualified class names -->
 * <param-value>au.com.vaadinutils.servlet.AtmosphereFilter</param-value>
 * </init-param> <async-supported>true</async-supported> </servlet>
 *
 *
 */

public class AtmosphereFilter extends AtmosphereInterceptorAdapter
{
	Transaction t;

	EntityManager em;

	@Override
	public Action inspect(AtmosphereResource r)
	{
		// do pre-request stuff
		// In certain circumstances push can be called after the standard filter
		// which means there will already
		// be an EM on the thread hence we check if one already exits
		if (EntityManagerProvider.getEntityManager() == null)
		{
			em = EntityManagerProvider.createEntityManager();
			t = new Transaction(em);

			// Create and set the entity manager
			EntityManagerProvider.setCurrentEntityManager(em);
		}

		return super.inspect(r);
	}

	// do post-request stuff (Vaadin request handling is done at this point)
	@Override
	public void postInspect(AtmosphereResource r)
	{
		try
		{
			// In certain circumstances push can be called after the standard
			// filter which means there will already
			// be an EM on the thread hence if there is no transaction then we
			// didn't create the EM.
			if (t != null)
			{
				try
				{
					t.commit();
				}
				finally
				{
					// we need to guarentee that we cleanup even if the commit
					// fails.
					t.close();
					em.close();
				}
			}
		}
		catch (Throwable e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			// Reset the entity manager as we get a new one everytime we inspect
			// is called, but only if we own the EM which we do if t!=null
			if (t != null)
				EntityManagerProvider.setCurrentEntityManager(null);
		}

	}
}
