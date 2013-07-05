package au.com.noojee.scouts.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * An organisation (company, government body) that the scout group interacts with in some way.
 * 
 * There should also be one special organisation created for each system which is the one
 * that represents the Scout Group.
 * 
 * e.g. supplier of uniforms.
 * 
 * @author bsutton
 *
 */
@Entity
public class Organisation
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * If true then this organisation represents our local scout group.
	 * This organisations should be created by the setup wizard.
	 * 
	 */
	boolean isOurScoutGroup;
	
	/**
	 * The name of the organisation
	 */
	String name;
	
	/**
	 * A description of the organisation and how the group interacts with it.
	 */
	String description;
	
	/**
	 *  The list of contacts at the organsiation that the group associates with.
	 */
	@OneToMany
	List<Contact> contacts = new ArrayList<>();
	
	/**
	 * The location of the organisation.
	 */
	@OneToOne
	Address location;
	
	@OneToOne
	Phone primaryPhone;
	
	/**
	 * The list of tags used to describe the organisation.
	 */
	@ManyToMany
	List<Tag> tags = new ArrayList<>();
	
	@OneToMany
	private List<Note> notes = new ArrayList<>();
	
	/**
	 * List of interactions with this contact.
	 */
	@OneToMany
	private List<Activity> activites = new ArrayList<>();

	

}
