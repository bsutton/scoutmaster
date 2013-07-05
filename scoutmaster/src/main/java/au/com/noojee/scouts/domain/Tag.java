package au.com.noojee.scouts.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

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
{ @NamedQuery(name = "Tag.findAll", query = "SELECT tag FROM Tag tag"),
		@NamedQuery(name = "Tag.findMatching", query = "SELECT tag FROM Tag tag WHERE tag.name = :tagName") })
public class Tag
{
	/**
	 * Builtin-tags 
	 * 
	 * There are a number of built in tags which are used to identify
	 * important attributes.
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

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Date created = new Date();
	
	@Column(unique = true, length = 30)
	String name;

	@Column(length = 250)
	String description;

	@ManyToMany
	private List<Contact> contacts = new ArrayList<>();

	@ManyToMany
	private List<School> schools = new ArrayList<>();

	@ManyToMany
	private List<Organisation> organisations = new ArrayList<>();

	@ManyToMany
	private List<Household> households = new ArrayList<>();


	public Tag()
	{

	}

	public Tag(String name, String description)
	{
		this.name = name;
		this.description = description;
	}

	public void addContact(Contact contact)
	{
		this.contacts.add(contact);
	}

	public boolean isTag(String tagName)
	{
		return this.name.equals(tagName);
	}

	public String toString()
	{
		return this.name;
	}

	static public Tag findTag(String tagName)
	{
		Tag tag = null;
		JPAContainer<Tag> tags = JPAContainerFactory.make(Tag.class, "scouts");
		EntityProvider<Tag> ep = tags.getEntityProvider();
		EntityManager em = ep.getEntityManager();

		try
		{
			Query query = em.createNamedQuery("Tag.findMatching");
			query.setParameter("tagName", tagName);
			@SuppressWarnings("unchecked")
			List<Tag> tagList = query.getResultList();
			if (tagList.size() == 1)
				tag = tagList.get(0);
			if (tagList.size() > 1)
				throw new NonUniqueResultException("Found more than one tag for tagName=" + tagName);
		}
		finally
		{
			if (em != null)
				em.close();
		}

		return tag;
	}



	public Date getCreated()
	{
		return created;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public List<Contact> getContacts()
	{
		return contacts;
	}

	public List<School> getSchools()
	{
		return schools;
	}

	public List<Organisation> getOrganisations()
	{
		return organisations;
	}

	public List<Household> getHouseholds()
	{
		return households;
	}

}
