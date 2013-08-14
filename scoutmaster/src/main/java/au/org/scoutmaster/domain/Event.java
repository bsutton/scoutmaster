package au.org.scoutmaster.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

/**
 * Used to describe an event such as a section meeting or a bbq.
 * 
 * @author bsutton
 *
 */

@Entity
public class Event extends BaseEntity 
{
	private static final long serialVersionUID = 1L;

	public Event()
	{
	}

	public Event(String subject, String details, Date start, Date end, String styleName)
	{
		this.subject = subject;
		this.details = details;
		this.eventStartDateTime = start;
		this.eventEndDateTime = end;
	}

	/**
	 * The date this event record was created.
	 */
	Date created;
	
	/**
	 * The date this record was last modified.
	 */
	Date lastModified;
	
	
	/**
	 * If true then this event runs all day in which case the 'time' component of the event Start and End dates
	 * should be ignored.
	 */
	Boolean allDayEvent;
	
	/**
	 * The start and and dates of this event
	 */
	Date eventStartDateTime;
	Date eventEndDateTime;
	
	/**
	 * A short desription of the event used when displaying a summary of the event.
	 */
	String subject;
	
	/**
	 * A length desription of the event. This may contain html for formatting purposes.
	 */
	String  details;
	
	/**
	 * The list of contacts responsible for co-ordinating this event.
	 */
	@ManyToMany
	List<Contact> coordinators = new ArrayList<>();
	
}
