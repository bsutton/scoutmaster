package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
@Entity
public class Organisation extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * If true then this organisation represents our local scout group. This
	 * organisations should be created by the setup wizard.
	 * 
	 */
	private boolean isOurScoutGroup;

	/**
	 * The name of the organisation
	 */
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
