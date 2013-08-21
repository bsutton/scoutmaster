package au.org.scoutmaster.domain.access;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.NotBlank;

import au.org.scoutmaster.dao.access.UserDao;
import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.filter.EntityManagerProvider;
import au.org.scoutmaster.util.PasswordHash;

@Entity
@NamedQueries(
{
		@NamedQuery(name = User.FIND_ALL, query = "SELECT user FROM User user"),
		@NamedQuery(name = User.FIND_BY_NAME, query = "SELECT user FROM User user WHERE user.username = :username"),
		@NamedQuery(name = User.FIND_BY_EMAIL, query = "SELECT user FROM User user WHERE user.emailAddress = :emailAddress"), })
public class User extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	@Transient
	private static final Logger logger = Logger.getLogger(User.class);

	public String getEmailAddress()
	{
		return emailAddress;
	}

	public static final String FIND_ALL = "User.findAll";
	public static final String FIND_BY_NAME = "User.findByName";
	public static final String FIND_BY_EMAIL = "User.findByEmail";

	@NotBlank
	String username;

	/**
	 * A salted hash of the users password.
	 */
	String saltedPassword;

	/**
	 * The users email address used when they forget their password.
	 */
	private String emailAddress;

	/**
	 * Allows us to disable a user account. When a user is disabled they can't
	 * log in.
	 */
	Boolean enabled;
	/**
	 * Given users are linked to lots of data it will be hard to delete one. As
	 * such we just mark them as deleted and they will no longer be able to
	 * login and the user record will not show in the interface. When we do this
	 * we also need to mangle the username so it doesn't conflict with a
	 * potential new user that wants to use the same username.
	 */
	Boolean deleted;

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

	public void updatePassword(String username, String password)
	{
		UserDao daoUser = new UserDao();
		User user = daoUser.findByName(username);
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

	public boolean isValidPassword(String password)
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

	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;

	}
}
