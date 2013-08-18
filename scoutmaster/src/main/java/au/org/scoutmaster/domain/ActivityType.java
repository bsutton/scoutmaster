package au.org.scoutmaster.domain;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Activity are interactions with a Contact, Household or Organisation.
 * 
 * @author bsutton
 * 
 */
@Entity
@NamedQueries(
{
		@NamedQuery(name = ActivityType.FIND_ALL, query = "SELECT activitytype FROM ActivityType activitytype"),
		@NamedQuery(name = ActivityType.FIND_BY_NAME, query = "SELECT activitytype FROM ActivityType activitytype where activitytype.name = :name"),
})
public class ActivityType extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	static public final String FIND_ALL = "ActivityType.findAll";
	static public final String FIND_BY_NAME = "ActivityType.findByName";

	/**
	 * The name of the activity type
	 */
	private String name;

	/**
	 * A description of the Activity Type
	 */
	private String description;

	public ActivityType()
	{

	}

	public ActivityType(String name, String description)
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
}
