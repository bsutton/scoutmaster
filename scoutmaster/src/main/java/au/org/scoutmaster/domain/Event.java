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

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.hibernate.validator.constraints.NotEmpty;

import au.com.vaadinutils.crud.CrudEntity;

/**
 * Used to describe an event such as a section meeting or a bbq.
 *
 * @author bsutton
 *
 */

@Entity(name = "Event")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "Event")
@Access(AccessType.FIELD)
@NamedQueries(
{ @NamedQuery(name = Event.FIND_BETWEEN, query = "SELECT event FROM Event event WHERE event.eventStartDateTime >= :startDate and event.eventEndDateTime <= :endDate") })
public class Event extends BaseEntity implements CrudEntity
{
	private static final long serialVersionUID = 1L;

	public static final String FIND_BETWEEN = "Event.findBetween";

	/**
	 * A short description of the event used when displaying a summary of the
	 * event.
	 */
	@NotEmpty
	@Size(max = 255)
	private String subject;

	/**
	 * A detailed description of the event. This may contain html for formatting
	 * purposes.
	 */
	@Size(max = 1024)
	private String details;

	/**
	 * This event is a meeting of a section
	 */
	private Boolean sectionMeeting = true;

	/**
	 * If this is a section meeting then this is the specific section the
	 * meeting applies to.
	 */
	private SectionType sectionType;

	/**
	 * If true then this event runs all day in which case the 'time' component
	 * of the event Start and End dates should be ignored.
	 */
	private Boolean allDayEvent = new Boolean(false);

	/**
	 * The start and and dates of this event
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date eventStartDateTime = new Date();

	@Temporal(value = TemporalType.TIMESTAMP)
	private Date eventEndDateTime = new Date();

	@OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = Address.class, cascade =
	{ CascadeType.MERGE })
	private Address location = new Address();

	/**
	 * The list of contacts responsible for co-ordinating this event.
	 */
	@ManyToMany(targetEntity = Contact.class)
	private List<Contact> coordinators = new ArrayList<>();

	@OneToMany(targetEntity = Document.class)
	private List<Document> documents = new ArrayList<>();

	/**
	 * The Colour to display the event with.
	 */
	@Embedded
	private Color color = new Color();

	public Event()
	{
	}

	public Event(final String subject, final String details, final Date start, final Date end, final String styleName)
	{
		setSubject(subject);
		setDetails(details);
		setEventStartDateTime(start);
		setEventEndDateTime(end);
	}

	public Event(final Event rhs)
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
		return this.allDayEvent == null ? false : this.allDayEvent;
	}

	public void setAllDayEvent(final Boolean allDayEvent)
	{
		this.allDayEvent = allDayEvent;
	}

	public Date getEventStartDateTime()
	{
		return this.eventStartDateTime;
	}

	public void setEventStartDateTime(final Date eventStartDateTime)
	{
		this.eventStartDateTime = eventStartDateTime;
	}

	public Date getEventEndDateTime()
	{
		return this.eventEndDateTime;
	}

	public void setEventEndDateTime(final Date eventEndDateTime)
	{
		this.eventEndDateTime = eventEndDateTime;
	}

	public String getDetails()
	{
		return this.details;
	}

	public void setDetails(final String details)
	{
		this.details = details;
	}

	public String getSubject()
	{
		return this.subject;
	}

	public void setSubject(final String subject)
	{
		this.subject = subject;
	}

	@Override
	public String getName()
	{
		return this.subject;
	}

	public List<Document> getDocuments()
	{
		return this.documents;
	}

	public void setDocuments(final List<Document> documents)
	{
		this.documents = documents;
	}

	public Address getLocation()
	{
		return this.location;
	}

	public void setLocation(final Address location)
	{
		this.location = location;
	}

	public List<Contact> getCoordinators()
	{
		return this.coordinators;
	}

	public void setCoordinators(final List<Contact> coordinators)
	{
		this.coordinators = coordinators;
	}

	@Override
	public String toString()
	{
		return getEventStartDateTime() + " " + this.subject;
	}

	public Color getColor()
	{
		return this.color;
	}

	public Boolean getSectionMeeting()
	{
		return this.sectionMeeting;
	}

	public void setSectionMeeting(final Boolean sectionMeeting)
	{
		this.sectionMeeting = sectionMeeting;
	}

	public SectionType getSectionType()
	{
		return this.sectionType;
	}

	public void setSectionType(final SectionType sectionType)
	{
		this.sectionType = sectionType;
	}

}
