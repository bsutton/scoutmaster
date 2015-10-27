package au.org.scoutmaster.dao.access;

import javax.persistence.EntityManager;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.Dao;
import au.org.scoutmaster.domain.access.User;

public class UserDao extends JpaBaseDao<User, Long> implements Dao<User, Long>
{

	public UserDao()
	{
		// inherit the default per request em.
	}

	

	@Override
	public User findById(final Long id)
	{
		final User user = JpaBaseDao.getEntityManager().find(this.entityClass, id);
		return user;
	}

	public User addUser(final String username, final String password)
	{
		final User user = new User(username, password);
		persist(user);
		return user;

	}

	public void updatePassword(final User user, final String password)
	{
		user.setPassword(password);

		final EntityManager em = EntityManagerProvider.getEntityManager();
		em.merge(user);
	}

	public User findByName(final String username)
	{
		return super.findSingleBySingleParameter(User.FIND_BY_NAME, "username", username);
	}

	public User findByEmail(final String emailAddressValue)
	{
		return super.findSingleBySingleParameter(User.FIND_BY_EMAIL, "emailAddress", emailAddressValue);
	}

	@Override
	public JPAContainer<User> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

}
