package au.org.scoutmaster.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Used to describe an event such as a section meeting or a bbq.
 * 
 * @author bsutton
 *
 */

@Entity(name="Event")
@Table(name="Event")
public class Event extends BaseEntity 
{
	private static final long serialVersionUID = 1L;

	/**
	 * A short description of the event used when displaying a summary of the event.
	 */
	private String subject;
	
	/**
	 * A detailed description of the event. This may contain html for formatting purposes.
	 */
	private String  details;

	/**
	 * If true then this event runs all day in which case the 'time' component of the event Start and End dates
	 * should be ignored.
	 */
	private Boolean allDayEvent;
	
	/**
	 * The start and and dates of this event
	 */
	private Date eventStartDateTime;
	private Date eventEndDateTime;
	
	
	/**
	 * The list of contacts responsible for co-ordinating this event.
	 */
	@ManyToMany
	private List<Contact> coordinators = new ArrayList<>();

	public Event()
	{
	}

	public Event(String subject, String details, Date start, Date end, String styleName)
	{
		this.setSubject(subject);
		this.setDetails(details);
		this.setEventStartDateTime(start);
		this.setEventEndDateTime(end);
	}

	public Boolean getAllDayEvent()
	{
		return allDayEvent;
	}

	public void setAllDayEvent(Boolean allDayEvent)
	{
		this.allDayEvent = allDayEvent;
	}

	public Date getEventStartDateTime()
	{
		return eventStartDateTime;
	}

	public void setEventStartDateTime(Date eventStartDateTime)
	{
		this.eventStartDateTime = eventStartDateTime;
	}

	public Date getEventEndDateTime()
	{
		return eventEndDateTime;
	}

	public void setEventEndDateTime(Date eventEndDateTime)
	{
		this.eventEndDateTime = eventEndDateTime;
	}

	public String getDetails()
	{
		return details;
	}

	public void setDetails(String details)
	{
		this.details = details;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

}
