package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.joda.time.DateTime;

import au.org.scoutmaster.domain.access.User;

/**
 * When a user needs to reset their password we generate a random string which
 * is used to do the reset.
 * 
 * The random string is valid for 24 hours and we store it here.
 * 
 * @author bsutton
 * 
 */

@Entity(name="ForgottenPasswordReset")
@Table(name="ForgottenPasswordReset")
@Access(AccessType.FIELD)
@NamedQueries(
// We order the set just incase we get two identical random strings (but almost
// impossible)
{
		@NamedQuery(name = ForgottenPasswordReset.FIND_BY_RESET_ID, query = "SELECT forgotten FROM ForgottenPasswordReset forgotten where forgotten.resetid = :resetid order by forgotten.created desc"), })
public class ForgottenPasswordReset extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_RESET_ID = "ForgottenPasswordReset.findByResetId";

	/**
	 * The date the reset record expires and the user can no longer reset their
	 * password.
	 */
	private Date expires;

	@ManyToOne(targetEntity=User.class)
	private User userWithBadMemory;

	/**
	 * A 32 byte random string used in the reset url.
	 */
	private String resetid;

	public User getUserWithBadMemory()
	{
		return userWithBadMemory;
	}

	public void setUserWithBadMemory(User userWithBadMemory)
	{
		this.userWithBadMemory = userWithBadMemory;
	}

	public String getResetid()
	{
		return resetid;
	}

	public void setResetid(String resetid)
	{
		this.resetid = resetid;
	}

	public void setExpires(DateTime expires)
	{
		this.expires = new Date(expires.toDate().getTime());
	}

	public DateTime getExpires()
	{
		return new DateTime(expires);
	}

	@Override
	public String getName()
	{
		return userWithBadMemory.getUsername() + expires.toString();
	}

	
}
