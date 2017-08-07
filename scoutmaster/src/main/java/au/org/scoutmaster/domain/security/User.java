package au.org.scoutmaster.domain.security;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.domain.ScoutGroup;
import au.org.scoutmaster.security.eSecurityRole;
import au.org.scoutmaster.util.PasswordHash;

@Entity
@Table(name = "User")
@NamedQueries(
{ @NamedQuery(name = User.FIND_BY_NAME, query = "SELECT user FROM User user WHERE user.username = :username"),
		@NamedQuery(name = User.FIND_BY_EMAIL, query = "SELECT user FROM User user WHERE user.emailAddress = :emailAddress"), })
public class User extends BaseEntity
{

	private static final long serialVersionUID = 1L;

	@Transient
	private static final Logger logger = LogManager.getLogger(User.class);

	public static final String FIND_BY_NAME = "User.findByName";
	public static final String FIND_BY_EMAIL = "User.findByEmail";

	/**
	 * Whilst a user doesn't have the @Tenant attribute we do store the user's
	 * Tenant ID (ScoutGroup_ID) on the user.
	 *
	 * We can't use JPA's @Tenant annotation on the user as during login we have
	 * to access every user until we know which user is logging and therefore
	 * which Tenant to configure JPA for.
	 *
	 * For Scoutmaster the Group entity acts as the Tenant.
	 */
	@JoinColumn(name = "ScoutGroup_ID")
	@ManyToOne(targetEntity = ScoutGroup.class)
	private ScoutGroup scoutGroup;

	@NotBlank
	@Column(unique = true)
	private String username;

	/**
	 * A salted hash of the users password.
	 */
	@NotBlank
	private String saltedPassword;

	private String firstname;

	private String lastname;

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
	@Size(max = 1024)
	private String emailSignature;

	/**
	 * The set of roles the user undertakes.
	 */
	@ManyToMany
	private List<SecurityRole> belongsTo = new ArrayList<>();

	public User()
	{
		this.enabled = true;
		this.deleted = false;

	}

	public User(final String username, final String password)
	{
		this.username = username;
		this.saltedPassword = generatePassword(password);
		this.enabled = true;
		this.deleted = false;

	}

	public ScoutGroup getGroup()
	{
		return this.scoutGroup;
	}

	public String getSenderMobile()
	{
		return this.senderMobile;
	}

	public void setSenderMobile(final String senderMobile)
	{
		this.senderMobile = senderMobile;
	}

	public String getUsername()
	{
		return this.username;
	}

	public void setUsername(final String username)
	{
		this.username = username;
	}

	public Boolean isEnabled()
	{
		return this.enabled;
	}

	public void setEnabled(final Boolean enabled)
	{
		this.enabled = enabled;
	}

	public Boolean getDeleted()
	{
		return this.deleted;
	}

	public void setDeleted(final Boolean deleted)
	{
		this.deleted = deleted;
	}

	public List<SecurityRole> getRoles()
	{
		return this.belongsTo;
	}

	public void setRoles(final List<SecurityRole> belongsTo)
	{
		this.belongsTo = belongsTo;
	}

	@Access(value = AccessType.PROPERTY)
	/**
	 * Takes a clear text password and hashes and salts it for storage.
	 *
	 * @param password
	 */
	public void setPassword(final String password)
	{
		this.saltedPassword = generatePassword(password);
	}

	/*
	 * returns a hashed and salted version of the password
	 */
	@Transient
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
	private String generatePassword(final String password)
	{
		String saltedPassword;
		try
		{
			saltedPassword = PasswordHash.createHash(password);
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException e)
		{
			User.logger.error(e, e);
			throw new RuntimeException(e);
		}
		return saltedPassword;

	}

	public boolean isValidPassword(final String password)
	{
		try
		{
			return PasswordHash.validatePassword(password, this.saltedPassword);
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException e)
		{
			User.logger.error(e, e);
			throw new RuntimeException(e);
		}
	}

	public void setEmailAddress(final String emailAddress)
	{
		this.emailAddress = emailAddress;

	}

	public String getEmailAddress()
	{
		return this.emailAddress;
	}

	@Override
	public String toString()
	{
		return this.username;
	}

	@Override
	public String getName()
	{
		return this.username;
	}

	public String getFirstname()
	{
		return this.firstname;
	}

	public void setFirstname(final String firstname)
	{
		this.firstname = firstname;
	}

	public String getLastname()
	{
		return this.lastname;
	}

	public void setLastname(final String lastname)
	{
		this.lastname = lastname;
	}

	public String getFullname()
	{
		return this.firstname + " " + this.lastname;
	}

	public String getEmailSignature()
	{
		return this.emailSignature;
	}

	public void setGroup(ScoutGroup group)
	{
		this.scoutGroup = group;

	}

	/**
	 * Returns true if the belongs to the given role
	 * 
	 * @param role
	 * @return
	 */
	public boolean hasRole(eSecurityRole role)
	{
		boolean hasRole = false;
		for (SecurityRole userRole : this.belongsTo)
		{
			if (userRole.getERole() == role)
			{
				hasRole = true;
				break;
			}
		}

		return hasRole;
	}
}
