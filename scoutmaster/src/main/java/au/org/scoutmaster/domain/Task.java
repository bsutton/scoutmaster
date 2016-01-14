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

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
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
@Table(name = "Task")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Access(AccessType.FIELD)
@NamedQueries(
{})
public class Task extends BaseEntity implements CrudEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The type of task.
	 */
	@ManyToOne(targetEntity = TaskType.class)
	private TaskType taskType;

	/**
	 * The user that added this task.
	 */
	@ManyToOne(targetEntity = User.class)
	private User addedBy;

	/**
	 * The current status of the task.
	 */
	@ManyToOne(targetEntity = TaskStatus.class)
	private TaskStatus taskStatus = new TaskStatus();
	/**
	 * This task is private to the user that added the task.
	 */
	private Boolean privateTask = new Boolean(true);

	@ManyToOne(targetEntity = Contact.class)
	private Contact withContact;

	/**
	 * The date/time task is due by
	 */
	@Temporal(value = TemporalType.DATE)
	private Date dueDate = new Date(); // DateTime.now();

	/**
	 * The date/time the task was completed on.
	 */
	@Temporal(value = TemporalType.DATE)
	private Date completionDate;

	/**
	 * A short description of the task
	 */
	@NotBlank
	private String subject;

	@Lob
	@Column(name = "DETAILS", columnDefinition = "TEXT")
	private String details;

	/**
	 * The section the task is applicable to. This is optional.
	 */
	@ManyToOne(targetEntity = Section.class)
	private Section section;

	public TaskType getType()
	{
		return this.taskType;
	}

	public void setType(final TaskType taskType)
	{
		this.taskType = taskType;
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

	public Date getTaskDate()
	{
		return this.dueDate;
	}

	public void setTaskDate(final Date activityDate)
	{
		this.dueDate = activityDate;
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
		return this.addedBy.getFullname() + " Task:" + this.subject.substring(0, Math.min(20, this.subject.length()));
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public Boolean getPrivateTask()
	{
		return this.privateTask;
	}

	public void setPrivateTask(final Boolean privateTask)
	{
		this.privateTask = privateTask;
	}

	public void setStatus(final TaskStatus taskStatus)
	{
		this.taskStatus = taskStatus;

	}

}
