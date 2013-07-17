package au.org.scoutmaster.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import au.org.scoutmaster.filter.EntityManagerProvider;

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
{ 
	@NamedQuery(name = Tag.FIND_ALL, query = "SELECT tag FROM Tag tag"),
	@NamedQuery(name = Tag.FIND_BY_NAME, query = "SELECT tag FROM Tag tag WHERE tag.name = :tagName") 
}
)
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

	public void setName(String name)
	{
		this.name = name;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

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

	@Override
	public String toString()
	{
		return this.name;
	}

	static public Tag findTag(String tagName)
	{
		Tag tag = null;
		EntityManager em = EntityManagerProvider.INSTANCE.getEntityManager();

		Query query = em.createNamedQuery("Tag.findMatching");
		query.setParameter("tagName", tagName);
		@SuppressWarnings("unchecked")
		List<Tag> tagList = query.getResultList();
		if (tagList.size() == 1)
			tag = tagList.get(0);
		if (tagList.size() > 1)
			throw new NonUniqueResultException("Found more than one tag for tagName=" + tagName);

		return tag;
	}

	public String getName()
	{
		return this.name;
	}

	public String getDescription()
	{
		return this.description;
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


}
