package au.org.scoutmaster.dao;

import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;

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
	
	public void flush()
	{
		this.entityManager.flush();
		
	}

}