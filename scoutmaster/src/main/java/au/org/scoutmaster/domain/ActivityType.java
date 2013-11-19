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
@Entity(name="ActivityType")
@Table(name="ActivityType")
@Access(AccessType.FIELD)
@NamedQueries(
{
		@NamedQuery(name = ActivityType.FIND_BY_NAME, query = "SELECT activitytype FROM ActivityType activitytype where activitytype.name = :name"), })
public class ActivityType extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	static public final String FIND_BY_NAME = "ActivityType.findByName";

	public static final String MEETING = "Meeting";
	public static final String PHONE_CALL = "Phone Call";
	public static final String EMAIL = "Email";
	public static final String SMS = "Text Message (SMS)";
	public static final String BULK_EMAIL = "Bulk Email";
	public static final String BULK_SMS = "Bulk Text Message (SMS)";
	public static final String VISIT = "Visit";

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
	
	public String toString()
	{
		return name;
	}
}
