package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import au.org.scoutmaster.domain.Tag;

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
	public Tag findById(Long id)
	{
		Tag tag= entityManager.find(this.entityClass, id);
		return tag;
	}

	@Override
	public List<Tag> findAll()
	{
		Query query = entityManager.createNamedQuery(Tag.FIND_ALL);
		@SuppressWarnings("unchecked")
		List<Tag> list = query.getResultList();
		return list;
	}
	public Tag findByName(String name)
	{
		Tag tag = null;
		Query query = entityManager.createNamedQuery(Tag.FIND_BY_NAME);
		query.setParameter("tagName", name);
		@SuppressWarnings("unchecked")
		List<Tag> tags = query.getResultList();
		if (tags.size() > 0)
			tag = tags.get(0);
		 
		return tag;
	}
}
