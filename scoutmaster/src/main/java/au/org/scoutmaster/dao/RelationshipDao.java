package au.org.scoutmaster.dao;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.Relationship;

public class RelationshipDao extends JpaBaseDao<Relationship, Long>
{

	@Override
	public JPAContainer<Relationship> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

}
