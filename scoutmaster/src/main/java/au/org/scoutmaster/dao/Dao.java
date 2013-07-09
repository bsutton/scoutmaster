package au.org.scoutmaster.dao;

import java.util.List;

public interface Dao<E, K>
{
	void persist(E entity);
	
	public void merge(E entity);

	void remove(E entity);

	E findById(K id);
	
	List<E> findAll();
}