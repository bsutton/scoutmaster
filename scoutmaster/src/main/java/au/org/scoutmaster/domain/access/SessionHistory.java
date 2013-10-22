package au.org.scoutmaster.domain.access;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import au.org.scoutmaster.domain.BaseEntity;

/**
 * When a user logs out we write a record of their login session and its stored here.
 * 
 * @author bsutton
 *
 */
@Entity
@Table(name="SessionHistory")
public class SessionHistory  extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	Date start;
	
	Date end;
	
	@ManyToOne
	User user;

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return user.getUsername() + " " + start.toString() + " - " + end.toString();
	}
}
