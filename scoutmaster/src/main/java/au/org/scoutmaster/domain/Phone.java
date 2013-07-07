package au.org.scoutmaster.domain;

import javax.persistence.Entity;

/**
 * Used to store a phone no.
 * 
 * @author bsutton
 *
 */
@Entity
public class Phone extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * the phone no.
	 */
	String phoneNo;
	
	/**
	 * The type of phone number.
	 */
	PhoneType phoneType;
	
	/**
	 * The location type of the phone.
	 */
	PhoneLocationType locationType;
	
	/**
	 * If true then this is the contacts primary phone no.
	 * A contact MUST have only one primary phone no.
	 */
	boolean primaryPhone;
}
