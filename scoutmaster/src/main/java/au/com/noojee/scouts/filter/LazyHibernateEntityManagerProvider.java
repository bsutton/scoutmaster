package au.com.noojee.scouts.filter;

import javax.persistence.EntityManager;

import org.hibernate.Session;

import com.vaadin.addon.jpacontainer.EntityManagerProvider;

public enum LazyHibernateEntityManagerProvider implements EntityManagerProvider
{
	INSTANCE;
	
	
	@Override
	public EntityManager getEntityManager()
	{
		return entityManagerThreadLocal.get();
	}

	public void setCurrentEntityManager(EntityManager em)
	{
		entityManagerThreadLocal.set(em);
	}

	public Session getSession()
	{
		Session currentSession = HibernateUtil.getCurrentSession();
		if (!currentSession.getTransaction().isActive())
		{
			currentSession.beginTransaction();
		}
		return currentSession;
	}

	void closeSession()
	{
		Session sess = HibernateUtil.getCurrentSession();
		if (sess.getTransaction().isActive())
		{
			sess.getTransaction().commit();
		}
		sess.flush();
		sess.close();
	}
	
	private  ThreadLocal<EntityManager> entityManagerThreadLocal = new ThreadLocal<EntityManager>();


}