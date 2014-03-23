package au.org.scoutmaster.dao;

import javax.persistence.EntityManager;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.CommunicationType;
import au.org.scoutmaster.domain.CommunicationType_;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class CommunicationTypeDao extends JpaBaseDao<CommunicationType, Long> implements Dao<CommunicationType, Long>
{

	public CommunicationTypeDao()
	{
		// inherit the default per request em. 
	}
	public CommunicationTypeDao(EntityManager em)
	{
		super(em);
	}

	
	public CommunicationType findByName(String name)
	{
		return super.findSingleBySingleParameter(CommunicationType.FIND_BY_NAME, CommunicationType_.name, name);
	}
	@Override
	public JPAContainer<CommunicationType> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}
}
