package au.org.scoutmaster.dao;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.QualificationType;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class QualificationTypeDao extends JpaBaseDao<QualificationType, Long>
{

	@Override
	public JPAContainer<QualificationType> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

}
