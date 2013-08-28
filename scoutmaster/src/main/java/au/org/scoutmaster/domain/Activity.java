package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

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
	@NamedQuery(name = Activity.FIND_ALL, query = "SELECT activity FROM Activity activity"),
})

public class Activity extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	static public final String FIND_ALL = "Activity.findAll";

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
	private Date activityDate;
	
	/**
	 * A short description of the activity
	 */
	@NotBlank
	private String subject;
	
	/**
	 * The activities details. May contain html for markup.
	 */
	@NotBlank
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
