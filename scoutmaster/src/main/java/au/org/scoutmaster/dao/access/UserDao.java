package au.org.scoutmaster.dao.access;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import au.org.scoutmaster.dao.Dao;
import au.org.scoutmaster.dao.JpaBaseDao;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.filter.EntityManagerProvider;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class UserDao extends JpaBaseDao<User, Long> implements Dao<User, Long>
{

	public UserDao()
	{
		// inherit the default per request em.
	}

	public UserDao(EntityManager em)
	{
		super(em);
	}

	@Override
	public User findById(Long id)
	{
		User user = entityManager.find(this.entityClass, id);
		return user;
	}

	@Override
	public List<User> findAll()
	{
		Query query = entityManager.createNamedQuery(User.FIND_ALL);
		@SuppressWarnings("unchecked")
		List<User> list = query.getResultList();

		return list;
	}

	public User addUser(String username, String password)
	{
		User user = new User(username, password);
		this.persist(user);
		return user;

	}
	
	
	public void updatePassword(User user, String username, String password)
	{
		user.setPassword(password);

		EntityManager em = EntityManagerProvider.INSTANCE.getEntityManager();
		em.merge(user);
	}


	public User findByName(String username)
	{
		return super.findSingleBySingleParameter(User.FIND_BY_NAME, "username", username);
	}

	public User findByEmail(String emailAddressValue)
	{
		return super.findSingleBySingleParameter(User.FIND_BY_EMAIL, "emailAddress", emailAddressValue);
	}
	
	public JPAContainer<User> makeJPAContainer()
	{
		return super.makeJPAContainer(User.class);
	}

}