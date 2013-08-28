package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

/**
 * A household is used to link a number of contacts which live at a single
 * locations
 * 
 * If a member belongs to a split household then a Household should be created
 * for each family grouping and the contact should be added to both.
 * 
 * @author bsutton
 * 
 */
@Entity(name="Household")
@Table(name="Household")
public class Household extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The name of the household. This will normally be the families lastname.
	 * In cases where the group has more than one household with the same
	 * lastname then the name can be augmented by appending the street name
	 * 
	 * e.g. Smith - Drive St.
	 */
	@NotBlank
	@Column(unique=true)
	private String name;

	/**
	 * The address of the household.
	 */
	@OneToOne
	private Address location;

	@OneToOne
	private Contact primaryContact;

	/**
	 * Members of the household and their relationship to the household.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	private final List<Relationship> members = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)
	private final List<Note> notes = new ArrayList<>();

	@ManyToMany
	private final List<Tag> tags = new ArrayList<>();

	/**
	 * List of interactions with this contact.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	private final List<Activity> activites = new ArrayList<>();

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Address getLocation()
	{
		return location;
	}

	public void setLocation(Address location)
	{
		this.location = location;
	}


}
