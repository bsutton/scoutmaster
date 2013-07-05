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
 * A household is used to link a number of contacts which live at a single locations 
 * 
 * If a member belongs to a split household then a Household should be created for each
 * family grouping and the contact should be added to both.
 * 
 * @author bsutton
 *
 */
@Entity
public class Household
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * The name of the household.
	 * This will normally be the families lastname. In cases where the group has more than
	 * one household with the same lastname then the name can be augmented by appending the street name
	 * 
	 * e.g.
	 * Smith - Drive St.
	 */
	private String name;
	
	/**
	 * The address of the household.
	 */
	@OneToOne
	private Address location;
	
	@OneToOne
	private Contact primaryContact;
	
	/**
	 * Members of the household and their relationship
	 * to the household.
	 */
	@OneToMany
	private List<Relationship> members = new ArrayList<>();
	
	@OneToMany
	private List<Note> notes = new ArrayList<>();
	
	@ManyToMany
	private List<Tag> tags = new ArrayList<>();
	
	/**
	 * List of interactions with this contact.
	 */
	@OneToMany
	private List<Activity> activites = new ArrayList<>();

}
