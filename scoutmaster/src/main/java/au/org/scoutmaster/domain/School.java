package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * A school in the Scout Groups catchment area.
 * 
 * @author bsutton
 *
 */
@Entity
public class School extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The name of the school
	 */
	String name;
	
	/**
	 * A detailed description of the school. May be HTML.
	 */
	String description;
	
	/**
	 * A list of the schools locations (if it has multiple campus'
	 */
	@OneToMany(cascade = CascadeType.ALL)
	List<Address> location = new ArrayList<>();
	
	/**
	 * The schools principle
	 */
	@OneToOne
	Contact principle;
	
	/**
	 * Advertising contact
	 */
	@OneToOne
	Contact advertisingContact;
	
	
	/**
	 * The list of youth affiliated with our group (this includes prospects) that attend the school.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	List<Contact> youth = new ArrayList<>();

}