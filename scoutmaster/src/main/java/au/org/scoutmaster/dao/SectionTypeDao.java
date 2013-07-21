package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import au.org.scoutmaster.domain.SectionType;

public class SectionTypeDao extends JpaBaseDao<SectionType, Long> implements Dao<SectionType, Long>
{

	public SectionTypeDao()
	{
		// inherit the default per request em. 
	}
	
	public SectionTypeDao(EntityManager em)
	{
		super(em);
	}

	@Override
	public SectionType findById(Long id)
	{
		SectionType tag= entityManager.find(this.entityClass, id);
		return tag;
	}

	@Override
	public List<SectionType> findAll()
	{
		Query query = entityManager.createNamedQuery(SectionType.FIND_ALL);
		@SuppressWarnings("unchecked")
		List<SectionType> list = query.getResultList();
		return list;
	}
	public SectionType findByName(String name)
	{
		SectionType tag = null;
		Query query = entityManager.createNamedQuery(SectionType.FIND_BY_NAME);
		query.setParameter("name", name);
		@SuppressWarnings("unchecked")
		List<SectionType> tags = query.getResultList();
		if (tags.size() > 0)
			tag = tags.get(0);
		 
		return tag;
	}
}
