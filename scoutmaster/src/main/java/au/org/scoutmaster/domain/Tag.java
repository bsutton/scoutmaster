package au.org.scoutmaster.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

/**
 * Tags are used to group and identify certain attributes of a contact,
 * organisation, household or school
 * 
 * 
 * 
 * @author bsutton
 * 
 */
@Entity
@NamedQueries(
{ @NamedQuery(name = Tag.FIND_ALL, query = "SELECT tag FROM Tag tag"),
		@NamedQuery(name = Tag.FIND_BY_NAME, query = "SELECT tag FROM Tag tag WHERE tag.name = :tagName") })
@AutoProperty
public class Tag extends BaseEntity
{

	static public final String FIND_ALL = "Tag.findAll";
	static public final String FIND_BY_NAME = "Tag.findByName";

	private static final long serialVersionUID = 1L;
	public static final String DESCRIPTION = "description";
	public static final String NAME = "name";

	/**
	 * Builtin-tags
	 * 
	 * There are a number of built in tags which are used to identify important
	 * attributes.
	 * 
	 * Note tags should NOT duplicate GroupRoles
	 * 
	 * @author bsutton
	 * 
	 */
	enum BuiltInTag
	{
		PROSPECT, SUPPLIER
	};

	@Column(unique = true, length = 30)
	String name;


	@Column(length = 250)
	String description;

	// @ManyToMany
	// private final List<Contact> contacts = new ArrayList<>();
	//
	// @ManyToMany
	// private final List<School> schools = new ArrayList<>();
	//
	// @ManyToMany
	// private final List<Organisation> organisations = new ArrayList<>();
	//
	// @ManyToMany
	// private final List<Household> households = new ArrayList<>();

	public Tag()
	{

	}

	public Tag(String name)
	{
		this.name = name;
		this.description = "";
	}

	public Tag(String name, String description)
	{
		this.name = name;
		this.description = description;
	}

	// public void addContact(Contact contact)
	// {
	// this.contacts.add(contact);
	// }

	public boolean isTag(String tagName)
	{
		return this.name.equals(tagName);
	}

	
	public String getName()
	{
		return this.name;
	}

	public String getDescription()
	{
		return this.description;
	}
	
	
	public void setName(String name)
	{
		this.name = name;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}


	// public List<Contact> getContacts()
	// {
	// return this.contacts;
	// }
	//
	// public List<School> getSchools()
	// {
	// return this.schools;
	// }
	//
	// public List<Organisation> getOrganisations()
	// {
	// return this.organisations;
	// }
	//
	// public List<Household> getHouseholds()
	// {
	// return this.households;
	// }

	@Override
	public boolean equals(Object other)
	{
		return Pojomatic.equals(this, other);
	}

	@Override
	public String toString()
	{
		return Pojomatic.toString(this);
	}

	@Override
	public int hashCode()
	{
		return Pojomatic.hashCode(this);
	}

}
