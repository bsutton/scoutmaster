package au.com.noojee.scouts.domain.access;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class SessionHistory
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	Date start;
	
	Date end;
	
	@ManyToOne
	User user;
}
