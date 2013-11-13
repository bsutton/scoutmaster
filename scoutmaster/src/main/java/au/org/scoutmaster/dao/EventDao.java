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

	public EventDao(EntityManager em)
	{
		super(em);
	}

	@Override
	public JPAContainer<Event> createVaadinContainer()
	{
		JPAContainer<Event> container = super.createVaadinContainer();

		container.sort(new Object[]
		{ Event_.eventStartDateTime.getName() }, new boolean[]
		{ true });

		container.addNestedContainerProperty("location.street");
		container.addNestedContainerProperty("location.city");
		container.addNestedContainerProperty("location.postcode");
		container.addNestedContainerProperty("location.state");

		return container;
	}

	public List<Event> findBetween(Date startDate, Date endDate)
	{
		Query query = entityManager.createNamedQuery(Event.FIND_BETWEEN);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		@SuppressWarnings("unchecked")
		List<Event> results = query.getResultList();
		return results;

	}

	public boolean hasCoordinator(au.org.scoutmaster.domain.Event event, Contact contact)
	{
		return event.getCoordinators().contains(contact);
	}

	public void attachCoordinator(au.org.scoutmaster.domain.Event event, Contact contact)
	{
		event.getCoordinators().add(contact);
	}

	public void detachCoordinator(au.org.scoutmaster.domain.Event event, Contact contact)
	{
		event.getCoordinators().remove(contact);

	}
}
