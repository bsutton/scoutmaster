package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Activity are interactions with a Contact, Household or Organisation.
 *
 * @author bsutton
 *
 */
@Entity(name = "TaskStatus")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "TaskStatus")
@Access(AccessType.FIELD)
@NamedQueries(
{ @NamedQuery(name = TaskStatus.FIND_BY_NAME, query = "SELECT taskstatus FROM TaskStatus taskstatus where taskstatus.name = :name"), })
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

	public TaskStatus(final String name, final String description)
	{
		setName(name);
		setDescription(description);

	}

	@Override
	public String getName()
	{
		return this.name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
