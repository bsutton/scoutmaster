package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotEmpty;

import au.com.vaadinutils.crud.CrudEntity;

/**
 * Used to describe an event such as a section meeting or a bbq.
 * 
 * @author bsutton
 *
 */

@Entity(name="Event")
@Table(name="Event")
@Access(AccessType.FIELD)

@NamedQueries(
{ @NamedQuery(name = Event.FIND_BETWEEN, query = "SELECT event FROM Event event WHERE event.eventStartDateTime >= :startDate and event.eventEndDateTime <= :endDate") })

public class Event extends BaseEntity implements CrudEntity
{
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_BETWEEN = "Event.findBetween";

	/**
	 * A short description of the event used when displaying a summary of the event.
	 */
	@NotEmpty
	private String subject;
	
	/**
	 * A detailed description of the event. This may contain html for formatting purposes.
	 */
	private String  details;

	/**
	 * If true then this event runs all day in which case the 'time' component of the event Start and End dates
	 * should be ignored.
	 */
	private Boolean allDayEvent = new Boolean(false);
	
	/**
	 * The start and and dates of this event
	 */
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date eventStartDateTime = new Date();
	
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date eventEndDateTime = new Date();
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private Address location = new Address();
	
	
	/**
	 * The list of contacts responsible for co-ordinating this event.
	 */
	@ManyToMany
	private List<Contact> coordinators = new ArrayList<>();

	@OneToMany
	private List<Document> documents = new ArrayList<>();
	
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
		return (allDayEvent == null ? false : allDayEvent);
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

	@Override
	public String getName()
	{
		return subject;
	}

	public List<Document> getDocuments()
	{
		return this.documents;
	}

	public void setDocuments(List<Document> documents)
	{
		this.documents = documents;
	}

	public Address getLocation()
	{
		return location;
	}

	public void setLocation(Address location)
	{
		this.location = location;
	}

	public List<Contact> getCoordinators()
	{
		return coordinators;
	}

	public void setCoordinators(List<Contact> coordinators)
	{
		this.coordinators = coordinators;
	}

	
	

}
