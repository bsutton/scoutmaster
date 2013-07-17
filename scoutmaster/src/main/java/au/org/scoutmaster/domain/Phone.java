package au.org.scoutmaster.domain;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Used to store a phone no.
 * 
 * @author bsutton
 *
 */
@Entity
@NamedQueries(
{
	@NamedQuery(name = Phone.FIND_ALL, query = "SELECT phone FROM Phone phone"),
})

public class Phone extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	static public final String FIND_ALL = "Phone.findAll";

	/**
	 * the phone no.
	 */
	String phoneNo;
	
	/**
	 * The type of phone number.
	 */
	private PhoneType phoneType;
	
	/**
	 * The location type of the phone.
	 */
	PhoneLocationType locationType;
	
	/**
	 * If true then this is the contacts primary phone no.
	 * A contact MUST have only one primary phone no.
	 */
	boolean primaryPhone;

	public PhoneType getPhoneType()
	{
		return phoneType;
	}

	public void setPhoneType(PhoneType phoneType)
	{
		this.phoneType = phoneType;
	}

	public String getPhoneNo()
	{
		return phoneNo;
	}
}
