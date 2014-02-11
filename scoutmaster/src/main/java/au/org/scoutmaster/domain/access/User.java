package au.org.scoutmaster.domain.access;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.util.PasswordHash;

@Entity
@Table(name="User")
@NamedQueries(
{
		@NamedQuery(name = User.FIND_BY_NAME, query = "SELECT user FROM User user WHERE user.username = :username"),
		@NamedQuery(name = User.FIND_BY_EMAIL, query = "SELECT user FROM User user WHERE user.emailAddress = :emailAddress"), })
public class User extends BaseEntity
{
	
	private static final long serialVersionUID = 1L;

	@Transient
	private static final Logger logger = Logger.getLogger(User.class);

	
	public static final String FIND_BY_NAME = "User.findByName";
	public static final String FIND_BY_EMAIL = "User.findByEmail";

	@NotBlank
	@Column(unique=true)
	private String username;

	/**
	 * A salted hash of the users password.
	 */
	@NotBlank
	private String saltedPassword;
	
	
	private String firstname;
	
	private String surname;
	
	/**
	 * The users email address used when they forget their password.
	 */
	@Email
	private String emailAddress;
	
	/**
	 * Allows us to disable a user account. When a user is disabled they can't
	 * log in.
	 */
	private Boolean enabled;
	/**
	 * Given users are linked to lots of data it will be hard to delete one. As
	 * such we just mark them as deleted and they will no longer be able to
	 * login and the user record will not show in the interface. When we do this
	 * we also need to mangle the username so it doesn't conflict with a
	 * potential new user that wants to use the same username.
	 */
	private Boolean deleted;
	
	
	/**
	 * Used as the 'sender' mobile when sending bulk mobiles. 
	 */
	private String senderMobile;
	
	/** 
	 * Email signature used by default when this user is sending an email
	 */
	@Size(max=1024)
	private String emailSignature;
	
	

	public String getSenderMobile()
	{
		return senderMobile;
	}

	public void setSenderMobile(String senderMobile)
	{
		this.senderMobile = senderMobile;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public Boolean getEnabled()
	{
		return enabled;
	}

	public void setEnabled(Boolean enabled)
	{
		this.enabled = enabled;
	}

	public Boolean getDeleted()
	{
		return deleted;
	}

	public void setDeleted(Boolean deleted)
	{
		this.deleted = deleted;
	}

	public List<Role> getBelongsTo()
	{
		return belongsTo;
	}

	public void setBelongsTo(List<Role> belongsTo)
	{
		this.belongsTo = belongsTo;
	}

	/**
	 * The set of roles the user undertakes.
	 */
	@ManyToMany
	private List<Role> belongsTo = new ArrayList<>();

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
	
	@Access(value = AccessType.PROPERTY)
	/**
	 * Takes a clear text password and hashes and salts it for storage.
	 * @param password
	 */
	public void setPassword(String password)
	{
		this.saltedPassword = generatePassword(password);
	}
	
	/*
	 * returns a hashed and salted version of the password 
	 */
	public String getPassword()
	{
		// You can't really get the password.
		return this.saltedPassword;
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
	
	public String getEmailAddress()
	{
		return emailAddress;
	}

	public String toString()
	{
		return username;
	}

	@Override
	public String getName()
	{
		return username;
	}

	public String getFirstname()
	{
		return firstname;
	}

	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

	public String getSurname()
	{
		return surname;
	}

	public void setSurname(String surname)
	{
		this.surname = surname;
	}
	
	public String getFullname()
	{
		return this.firstname + " " + this.surname;
	}

	public String getEmailSignature()
	{
		return emailSignature;
	}
}

