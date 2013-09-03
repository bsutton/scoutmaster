package au.org.scoutmaster.filter;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.dao.Transaction;
import au.org.scoutmaster.application.LocalEntityManagerFactory;

public class EntityManagerInjectorFilter implements Filter
{
	private static Logger logger = Logger.getLogger(EntityManagerInjectorFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException
	{
		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		try (Transaction t = new Transaction(em))
		{
			// Create and set the entity manager
			EntityManagerProvider.INSTANCE.setCurrentEntityManager(em);

			// Handle the request
			filterChain.doFilter(servletRequest, servletResponse);

			t.commit();
		}
		catch (RollbackException | ConstraintViolationException  e)
		{
			ConstraintViolationException violationException = null;

			if (e instanceof ConstraintViolationException)
				violationException = (ConstraintViolationException) e;

			if (e.getCause() instanceof ConstraintViolationException)
			{
				violationException = (ConstraintViolationException) e.getCause();
			}

			if (violationException != null)
			{
				for (ConstraintViolation<?> violation : violationException.getConstraintViolations())
				{
					StringBuilder sb = new StringBuilder();
					sb.append("Constraint Violation: \n");
					sb.append("Entity:" + violation.getRootBean());
					sb.append("Error: " + violation.getMessage() + "\n");
					sb.append(" on property: " + violation.getPropertyPath() + "\n");
					sb.append("Constraint:" + violation.getMessageTemplate());
					
					logger.error(sb.toString());
				}

			}
			throw e;
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
		// entityManagerFactory = null;
	}
}
