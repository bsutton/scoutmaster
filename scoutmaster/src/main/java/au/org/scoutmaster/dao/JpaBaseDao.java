package au.org.scoutmaster.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import au.org.scoutmaster.filter.EntityManagerProvider;

public abstract class JpaBaseDao<E, K> implements Dao<E, K>
{
	protected Class<E> entityClass;

	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public JpaBaseDao()
	{
		this.entityManager = EntityManagerProvider.INSTANCE.getEntityManager(); 
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
	}

	@SuppressWarnings("unchecked")
	public JpaBaseDao(EntityManager em)
	{
		this.entityManager = em; 
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
	}

	public void persist(E entity)
	{
		entityManager.persist(entity);
	}

	public E merge(E entity)
	{
		return entityManager.merge(entity);
	}


	public void remove(E entity)
	{
		entityManager.remove(entity);
	}

	public E findById(K id)
	{
		return entityManager.find(entityClass, id);
	}
	
	public E findByName(String queryName, String fieldName, String value)
	{
		E entity = null;
		Query query = entityManager.createNamedQuery(queryName);
		query.setParameter(fieldName, value);
		@SuppressWarnings("unchecked")
		List<E> entities = query.getResultList();
		if (entities.size() > 0)
			entity = entities.get(0);
		return entity;
	}

	
	public void flush()
	{
		this.entityManager.flush();
		
	}

}