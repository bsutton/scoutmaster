package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;

import au.com.vaadinutils.dao.EntityManagerProvider;
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

	@Override
	public List<Tag> findAll()
	{
		return super.findAll(Tag.FIND_ALL);
	}
	public Tag findByName(String name)
	{
		return super.findSingleBySingleParameter(Tag.FIND_BY_NAME, "tagName", name);
	}
	public static Tag addTag(String name, String description)
	{
		EntityManager em = EntityManagerProvider.INSTANCE.getEntityManager();
		Tag tag = new Tag(name, description);
		em.persist(tag);
	
		return tag;
	
	}
	@Override
	public JPAContainer<Tag> makeJPAContainer()
	{
		return super.makeJPAContainer(Tag.class);
	}
}
