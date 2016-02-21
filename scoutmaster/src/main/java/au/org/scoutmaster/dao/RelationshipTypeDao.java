package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.Query;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.RelationshipType;
import au.org.scoutmaster.domain.RelationshipType_;

public class RelationshipTypeDao extends JpaBaseDao<RelationshipType, Long>
{

	@Override
	public JPAContainer<RelationshipType> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

	public RelationshipType find(final String name, final RelationshipType.Type type)
	{
		final Query query = JpaBaseDao.getEntityManager().createNamedQuery(RelationshipType.FIND_BY_TYPE);
		query.setParameter(RelationshipType_.lhs.getName(), name);
		query.setParameter(RelationshipType_.lhsType.getName(), type);
		@SuppressWarnings("unchecked")
		List<RelationshipType> list = query.getResultList();
		RelationshipType result = list.size() == 0 ? null : list.get(0);
		return result;
	}

}
