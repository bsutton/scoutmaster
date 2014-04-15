package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.joda.time.DateTime;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.access.UserDao;
import au.org.scoutmaster.domain.ForgottenPasswordReset;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.util.RandomString;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import com.vaadin.addon.jpacontainer.JPAContainer;

public class ForgottenPasswordResetDao extends JpaBaseDao<ForgottenPasswordReset, Long> implements
		Dao<ForgottenPasswordReset, Long>
{

	public ForgottenPasswordResetDao()
	{
		// inherit the default per request em.
	}

	public ForgottenPasswordResetDao(final EntityManager em)
	{
		super(em);
	}

	@SuppressWarnings("unchecked")
	boolean hasExpired(final String resetid)
	{
		boolean hasExpired = true;

		List<ForgottenPasswordReset> resultReset = null;
		final EntityManager em = EntityManagerProvider.getEntityManager();

		final Query query = em.createNamedQuery("ForgottenPasswordReset.getByResetid");
		query.setParameter("resetid", resetid);
		resultReset = query.getResultList();
		if (!resultReset.isEmpty())
		{
			final ForgottenPasswordReset row = resultReset.get(0);
			final DateTime expires = row.getExpires();
			if (expires.isAfterNow())
			{
				hasExpired = false;
			}
		}
		return hasExpired;

	}

	public ForgottenPasswordReset createReset(final String emailAddressValue)
	{
		Preconditions.checkArgument(emailAddressValue != null, "You must pass a valid email address.");
		final UserDao userDao = new DaoFactory().getUserDao();
		final User user = userDao.findByEmail(emailAddressValue);

		Preconditions.checkArgument(user != null, "The email address: " + emailAddressValue + " does not exist");
		final RandomString rs = new RandomString(RandomString.Type.ALPHANUMERIC, 32);
		final String resetid = rs.nextString();

		final ForgottenPasswordReset reset = new ForgottenPasswordReset();
		final DateTime now = new DateTime();
		now.plusDays(1);
		reset.setExpires(now);
		reset.setResetid(resetid);

		return reset;
	}

	@Override
	public JPAContainer<ForgottenPasswordReset> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

}
