package au.com.noojee.scouts.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Used to store a phone no.
 * 
 * @author bsutton
 *
 */
@Entity
public class Phone
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

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
	boolean primary;
}
