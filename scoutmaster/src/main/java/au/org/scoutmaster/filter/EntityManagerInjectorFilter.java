package au.org.scoutmaster.filter;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EntityManagerInjectorFilter implements Filter
{

	private EntityManagerFactory entityManagerFactory;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		entityManagerFactory = Persistence.createEntityManagerFactory("scoutmaster");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException
	{
		EntityManager em = entityManagerFactory.createEntityManager();
		
		try (Transaction t = new Transaction(em))
		{
			// Create and set the entity manager
			EntityManagerProvider.INSTANCE.setCurrentEntityManager(em);

			// Handle the request
			filterChain.doFilter(servletRequest, servletResponse);

			t.commit();
		}
		finally
		{
			// Reset the entity manager
			EntityManagerProvider.INSTANCE.setCurrentEntityManager(null);
		}
	}

	@Override
	public void destroy()
	{
		entityManagerFactory = null;
	}
}
