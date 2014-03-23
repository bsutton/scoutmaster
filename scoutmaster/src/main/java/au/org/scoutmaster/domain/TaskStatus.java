package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Activity are interactions with a Contact, Household or Organisation.
 * 
 * @author bsutton
 * 
 */
@Entity(name="TaskStatus")
@Table(name="TaskStatus")
@Access(AccessType.FIELD)
@NamedQueries(
{
		@NamedQuery(name = TaskStatus.FIND_BY_NAME, query = "SELECT taskstatus FROM TaskStatus taskstatus where taskstatus.name = :name"), })
public class TaskStatus extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	static public final String FIND_BY_NAME = "TaskStatus.findByName";

	public static final String NOT_STARTED = "Not Started";
	public static final String IN_PROGRESS = "In Progress";
	public static final String AWAITING_RESPONSE = "Awaiting Response";
	public static final String COMPLETE = "Complete";
	public static final String CANCELLED = "Cancelled";
	public static final String ONHOLD = "On Hold";
	public static final String AWAITING_FUNDING = "Awaiting Funding";

	/**
	 * The name of the activity type
	 */
	@NotBlank
	@Column(unique = true)
	private String name;

	/**
	 * A description of the Activity Type
	 */
	@NotBlank
	private String description;

	public TaskStatus()
	{

	}

	public TaskStatus(String name, String description)
	{
		this.setName(name);
		this.setDescription(description);

	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String toString()
	{
		return name;
	}
}
