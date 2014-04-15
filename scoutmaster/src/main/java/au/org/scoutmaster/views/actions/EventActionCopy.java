package au.org.scoutmaster.views.actions;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.CrudAction;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.EventDao;
import au.org.scoutmaster.domain.Event;

import com.vaadin.addon.jpacontainer.EntityItem;

public class EventActionCopy implements CrudAction<Event>
{
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isDefault()
	{
		return false;
	}

	@Override
	public void exec(final BaseCrudView<Event> crud, final EntityItem<Event> entity)
	{
		final EventDao eventDao = new DaoFactory().getEventDao();

		final Event copy = new Event(entity.getEntity());
		eventDao.persist(copy);
	}

	@Override
	public String toString()
	{
		return "Copy";
	}

}
