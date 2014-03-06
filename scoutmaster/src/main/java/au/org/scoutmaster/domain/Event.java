package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
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
import javax.validation.constraints.Size;

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
	@Size(max=255)
	private String subject;
	
	/**
	 * A detailed description of the event. This may contain html for formatting purposes.
	 */
	@Size(max=1024)
	private String  details;
	
	/**
	 * This event is a meeting of a section
	 */
	private Boolean sectionMeeting = true;
	
	/**
	 * If this is a section meeting then this is the specific section the meeting applies to.
	 */
	private SectionType sectionType;

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
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, targetEntity=Address.class)
	private Address location = new Address();
	
	
	/**
	 * The list of contacts responsible for co-ordinating this event.
	 */
	@ManyToMany(targetEntity=Contact.class)
	private List<Contact> coordinators = new ArrayList<>();

	@OneToMany(targetEntity=Document.class)
	private List<Document> documents = new ArrayList<>();

	/** 
	 * The Colour to display the event with.
	 */
	@Embedded
	private Color color = new Color();
	
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

	public Event(Event rhs)
	{
		this.subject = rhs.subject;
		this.allDayEvent = rhs.allDayEvent;
		this.coordinators = new ArrayList<>();
		this.coordinators.addAll(rhs.coordinators);
		this.details = rhs.details;
		this.documents = rhs.documents;
		this.eventEndDateTime = rhs.eventEndDateTime;
		this.eventStartDateTime = rhs.eventStartDateTime;
		this.location = rhs.location;
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

	public String toString()
	{
		return this.getEventStartDateTime() + " " + this.subject; 
	}

	public Color getColor()
	{
		return this.color;
	}

	public Boolean getSectionMeeting()
	{
		return sectionMeeting;
	}

	public void setSectionMeeting(Boolean sectionMeeting)
	{
		this.sectionMeeting = sectionMeeting;
	}

	public SectionType getSectionType()
	{
		return sectionType;
	}

	public void setSectionType(SectionType sectionType)
	{
		this.sectionType = sectionType;
	}

}
