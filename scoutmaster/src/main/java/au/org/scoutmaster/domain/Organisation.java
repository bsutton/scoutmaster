package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

/**
 * An organisation (company, government body) that the scout group interacts
 * with in some way.
 * 
 * There should also be one special organisation created for each system which
 * is the one that represents the Scout Group.
 * 
 * e.g. supplier of uniforms.
 * 
 * @author bsutton
 * 
 */
@Entity(name="Organisation")
@Table(name="Organisation")

@NamedQueries(
{
	@NamedQuery(name = Organisation.FIND_ALL, query = "SELECT organisation FROM Organisation organisation"),
	@NamedQuery(name = Organisation.FIND_OUR_SCOUT_GROUP, query = "SELECT organisation FROM Organisation organisation where organisation.isOurScoutGroup = true"),
})
public class Organisation extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL = "Organisation.findAll";
	public static final String FIND_OUR_SCOUT_GROUP = "Organisation.findOurScoutGroup";

	/**
	 * If true then this organisation represents our local scout group. This
	 * organisations should be created by the setup wizard.
	 * 
	 */
	private Boolean isOurScoutGroup;

	/**
	 * The name of the organisation
	 */
	@NotBlank
	@Column(unique=true)
	private String name;

	/**
	 * A description of the organisation and how the group interacts with it.
	 */
	private String description;

	/**
	 * The list of contacts at the organsiation that the group associates with.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	private List<Contact> contacts = new ArrayList<>();

	/**
	 * The location of the organisation.
	 */
	@OneToOne
	private Address location;

	@OneToOne
	private Phone primaryPhone;
	

	/**
	 * The list of tags used to describe the organisation.
	 */
	@ManyToMany
	private List<Tag> tags = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)
	private List<Note> notes = new ArrayList<>();

	/**
	 * List of interactions with this contact.
	 */
	@OneToMany
	private List<Activity> activites = new ArrayList<>();



	public Boolean isOurScoutGroup()
	{
		return isOurScoutGroup;
	}

	public void setOurScoutGroup(Boolean isOurScoutGroup)
	{
		this.isOurScoutGroup = isOurScoutGroup;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Phone getPrimaryPhone()
	{
		return primaryPhone;
	}

	public void setPrimaryPhone(Phone primaryPhone)
	{
		this.primaryPhone = primaryPhone;
	}

	public String getName()
	{
		return name;
	}

	public Address getLocation()
	{
		return location;
	}

	public void setName(String name)
	{
		this.name = name;

	}

	public void setLocation(Address location)
	{
		this.location = location;
	}

	public void addTag(Tag tag)
	{
		this.tags.add(tag);
	}

}
