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
@Entity(name = "TaskType")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "TaskType")
@Access(AccessType.FIELD)
@NamedQueries(
{ @NamedQuery(name = TaskType.FIND_BY_NAME, query = "SELECT tasktype FROM TaskType tasktype where tasktype.name = :name"), })
public class TaskType extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	static public final String FIND_BY_NAME = "TaskType.findByName";

	public static final String WORKING_BEE = "Working Bee";
	public static final String FUND_RAISING = "Fund Raising";
	public static final String CAPITALWORKS = "Capital Works";
	public static final String SECTIONAL = "Sectional";
	public static final String GENERAL = "General";
	public static final String MAINTENANCE = "Maintenance";
	public static final String COMMUNICATION = "Communication";

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

	public TaskType()
	{

	}

	public TaskType(final String name, final String description)
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
