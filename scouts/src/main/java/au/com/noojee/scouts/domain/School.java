package au.com.noojee.scouts.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * A school in the Scout Groups catchment area.
 * 
 * @author bsutton
 *
 */
@Entity
public class School
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

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
	@OneToMany
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
	@OneToMany
	List<Contact> youth = new ArrayList<>();

}
