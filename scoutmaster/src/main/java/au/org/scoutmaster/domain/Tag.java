package au.org.scoutmaster.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.hibernate.validator.constraints.NotBlank;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

/**
 * Tags are used to group and identify certain attributes of a contact,
 * organisation, household or school
 *
 *
 *
 * @author bsutton
 *
 */
@Entity(name = "Tag")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "Tag")
@Access(AccessType.FIELD)
@NamedQueries(
{ @NamedQuery(name = Tag.FIND_BY_NAME, query = "SELECT tag FROM Tag tag WHERE tag.name = :tagName") })
@AutoProperty
public class Tag extends BaseEntity
{

	static public final String FIND_BY_NAME = "Tag.findByName";

	private static final long serialVersionUID = 1L;
	public static final String DESCRIPTION = "description";

	public static final String NAME = "name";

	@Column(unique = true, length = 30)
	@NotBlank
	@Size(max = 30)
	String name;

	@Column(length = 250)
	@Size(max = 250)
	@NotBlank
	String description;

	/**
	 * Indicates that this is a builtin tag and therefore it may not be deleted
	 */
	Boolean builtin = new Boolean(false);

	/*
	 * Non detachable tags are a special class of tags which are automatically
	 * assigned to an entity based on some other entities property. For instance
	 * a Contact which is a Youth Member would also be automatically assigned to
	 * the tag Youth Member. This is some what redundant but the idea is to make
	 * searching for entities by a tag all encompassing. i.e. all major
	 * attributes of an entity are cross referenced by a tag for the purposes of
	 * searching.
	 *
	 * Only builtin tags may be NON-detachable.
	 */
	Boolean detachable = new Boolean(true);

	@ManyToMany(mappedBy = "tags")
	@Property(policy = PojomaticPolicy.NONE)
	// stop pojomatic generating a circular reference.
	private Set<Contact> contacts = new HashSet<>();

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

	public Tag(final String name)
	{
		this.name = name;
		this.description = "";
	}

	public Tag(final String name, final String description)
	{
		this(name, description, false);
	}

	public Tag(String name, String description, Boolean builtin)
	{
		this.name = name;
		this.description = description;
		this.builtin = builtin;
	}

	public Boolean isTag(final String tagName)
	{
		return this.name.equals(tagName);
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public Boolean getBuiltin()
	{
		return this.builtin;
	}

	public void setBuiltin(final Boolean builtin)
	{
		this.builtin = builtin;
	}

	public Boolean getDetachable()
	{
		return this.detachable;
	}

	public void setDetachable(final Boolean detachable)
	{
		this.detachable = detachable;
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
	public boolean equals(final Object other)
	{
		return Pojomatic.equals(this, other);
	}

	@Override
	public String toString()
	{
		return this.name;
	}

	@Override
	public int hashCode()
	{
		return Pojomatic.hashCode(this);
	}

	public Set<Contact> getContacts()
	{
		return this.contacts;
	}

}
