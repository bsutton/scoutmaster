package au.org.scoutmaster.domain.access;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.scoutmaster.domain.BaseEntity;

/**
 * When a user logs out or their session expires we write a record of their login session and its stored here.
 * 
 * @author bsutton
 *
 */
@Entity
@Table(name="SessionHistory")
@Access(AccessType.FIELD)
public class SessionHistory  extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	@Temporal(value=TemporalType.TIMESTAMP)
	private Date start = new Date();
	
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date end = new Date();
	
	@ManyToOne(targetEntity=User.class)
	private User user;

	public Date getStart()
	{
		return start;
	}

	public void setStart(Date start)
	{
		this.start = start;
	}

	public Date getEnd()
	{
		return end;
	}

	public void setEnd(Date end)
	{
		this.end = end;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	@Override
	public String getName()
	{
		return user.getUsername() + " " + start.toString() + " - " + end.toString();
	}
}
