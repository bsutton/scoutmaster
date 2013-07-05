package au.com.noojee.scouts.filter;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil
{

	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;


	static
	{
		Configuration configuration = new Configuration();
	    configuration.configure();
	    serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();        
	    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}

	public static Session getSession() throws HibernateException
	{
		return sessionFactory.openSession();
	}

	public static Session getCurrentSession() throws HibernateException
	{
		return sessionFactory.getCurrentSession();
	}
}