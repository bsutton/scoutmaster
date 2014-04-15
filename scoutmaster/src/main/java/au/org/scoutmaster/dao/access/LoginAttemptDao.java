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

	public LoginAttemptDao(final EntityManager em)
	{
		super(em);
	}

	@Override
	public LoginAttempt findById(final Long id)
	{
		final LoginAttempt user = this.entityManager.find(this.entityClass, id);
		return user;
	}

	public LoginAttempt findByName(final String username)
	{
		return super.findSingleBySingleParameter(LoginAttempt.FIND_BY_NAME, "username", username);
	}

	@Override
	public JPAContainer<LoginAttempt> createVaadinContainer()
	{
		final JPAContainer<LoginAttempt> container = super.createVaadinContainer();
		container.addNestedContainerProperty("user.username");

		return container;
	}

	public DateTime blockedUtil(final String username)
	{
		final LoginAttempt attempt = super.findSingleBySingleParameter(LoginAttempt.FIND_LAST_ATTEMPTS, "username",
				username);

		final DateTime blockedUntil = new DateTime(attempt.getDateOfAttempt()).plusMinutes(5);

		return blockedUntil;
	}

	/**
	 * User can have no more than five failed login attempts (in a row) in any
	 * five minute period.
	 * 
	 * @param username
	 * @return
	 */
	public boolean hasExceededAttempts(final String username)
	{

		final Query query = this.entityManager.createNamedQuery(LoginAttempt.FIND_FIVE_MINUTE_ATTEMPTS);
		query.setParameter("username", username);
		final DateTime fiveMinutesAgo = new DateTime().minusMinutes(5);
		query.setParameter("fiveMinutesAgo", fiveMinutesAgo.toDate(), TemporalType.TIMESTAMP);
		@SuppressWarnings("unchecked")
		final List<LoginAttempt> attempts = query.getResultList();

		int failedAttempts = 0;
		for (final LoginAttempt attempt : attempts)
		{
			if (attempt.isSucceeded())
			{
				break;
			}
			failedAttempts++;

			if (failedAttempts > 4)
			{
				break;
			}
		}

		return failedAttempts > 4;
	}

}
