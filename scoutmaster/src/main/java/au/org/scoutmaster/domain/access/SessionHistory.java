package au.org.scoutmaster.domain.access;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import au.org.scoutmaster.domain.BaseEntity;

/**
 * When a user logs out or their session expires we write a record of their
 * login session and its stored here.
 *
 * @author bsutton
 *
 */
@Entity
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "SessionHistory")
@Access(AccessType.FIELD)
public class SessionHistory extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	@Temporal(value = TemporalType.TIMESTAMP)
	private Date start = new Date();

	@Temporal(value = TemporalType.TIMESTAMP)
	private Date end = new Date();

	@ManyToOne(targetEntity = User.class)
	private User user;

	public Date getStart()
	{
		return this.start;
	}

	public void setStart(final Date start)
	{
		this.start = start;
	}

	public Date getEnd()
	{
		return this.end;
	}

	public void setEnd(final Date end)
	{
		this.end = end;
	}

	public User getUser()
	{
		return this.user;
	}

	public void setUser(final User user)
	{
		this.user = user;
	}

	@Override
	public String getName()
	{
		return this.user.getUsername() + " " + this.start.toString() + " - " + this.end.toString();
	}
}
