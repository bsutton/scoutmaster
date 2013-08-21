package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;

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
	public List<Phone> findAll()
	{
		return super.findAll(Phone.FIND_ALL);
	}

	public boolean isEmpty(Phone phone)
	{
		return phone.getPhoneNo() == null || phone.getPhoneNo().trim().length() == 0;
	}
	public List<Phone> findByNo(String value)
	{
		return super.findListBySingleParameter(Phone.FIND_BY_NO, "phoneNo", value);
	}
}
