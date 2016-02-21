package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.Query;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Relationship;
import au.org.scoutmaster.domain.RelationshipType;
import au.org.scoutmaster.domain.Relationship_;

public class RelationshipDao extends JpaBaseDao<Relationship, Long>
{

	@Override
	public JPAContainer<Relationship> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

	public Relationship find(final Contact lhs, final RelationshipType relationshipType, final Contact rhs)
	{
		final Query query = JpaBaseDao.getEntityManager().createNamedQuery(Relationship.FIND);
		query.setParameter(Relationship_.lhs.getName(), lhs);
		query.setParameter(Relationship_.type.getName(), relationshipType);
		query.setParameter(Relationship_.rhs.getName(), rhs);
		@SuppressWarnings("unchecked")
		List<Relationship> list = query.getResultList();
		Relationship result = list.size() == 0 ? null : list.get(0);
		return result;
	}

}
