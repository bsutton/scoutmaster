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
@Entity(name = "CommunicationType")
@Table(name = "CommunicationType")
@Access(AccessType.FIELD)
@NamedQueries(
		{ @NamedQuery(name = CommunicationType.FIND_BY_NAME, query = "SELECT communicationtype FROM CommunicationType communicationtype where communicationtype.name = :name"), })
public class CommunicationType extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	static public final String FIND_BY_NAME = "CommunicationType.findByName";

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

	public CommunicationType()
	{

	}

	public CommunicationType(final String name, final String description)
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
