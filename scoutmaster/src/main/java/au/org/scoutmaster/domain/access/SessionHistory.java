package au.org.scoutmaster.domain.access;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import au.org.scoutmaster.domain.BaseEntity;

@Entity
public class SessionHistory  extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	Date start;
	
	Date end;
	
	@ManyToOne
	User user;
}
