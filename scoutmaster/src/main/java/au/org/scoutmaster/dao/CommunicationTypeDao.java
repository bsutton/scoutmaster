package au.org.scoutmaster.dao;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.CommunicationType;
import au.org.scoutmaster.domain.CommunicationType_;

public class CommunicationTypeDao extends JpaBaseDao<CommunicationType, Long> implements Dao<CommunicationType, Long>
{

	public CommunicationTypeDao()
	{
		// inherit the default per request em.
	}

	
	public CommunicationType findByName(final String name)
	{
		return super.findSingleBySingleParameter(CommunicationType.FIND_BY_NAME, CommunicationType_.name, name);
	}

	@Override
	public JPAContainer<CommunicationType> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}
}
