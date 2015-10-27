package au.org.scoutmaster.dao;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.QualificationType;

public class QualificationTypeDao extends JpaBaseDao<QualificationType, Long>
{

	@Override
	public JPAContainer<QualificationType> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

}
