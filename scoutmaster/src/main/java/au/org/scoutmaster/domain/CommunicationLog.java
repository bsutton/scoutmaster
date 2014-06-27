package au.org.scoutmaster.domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotBlank;

import au.com.vaadinutils.crud.CrudEntity;
import au.org.scoutmaster.domain.access.User;

/**
 * Used to log a variety of activities
 *
 * @author bsutton
 *
 */
@Entity
@Table(name = "CommunicationLog")
@Access(AccessType.FIELD)
@NamedQueries(
		{})
public class CommunicationLog extends BaseEntity implements CrudEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The type of activity.
	 */
	@ManyToOne(targetEntity = CommunicationType.class)
	private CommunicationType type;

	@ManyToOne(targetEntity = User.class)
	private User addedBy;

	@ManyToOne(targetEntity = Contact.class)
	private Contact withContact;

	/**
	 * The date/time the activity occurred.
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
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
	@Column(name = "DETAILS", columnDefinition = "TEXT")
	private String details;

	public CommunicationType getType()
	{
		return this.type;
	}

	public void setType(final CommunicationType type)
	{
		this.type = type;
	}

	public User getAddedBy()
	{
		return this.addedBy;
	}

	public void setAddedBy(final User addedBy)
	{
		this.addedBy = addedBy;
	}

	public Contact getWithContact()
	{
		return this.withContact;
	}

	public void setWithContact(final Contact withContact)
	{
		this.withContact = withContact;
	}

	public Date getActivityDate()
	{
		return this.activityDate;
	}

	public void setActivityDate(final Date activityDate)
	{
		this.activityDate = activityDate;
	}

	public String getSubject()
	{
		return this.subject;
	}

	public void setSubject(final String subject)
	{
		this.subject = subject;
	}

	public String getDetails()
	{
		return this.details;
	}

	public void setDetails(final String details)
	{
		this.details = details;
	}

	@Override
	public String getName()
	{
		return this.withContact.getFullname() + " CommunicationLog:"
				+ this.subject.substring(0, Math.min(20, this.subject.length()));
	}

	@Override
	public String toString()
	{
		return getName();
	}

}
