package au.org.scoutmaster.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotBlank;

import au.org.scoutmaster.domain.access.User;

/**
 * Used to log a variety of activities
 * 
 * @author bsutton
 *
 */
@Entity
@Table(name="Activity")
@NamedQueries(
{
})

public class Activity extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The type of activity.
	 */
	@ManyToOne
	private ActivityType type;
	
	@ManyToOne
	private User addedBy;
	
	@ManyToOne
	private Contact withContact;
	
	/**
	 * The date/time the activity occurred.
	 */
//	@Converter(name="DateTime", converterClass=org.joda.time.contrib.eclipselink.DateTimeConverter.class)
//	@Convert(value="DateTime")
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date activityDate = new Date(); // DateTime.now();
	
	/**
	 * A short description of the activity
	 */
	@NotBlank
	private String subject;
	
	/**
	 * The activities details. May contain html for markup.
	 */
	@Lob
	@Column(name="DETAILS", columnDefinition="TEXT" )
	private String details;

	public ActivityType getType()
	{
		return type;
	}

	public void setType(ActivityType type)
	{
		this.type = type;
	}

	public User getAddedBy()
	{
		return addedBy;
	}

	public void setAddedBy(User addedBy)
	{
		this.addedBy = addedBy;
	}

	public Contact getWithContact()
	{
		return withContact;
	}

	public void setWithContact(Contact withContact)
	{
		this.withContact = withContact;
	}

	public Date getActivityDate()
	{
		return activityDate;
	}

	public void setActivityDate(Date activityDate)
	{
		this.activityDate = activityDate;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getDetails()
	{
		return details;
	}

	public void setDetails(String details)
	{
		this.details = details;
	}
	
	
}
