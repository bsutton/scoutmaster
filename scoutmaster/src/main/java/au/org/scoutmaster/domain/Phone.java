package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Used to store a phone no.
 *
 * @author bsutton
 *
 */
@Embeddable
@Access(AccessType.FIELD)
@NamedQueries(
		{ @NamedQuery(name = Phone.FIND_BY_NO, query = "SELECT phone FROM Phone phone where phone.phoneNo = :phoneNo"), })
public class Phone
{
	public static final String FIND_BY_NO = "Phone.findByNo";

	/**
	 * The type of phone number.
	 */
	private PhoneType phoneType = PhoneType.MOBILE;

	/**
	 * If true then this is the contacts primary phone no. A contact MUST have
	 * only one primary phone no.
	 */
	private Boolean primaryPhone = false;

	/**
	 * the phone no.
	 */
	private String phoneNo = "";

	public Phone()
	{

	}

	public Phone(final String phoneNo)
	{
		// Strip spaces from the phone no.
		this.phoneNo = phoneNo.replaceAll("\\s", "");
	}

	public Boolean getPrimaryPhone()
	{
		return this.primaryPhone;
	}

	public void setPrimaryPhone(final Boolean primaryPhone)
	{
		this.primaryPhone = primaryPhone;
	}

	public void setPhoneNo(final String phoneNo)
	{
		this.phoneNo = phoneNo.replaceAll("\\s", "");
	}

	public PhoneType getPhoneType()
	{
		return this.phoneType;
	}

	public void setPhoneType(final PhoneType phoneType)
	{
		this.phoneType = phoneType;
	}

	public String getPhoneNo()
	{
		return this.phoneNo.replaceAll("\\s", "");
	}

	@Override
	public String toString()
	{
		return this.phoneNo;
	}

	public String getName()
	{
		return toString();
	}
}
