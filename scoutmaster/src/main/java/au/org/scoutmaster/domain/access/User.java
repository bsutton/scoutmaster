package au.org.scoutmaster.domain.access;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import au.org.scoutmaster.filter.EntityManagerProvider;
import au.org.scoutmaster.util.PasswordHash;

@Entity
@NamedQueries(
{ @NamedQuery(name = "User.findAll", query = "SELECT user FROM User user"),
		@NamedQuery(name = "User.findByName", query = "SELECT user FROM User user WHERE user.username = :username"), })
public class User
{
	@Transient
	private static final Logger logger = Logger.getLogger(User.class);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	Date created = new Date(new java.util.Date().getTime());

	String username;

	/**
	 * A salted hash of the users password.
	 */
	String saltedPassword;

	/**
	 * Allows us to disable a user account. When a user is disabled they can't
	 * log in.
	 */
	boolean enabled;
	/**
	 * Given users are linked to lots of data it will be hard to delete one. As
	 * such we just mark them as deleted and they will no longer be able to
	 * login and the user record will not show in the interface. When we do this
	 * we also need to mangle the username so it doesn't conflict with a
	 * potential new user that wants to use the same username.
	 */
	boolean deleted;

	/**
	 * The set of roles the user undertakes.
	 */
	@ManyToMany
	List<Role> belongsTo = new ArrayList<>();

	public User()
	{
		this.enabled = true;
		this.deleted = false;

	}

	public User(String username, String password)
	{
		this.username = username;
		this.saltedPassword = generatePassword(password);
		this.enabled = true;
		this.deleted = false;

	}

	@SuppressWarnings("unchecked")
	public static User findUser(String username)
	{
		User user = null;
		List<User> resultUsers = null;
		EntityManager em = EntityManagerProvider.INSTANCE.getEntityManager();

		Query query = em.createNamedQuery("User.findByName");
		query.setParameter("username", username);
		resultUsers = query.getResultList();

		if (!resultUsers.isEmpty())
		{
			if (resultUsers.size() != 1)
			{
				throw new IllegalStateException("There are two users with an identical name in the db: " + username);
			}
			else
			{
				user = resultUsers.get(0);
			}
		}
		return user;
	}

	public void updatePassword(String username, String password)
	{
		User user = findUser(username);

		user.saltedPassword = generatePassword(password);

		EntityManager em = EntityManagerProvider.INSTANCE.getEntityManager();
		em.merge(user);
	}

	/**
	 * Takes a password and generates a salted hash we can store in the db.
	 * 
	 * @param password
	 * @return
	 */
	private String generatePassword(String password)
	{
		String saltedPassword;
		try
		{
			saltedPassword = PasswordHash.createHash(password);
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException e)
		{
			logger.error(e, e);
			throw new RuntimeException(e);
		}
		return saltedPassword;

	}

	public boolean validatePassword(String password)
	{
		try
		{
			return PasswordHash.validatePassword(password, this.saltedPassword);
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException e)
		{
			logger.error(e, e);
			throw new RuntimeException(e);
		}
	}

	public static void addUser(String username, String password)
	{
		EntityManager em = EntityManagerProvider.INSTANCE.getEntityManager();

		User user = new User(username, password);
		em.persist(user);
		em.flush();
	}

}
