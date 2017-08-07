package au.org.scoutmaster.dao;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.com.vaadinutils.dao.Path;
import au.org.scoutmaster.domain.CommunicationLog;
import au.org.scoutmaster.domain.CommunicationLog_;
import au.org.scoutmaster.domain.CommunicationType_;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.security.User_;

public class CommunicationLogDao extends JpaBaseDao<CommunicationLog, Long> implements Dao<CommunicationLog, Long>
{

	public CommunicationLogDao()
	{
		// inherit the default per request em.
	}

	

	@Override
	public JPAContainer<CommunicationLog> createVaadinContainer()
	{
		final JPAContainer<CommunicationLog> container = super.createVaadinContainer();
		container.addNestedContainerProperty(new Path(CommunicationLog_.withContact, Contact_.lastname).getName());
		container.addNestedContainerProperty(new Path(CommunicationLog_.withContact, Contact_.firstname).getName());
		container.addNestedContainerProperty(new Path(CommunicationLog_.addedBy, User_.username).getName());
		container.addNestedContainerProperty(new Path(CommunicationLog_.type, CommunicationType_.name).getName());

		return container;
	}
}
