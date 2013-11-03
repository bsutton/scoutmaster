package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.Phone;

import com.vaadin.addon.jpacontainer.JPAContainer;

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

	
	public boolean isEmpty(Phone phone)
	{
		return phone.getPhoneNo() == null || phone.getPhoneNo().trim().length() == 0;
	}
	public List<Phone> findByNo(String value)
	{
		return super.findListBySingleParameter(Phone.FIND_BY_NO, "phoneNo", value);
	}
	@Override
	public JPAContainer<Phone> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}
}
