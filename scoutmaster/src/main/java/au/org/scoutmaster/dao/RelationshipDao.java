package au.org.scoutmaster.dao;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.Relationship;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class RelationshipDao extends JpaBaseDao<Relationship, Long>
{

	@Override
	public JPAContainer<Relationship> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

}
