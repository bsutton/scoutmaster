package au.com.noojee.scouts.domain;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Used to log a variety of activities
 * 
 * @author bsutton
 *
 */
@Entity
public class Activity
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * The type of activity.
	 */
	@ManyToOne
	ActivityType type;
	
	@ManyToOne
	Contact addedBy;
	
	@ManyToOne
	Contact withContact;
	
	/**
	 * The date/time the activity occurred.
	 */
	Date activityDate;
	
	/**
	 * A short description of the activity
	 */
	String subject;
	
	/**
	 * The activities details. May contain html for markup.
	 */
	String details;
	
	
}
