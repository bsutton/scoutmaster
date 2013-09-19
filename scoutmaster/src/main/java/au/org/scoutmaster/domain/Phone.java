package au.org.scoutmaster.domain;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Used to store a phone no.
 * 
 * @author bsutton
 *
 */
@Entity(name="Phone")
@Table(name="Phone")
@NamedQueries(
{
	@NamedQuery(name = Phone.FIND_BY_NO, query = "SELECT phone FROM Phone phone where phone.phoneNo = :phoneNo"),
})

public class Phone extends BaseEntity
{
	private static final long serialVersionUID = 1L;


	public static final String FIND_BY_NO = "Phone.findByNo";

	/**
	 * The type of phone number.
	 */
	private PhoneType phoneType = PhoneType.MOBILE;
	
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
		// Strip spaces from the phone no.
		this.phoneNo = phoneNo.replaceAll("\\s","");
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
		this.phoneNo = phoneNo.replaceAll("\\s","");
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
		return phoneNo.replaceAll("\\s","");
	}
	
	public String toString()
	{
		return phoneNo;
	}
}
