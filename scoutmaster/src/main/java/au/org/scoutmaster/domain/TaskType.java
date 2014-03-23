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
@Entity(name="TaskType")
@Table(name="TaskType")
@Access(AccessType.FIELD)
@NamedQueries(
{
		@NamedQuery(name = TaskType.FIND_BY_NAME, query = "SELECT tasktype FROM TaskType tasktype where tasktype.name = :name"), })
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

	public TaskType(String name, String description)
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
