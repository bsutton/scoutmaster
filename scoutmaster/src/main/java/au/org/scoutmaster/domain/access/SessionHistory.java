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
 * When a user logs out we write a record of their login session and its stored here.
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
	Date start = new Date();
	
	@Temporal(value=TemporalType.TIMESTAMP)
	Date end = new Date();
	
	@ManyToOne(targetEntity=User.class)
	User user;

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return user.getUsername() + " " + start.toString() + " - " + end.toString();
	}
}
