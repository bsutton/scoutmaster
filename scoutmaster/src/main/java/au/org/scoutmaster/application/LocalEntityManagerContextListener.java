package au.org.scoutmaster.application;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import au.com.vaadinutils.servlet.VUEntityManagerContextListener;

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

}