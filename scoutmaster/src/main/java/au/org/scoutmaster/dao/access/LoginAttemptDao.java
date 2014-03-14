package au.org.scoutmaster.dao.access;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.Dao;
import au.org.scoutmaster.domain.access.LoginAttempt;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class LoginAttemptDao extends JpaBaseDao<LoginAttempt, Long> implements Dao<LoginAttempt, Long>
{

	public LoginAttemptDao()
	{
		// inherit the default per request em.
	}

	public LoginAttemptDao(EntityManager em)
	{
		super(em);
	}

	@Override
	public LoginAttempt findById(Long id)
	{
		LoginAttempt user = entityManager.find(this.entityClass, id);
		return user;
	}

	public LoginAttempt findByName(String username)
	{
		return super.findSingleBySingleParameter(LoginAttempt.FIND_BY_NAME, "username", username);
	}

	public JPAContainer<LoginAttempt> createVaadinContainer()
	{
		JPAContainer<LoginAttempt> container = super.createVaadinContainer();
		container.addNestedContainerProperty("user.username");
		
		return container;
	}

	public DateTime blockedUtil(String username)
	{
		LoginAttempt attempt = super.findSingleBySingleParameter(LoginAttempt.FIND_LAST_ATTEMPTS, "username", username);

		DateTime blockedUntil = new DateTime(attempt.getDateOfAttempt()).plusMinutes(5);
		
		return blockedUntil;
	}

	/**
	 * User can have no more than five failed login attempts (in a row) in any five minute period.
	 * @param username
	 * @return
	 */
	public boolean hasExceededAttempts(String username)
	{
		
		Query query = entityManager.createNamedQuery(LoginAttempt.FIND_FIVE_MINUTE_ATTEMPTS);
		query.setParameter("username", username);
		DateTime fiveMinutesAgo = new DateTime().minusMinutes(5);
		query.setParameter("fiveMinutesAgo", fiveMinutesAgo.toDate(), TemporalType.TIMESTAMP);
		@SuppressWarnings("unchecked")
		List<LoginAttempt> attempts = query.getResultList();
		
		int failedAttempts = 0;
		for (LoginAttempt attempt: attempts)
		{
			if (attempt.isSucceeded())
				break;
			failedAttempts++;
			
			if (failedAttempts > 4)
				break;
		}
		
		return (failedAttempts > 4);
	}

}
