package au.org.scoutmaster.dao;

import javax.persistence.EntityManager;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Tag;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class TagDao extends JpaBaseDao<Tag, Long> implements Dao<Tag, Long>
{

	public TagDao()
	{
		// inherit the default per request em. 
	}
	public TagDao(EntityManager em)
	{
		super(em);
	}

	public Tag findByName(String name)
	{
		return super.findSingleBySingleParameter(Tag.FIND_BY_NAME, "tagName", name);
	}
	
	public static Tag addTag(String name, String description)
	{
		EntityManager em = EntityManagerProvider.getEntityManager();
		Tag tag = new Tag(name, description);
		em.persist(tag);
	
		return tag;
	}

	@Override
	public JPAContainer<Tag> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}
	
	public void addContact(Tag tag, Contact contact)
	{
		tag.getContacts().add(contact);
		contact.getTags().add(tag);
		
	}
}
