package au.org.scoutmaster.domain;

import javax.persistence.Entity;

/**
 * Activity are interactions with a Contact, Household or Organisation.
 * 
 * @author bsutton
 *
 */
@Entity
public class ActivityType  extends BaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * The name of the activity type
	 */
	String name;
	
	/**
	 * A description of the Activity Type
	 */
	String description;
	
	public ActivityType()
	{
		
	}
	public ActivityType(String name, String description)
	{
		this.name = name;
		this.description = description;
				
	}

	static public void dbInit()
	{
		// Add standard activity types
		
		new ActivityType("Meeting", "");
		new ActivityType("Phone Call", "");
		new ActivityType("Email", "Email sent.");
		new ActivityType("Text Message (SMS)", "Text Message (SMS) sent.");
		new ActivityType("Bulk Email", "Bulk Email sent.");
		new ActivityType("Bulk Text Message (SMS)", "Bulk Text Message (SMS) sent.");
	}
	

}
