package au.org.scoutmaster.dao;

import java.util.List;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.Phone;

public class PhoneDao extends JpaBaseDao<Phone, Long> implements Dao<Phone, Long>
{

	public PhoneDao()
	{
		// inherit the default per request em.
	}

	

	public boolean isEmpty(final Phone phone)
	{
		return phone.getPhoneNo() == null || phone.getPhoneNo().trim().length() == 0;
	}

	public List<Phone> findByNo(final String value)
	{
		return super.findListBySingleParameter(Phone.FIND_BY_NO, "phoneNo", value);
	}

	@Override
	public JPAContainer<Phone> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}
}
