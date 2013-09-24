package au.org.scoutmaster.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.org.scoutmaster.domain.BaseEntity;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

public abstract class JpaBaseDao<E extends BaseEntity, K> implements Dao<E, K>
{
	protected Class<E> entityClass;

	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public JpaBaseDao()
	{
		this.entityManager = EntityManagerProvider.getEntityManager();
		Preconditions.checkNotNull(this.entityManager);
		
		// hack to get the derived classes Class type.
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		Preconditions.checkNotNull(genericSuperclass);
		this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
		Preconditions.checkNotNull(this.entityClass);
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
	
	protected E findSingleBySingleParameter(String queryName, SingularAttribute<E, String> paramName, String paramValue)
	{
		E entity = null;
		Query query = entityManager.createNamedQuery(queryName);
		query.setParameter(paramName.getName(), paramValue);
		@SuppressWarnings("unchecked")
		List<E> entities = query.getResultList();
		if (entities.size() > 0)
			entity = entities.get(0);
		return entity;
	}


	protected E findSingleBySingleParameter(String queryName, String paramName, String paramValue)
	{
		E entity = null;
		Query query = entityManager.createNamedQuery(queryName);
		query.setParameter(paramName, paramValue);
		@SuppressWarnings("unchecked")
		List<E> entities = query.getResultList();
		if (entities.size() > 0)
			entity = entities.get(0);
		return entity;
	}

	protected List<E> findListBySingleParameter(String queryName, String paramName, String paramValue)
	{
		Query query = entityManager.createNamedQuery(queryName);
		query.setParameter(paramName, paramValue);
		@SuppressWarnings("unchecked")
		List<E> entities = query.getResultList();
		return entities;
	}

	 public List<E> findAll()
	   {
	        String entityName = entityClass.getSimpleName();
	        Table annotation = entityClass.getAnnotation(Table.class);
	        String tableName;
	        if (annotation != null)
	        	tableName = annotation.name();
	        	else
	        		tableName = entityName;
	        
	        String qry = "select " + entityName + " from " + tableName + " "
	                + entityName;
	        return entityManager.createQuery(qry, entityClass).getResultList();

	    } 


	public void flush()
	{
		this.entityManager.flush();

	}

	public JPAContainer<E> makeJPAContainer(Class<E> clazz)
	{
		JPAContainer<E> container = JPAContainerFactory.makeBatchable(clazz, EntityManagerProvider.getEntityManager());
		return container;
	}
	
	abstract public JPAContainer<E> makeJPAContainer();

	public <V> E findOneByAttribute(Class<E> type, SingularAttribute<E, V> vKey, V value)
	{
		E ret = null;
		List<E> results = findByAttribute(type, vKey, value,null);
		if (results.size() > 0)
		{
			ret = results.get(0);
		}

		return ret;
	}

	public <V> List<E> findByAttribute(Class<E> type, SingularAttribute<E, V> vKey, V value,
			SingularAttribute<E, V> order)
	{

		EntityManager em = EntityManagerProvider.getEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();

		CriteriaQuery<E> criteria = builder.createQuery(type);

		Root<E> root = criteria.from(type);
		criteria.select(root);
		criteria.where(builder.equal(root.get(vKey), value));
		if (order != null)
		{
			criteria.orderBy(builder.asc(root.get(order)));
		}
		List<E> results = em.createQuery(criteria).getResultList();

		return results;

	}

}
