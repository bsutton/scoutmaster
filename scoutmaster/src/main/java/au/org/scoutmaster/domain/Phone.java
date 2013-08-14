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
	 * The type of phone number.
	 */
	private PhoneType phoneType = PhoneType.FIXED;
	
	/**
	 * The location type of the phone.
	 */
	private PhoneLocationType locationType = PhoneLocationType.HOME;
	
	/**
	 * If true then this is the contacts primary phone no.
	 * A contact MUST have only one primary phone no.
	 */
	private Boolean primaryPhone = false;

	/**
	 * the phone no.
	 */
	private String phoneNo = "";
	
	public Phone()
	{
		
	}
	public Phone(String phoneNo)
	{
		this.phoneNo = phoneNo;
	}

	public PhoneLocationType getLocationType()
	{
		return locationType;
	}

	public void setLocationType(PhoneLocationType locationType)
	{
		this.locationType = locationType;
	}

	public Boolean getPrimaryPhone()
	{
		return primaryPhone;
	}

	public void setPrimaryPhone(Boolean primaryPhone)
	{
		this.primaryPhone = primaryPhone;
	}

	public void setPhoneNo(String phoneNo)
	{
		this.phoneNo = phoneNo;
	}


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
	
	public String toString()
	{
		return phoneNo;
	}
}
