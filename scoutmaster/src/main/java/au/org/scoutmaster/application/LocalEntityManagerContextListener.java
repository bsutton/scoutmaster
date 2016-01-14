package au.org.scoutmaster.application;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.servlet.VUEntityManagerContextListener;
import au.org.scoutmaster.domain.Group;

/**
 * You need to hook this class as a servlet context listener in your web.xml
 *
 * You then need to create an EntityManagerFactory which the rest of the code
 * relies on.
 *
 * @author bsutton
 *
 */
public class LocalEntityManagerContextListener extends VUEntityManagerContextListener
{
	@Override
	protected EntityManagerFactory getEntityManagerFactory()
	{
		return Persistence.createEntityManagerFactory("scoutmaster");
	}

	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		super.contextInitialized(event);

		/**
		 * Register PreAction so that each time an EntityManager is fetched the
		 * tenant is injected into the EntityManager.
		 */
		EntityManagerProvider.registerPreAction(new EntityManagerProvider.EMAction()
		{

			@Override
			public void run(EntityManager em)
			{
				Group group = SMSession.INSTANCE.getGroup();

				if (group != null)
				{
					// If we know the tenant then lets tell eclipselink
					em.setProperty("eclipselink.tenant-id", "" + group.getId());
				}
			}

		});

		// /**
		// * Register PostAction so that each time an EntityManager is cleared
		// the
		// * tenant is also cleared from the EntityManager.
		// */
		//
		// EntityManagerProvider.registerPostAction(new Runnable()
		// {
		//
		// @Override
		// public void run()
		// {
		// // We remove the tenant from the em as its likely that the em
		// // is re-used and we don't want the tenant id to leak across to
		// // another session.
		// em.setProperty("eclipselink.tenant-id", null);
		// }
		//
		// });

	}

}