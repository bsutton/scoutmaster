package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.joda.time.DateTime;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.org.scoutmaster.dao.access.UserDao;
import au.org.scoutmaster.domain.ForgottenPasswordReset;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.util.RandomString;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import com.vaadin.addon.jpacontainer.JPAContainer;

public class ForgottenPasswordResetDao extends JpaBaseDao<ForgottenPasswordReset, Long> implements Dao<ForgottenPasswordReset, Long>
{

	public ForgottenPasswordResetDao()
	{
		// inherit the default per request em. 
	}
	public ForgottenPasswordResetDao(EntityManager em)
	{
		super(em);
	}

	@Override
	public List<ForgottenPasswordReset> findAll()
	{
		return super.findAll(ForgottenPasswordReset.FIND_ALL);
	}
	
	@SuppressWarnings("unchecked")
	boolean hasExpired(String resetid)
	{
		boolean hasExpired = true;

		List<ForgottenPasswordReset> resultReset = null;
		EntityManager em = EntityManagerProvider.INSTANCE.getEntityManager();

		Query query = em.createNamedQuery("ForgottenPasswordReset.getByResetid");
		query.setParameter("resetid", resetid);
		resultReset = query.getResultList();
		if (!resultReset.isEmpty())
		{
			ForgottenPasswordReset row = resultReset.get(0);
			DateTime expires = row.getExpires();
			if (expires.isAfterNow())
				hasExpired = false;
		}
		return hasExpired;

	}
	public ForgottenPasswordReset createReset(String emailAddressValue)
	{
		Preconditions.checkArgument(emailAddressValue != null, "You must pass a valid email address.");
		UserDao userDao = new DaoFactory().getUserDao();
		User user = userDao.findByEmail(emailAddressValue);
		
		Preconditions.checkArgument(user != null, "The email address: " + emailAddressValue + " does not exist");
		RandomString rs = new RandomString(RandomString.Type.ALPHANUMERIC, 32);
		String resetid = rs.nextString();
		
		ForgottenPasswordReset reset = new ForgottenPasswordReset();
		DateTime now = new DateTime();
		now.plusDays(1);
		reset.setExpires(now);
		reset.setResetid(resetid);
		
		return reset;
	}
	@Override
	public JPAContainer<ForgottenPasswordReset> makeJPAContainer()
	{
		return super.makeJPAContainer(ForgottenPasswordReset.class);
	}

}
