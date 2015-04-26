package au.org.scoutmaster.dao;

import javax.persistence.EntityManager;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.access.SessionHistory;

import com.vaadin.addon.jpacontainer.JPAContainer;


public class SessionHistoryDao extends JpaBaseDao<SessionHistory, Long> implements Dao<SessionHistory, Long>
{

	public SessionHistoryDao()
	{
		// inherit the default per request em.
	}

	public SessionHistoryDao(final EntityManager em)
	{
		super(em);
	}

	@Override
	public JPAContainer<SessionHistory> createVaadinContainer()
	{
		final JPAContainer<SessionHistory> container = super.createVaadinContainer();
		container.addNestedContainerProperty("user.username");
		
		return container;

	}
}
