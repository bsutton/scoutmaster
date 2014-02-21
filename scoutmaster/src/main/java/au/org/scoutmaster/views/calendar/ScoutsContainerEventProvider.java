package au.org.scoutmaster.views.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.EventDao;
import au.org.scoutmaster.domain.Event;
import au.org.scoutmaster.domain.Event_;
import au.org.scoutmaster.util.SMNotification;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventResize;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.MoveEvent;
import com.vaadin.ui.components.calendar.ContainerEventProvider;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEvent.EventChangeEvent;
import com.vaadin.ui.components.calendar.event.CalendarEvent.EventChangeListener;

final class ScoutsContainerEventProvider extends ContainerEventProvider  implements EventChangeListener
{
	private Logger logger = Logger.getLogger(ScoutsContainerEventProvider.class);

	private final JPAContainer<au.org.scoutmaster.domain.Event> container;

	private final List<EventChangeListener> eventChangeListeners = new LinkedList<CalendarEvent.EventChangeListener>();

	private Calendar calendar;
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	ScoutsContainerEventProvider(Calendar calendar)
	{
		super(new DaoFactory().getEventDao().createVaadinContainer());
		this.calendar = calendar;
		this.container = (JPAContainer<Event>) super.getContainerDataSource();

		this.setStartDateProperty(Event_.eventStartDateTime.getName());
		this.setEndDateProperty(Event_.eventEndDateTime.getName());
		this.setDescriptionProperty(Event_.details.getName());
		this.setCaptionProperty(Event_.subject.getName());

	}

	@Override
	public void addEvent(CalendarEvent event)
	{
		au.org.scoutmaster.domain.Event entity = new au.org.scoutmaster.domain.Event(event.getCaption(),
				event.getDescription(), event.getStart(), event.getEnd(), event.getStyleName());
		container.addEntity(entity);
	}

	@Override
	public void eventMove(MoveEvent event)
	{
		ScoutCalEvent calendarEvent = (ScoutCalEvent) event.getCalendarEvent();
		Event eventEntity = calendarEvent.getEntity();

		Date newStart = event.getNewStart();

		Date oldStart = eventEntity.getEventStartDateTime();
		eventEntity.setEventStartDateTime(newStart);

		long duration = eventEntity.getEventEndDateTime().getTime() - oldStart.getTime();

		Date newEndTime = new Date(newStart.getTime() + duration);
		eventEntity.setEventEndDateTime(newEndTime);
		eventChange(new EventChangeEvent(calendarEvent));
		try
		{
			EventDao daoEvent = new DaoFactory().getEventDao();
			eventEntity = daoEvent.merge(eventEntity);
			calendarEvent.eventEntity = eventEntity;
		}
		catch (Exception e)
		{
			logger.error(e, e);
			SMNotification.show(e, Type.ERROR_MESSAGE);
		}
	}

	@Override
	public void eventResize(EventResize event)
	{
		ScoutCalEvent calendarEvent = (ScoutCalEvent) event.getCalendarEvent();
		Event eventEntity = calendarEvent.getEntity();

		Date newEndTime = event.getNewEnd();
		Date newStartTime = event.getNewStart();
		eventEntity.setEventEndDateTime(newEndTime);
		eventEntity.setEventStartDateTime(newStartTime);
		try
		{
			EventDao daoEvent = new DaoFactory().getEventDao();
			eventEntity = daoEvent.merge(eventEntity);
			calendarEvent.eventEntity = eventEntity;
			eventChange(new EventChangeEvent(calendarEvent));
		}
		catch (Exception e)
		{
			logger.error(e, e);
			SMNotification.show(e, Type.ERROR_MESSAGE);
		}

	}

	@Override
	public List<CalendarEvent> getEvents(Date startDate, Date endDate)
	{
		EventDao daoEvent = new DaoFactory().getDao(EventDao.class);
		List<Event> entries = daoEvent.findBetween(startDate, endDate);

		// Wrap Events as CalendarEvents for the view
		ArrayList<CalendarEvent> arrayList = new ArrayList<CalendarEvent>();
		for (Event event : entries)
		{
			ScoutCalEvent scoutEvent = new ScoutCalEvent(event);
			scoutEvent.addEventChangeListener(this);
			arrayList.add(scoutEvent);

			// Inject the color style required by each event into the page
			Styles styles = Page.getCurrent().getStyles();

			// Inject the style. We Use the colour name as the css name (sans
			// the leading #
			styles.add(".v-calendar-event-" + event.getColor().getCSS().substring(1) + " { background-color:"
					+ event.getColor().getCSS() + "; }");
		}

		return arrayList;
	}

	@Override 
	public void eventChange(EventChangeEvent eventChangeEvent)
	{
		for (EventChangeListener listener : eventChangeListeners)
		{
			listener.eventChange(eventChangeEvent);
		}
		calendar.markAsDirty();
	}

	/**
	 * Over-ridden as the base implementation is broken.
	 */
	@Override
	public void addEventChangeListener(EventChangeListener listener)
	{
		if (!eventChangeListeners.contains(listener))
		{
			eventChangeListeners.add(listener);
		}
	}
}