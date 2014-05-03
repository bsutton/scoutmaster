package au.org.scoutmaster.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Event;
import au.org.scoutmaster.domain.Event_;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class EventDao extends JpaBaseDao<Event, Long> implements Dao<Event, Long>
{

	public EventDao()
	{
		// inherit the default per request em.
	}

	public EventDao(final EntityManager em)
	{
		super(em);
	}

	@Override
	public JPAContainer<Event> createVaadinContainer()
	{
		final JPAContainer<Event> container = super.createVaadinContainer();

		container.sort(new Object[]
		{ Event_.eventStartDateTime.getName() }, new boolean[]
		{ true });

		container.addNestedContainerProperty("location.street");
		container.addNestedContainerProperty("location.city");
		container.addNestedContainerProperty("location.postcode");
		container.addNestedContainerProperty("location.state");

		return container;
	}

	public List<Event> findBetween(final Date startDate, final Date endDate)
	{
		final Query query = this.entityManager.createNamedQuery(Event.FIND_BETWEEN);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		@SuppressWarnings("unchecked")
		final List<Event> results = query.getResultList();
		return results;

	}

	public boolean hasCoordinator(final au.org.scoutmaster.domain.Event event, final Contact contact)
	{
		return event.getCoordinators().contains(contact);
	}

	public void attachCoordinator(final au.org.scoutmaster.domain.Event event, final Contact contact)
	{
		event.getCoordinators().add(contact);
	}

	public void detachCoordinator(final au.org.scoutmaster.domain.Event event, final Contact contact)
	{
		event.getCoordinators().remove(contact);

	}
}
