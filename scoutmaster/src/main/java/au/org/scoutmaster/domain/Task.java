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
@Table(name="Task")
@Access(AccessType.FIELD)
@NamedQueries(
{
})

public class Task extends BaseEntity implements CrudEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The type of task.
	 */
	@ManyToOne(targetEntity=TaskType.class)
	private TaskType taskType;
	
	/**
	 * The user that added this task.
	 */
	@ManyToOne(targetEntity=User.class)
	private User addedBy;
	
	/**
	 * The current status of the task.
	 */
	@ManyToOne(targetEntity=TaskStatus.class)
	private TaskStatus taskStatus = new TaskStatus();
	/**
	 * This task is private to the user that added the task.
	 */
	private Boolean privateTask = new Boolean(true); 
	
	@ManyToOne(targetEntity=Contact.class)
	private Contact withContact;
	
	/**
	 * The date/time task is due by
	 */
	@Temporal(value=TemporalType.DATE)
	private Date dueDate = new Date(); // DateTime.now();

	/**
	 * The date/time the task was completed on.
	 */
	@Temporal(value=TemporalType.DATE)
	private Date completionDate;

	/**
	 * A short description of the task
	 */
	@NotBlank
	private String subject;
	
	@Lob
	@Column(name="DETAILS", columnDefinition="TEXT" )
	private String details;
	
	/**
	 * The section the task is applicable to. This is optional.
	 */
	@ManyToOne(targetEntity=Section.class)
	private Section section;

	

	
	public TaskType getType()
	{
		return taskType;
	}

	public void setType(TaskType taskType)
	{
		this.taskType = taskType;
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

	public Date getTaskDate()
	{
		return dueDate;
	}

	public void setTaskDate(Date activityDate)
	{
		this.dueDate = activityDate;
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

	@Override
	public String getName()
	{
		return addedBy.getFullname() + " Task:" + this.subject.substring(0, Math.min(20, this.subject.length()));
	}
	
	public String toString()
	{
		return getName();
	}

	public Boolean getPrivateTask()
	{
		return privateTask;
	}

	public void setPrivateTask(Boolean privateTask)
	{
		this.privateTask = privateTask;
	}

	public void setStatus(TaskStatus taskStatus)
	{
		this.taskStatus = taskStatus;
		
	}
	
}
