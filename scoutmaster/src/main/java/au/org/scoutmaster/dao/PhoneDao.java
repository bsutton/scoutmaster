package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import au.org.scoutmaster.domain.Phone;

public class PhoneDao extends JpaBaseDao<Phone, Long> implements Dao<Phone, Long>
{

	public PhoneDao()
	{
		// inherit the default per request em. 
	}
	public PhoneDao(EntityManager em)
	{
		super(em);
	}

	@Override
	public Phone findById(Long id)
	{
		Phone Phone = entityManager.find(this.entityClass, id);
		return Phone;
	}

	@Override
	public List<Phone> findAll()
	{
		Query query = entityManager.createNamedQuery(Phone.FIND_ALL);
		@SuppressWarnings("unchecked")
		List<Phone> list = query.getResultList();
		return list;
	}
	public boolean isEmpty(Phone phone)
	{
		return phone.getPhoneNo() == null || phone.getPhoneNo().trim().length() == 0;
	}
}
