package au.org.scoutmaster.views.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

final class ScoutsContainerEventProvider extends ContainerEventProvider implements EventChangeListener
{
	private final Logger logger = LogManager.getLogger(ScoutsContainerEventProvider.class);

	private final JPAContainer<au.org.scoutmaster.domain.Event> container;

	private final List<EventChangeListener> eventChangeListeners = new LinkedList<CalendarEvent.EventChangeListener>();

	private final Calendar calendar;
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	ScoutsContainerEventProvider(final Calendar calendar)
	{
		super(new DaoFactory().getEventDao().createVaadinContainer());
		this.calendar = calendar;

		this.container = (JPAContainer<Event>) super.getContainerDataSource();

		setStartDateProperty(Event_.eventStartDateTime.getName());
		setEndDateProperty(Event_.eventEndDateTime.getName());
		setDescriptionProperty(Event_.details.getName());
		setCaptionProperty(Event_.subject.getName());

	}

	@Override
	public void addEvent(final CalendarEvent event)
	{
		final au.org.scoutmaster.domain.Event entity = new au.org.scoutmaster.domain.Event(event.getCaption(),
				event.getDescription(), event.getStart(), event.getEnd(), event.getStyleName());
		this.container.addEntity(entity);
	}

	@Override
	public void eventMove(final MoveEvent event)
	{
		final ScoutCalEvent calendarEvent = (ScoutCalEvent) event.getCalendarEvent();
		Event eventEntity = calendarEvent.getEntity();

		final Date newStart = event.getNewStart();

		final Date oldStart = eventEntity.getEventStartDateTime();
		eventEntity.setEventStartDateTime(newStart);

		final long duration = eventEntity.getEventEndDateTime().getTime() - oldStart.getTime();

		final Date newEndTime = new Date(newStart.getTime() + duration);
		eventEntity.setEventEndDateTime(newEndTime);
		eventChange(new EventChangeEvent(calendarEvent));
		try
		{
			final EventDao daoEvent = new DaoFactory().getEventDao();
			eventEntity = daoEvent.merge(eventEntity);
			calendarEvent.eventEntity = eventEntity;
		}
		catch (final Exception e)
		{
			this.logger.error(e, e);
			SMNotification.show(e, Type.ERROR_MESSAGE);
		}
	}

	@Override
	public void eventResize(final EventResize event)
	{
		final ScoutCalEvent calendarEvent = (ScoutCalEvent) event.getCalendarEvent();
		Event eventEntity = calendarEvent.getEntity();

		final Date newEndTime = event.getNewEnd();
		final Date newStartTime = event.getNewStart();
		eventEntity.setEventEndDateTime(newEndTime);
		eventEntity.setEventStartDateTime(newStartTime);
		try
		{
			final EventDao daoEvent = new DaoFactory().getEventDao();
			eventEntity = daoEvent.merge(eventEntity);
			calendarEvent.eventEntity = eventEntity;
			eventChange(new EventChangeEvent(calendarEvent));
		}
		catch (final Exception e)
		{
			this.logger.error(e, e);
			SMNotification.show(e, Type.ERROR_MESSAGE);
		}

	}

	@Override
	public List<CalendarEvent> getEvents(final Date startDate, final Date endDate)
	{
		final EventDao daoEvent = new DaoFactory().getDao(EventDao.class);
		final List<Event> entries = daoEvent.findBetween(startDate, endDate);

		// Wrap Events as CalendarEvents for the view
		final ArrayList<CalendarEvent> arrayList = new ArrayList<CalendarEvent>();
		for (final Event event : entries)
		{
			final ScoutCalEvent scoutEvent = new ScoutCalEvent(event);
			scoutEvent.addEventChangeListener(this);
			arrayList.add(scoutEvent);

			if (event.getColor() != null)
			{
				// Inject the color style required by each event into the page
				final Styles styles = Page.getCurrent().getStyles();

				// Inject the style. We Use the colour name as the css name
				// (sans
				// the leading #
				styles.add(".v-calendar-event-" + event.getColor().getCSS().substring(1) + " { background-color:"
						+ event.getColor().getCSS() + "; }");
			}
		}

		return arrayList;
	}

	@Override
	public void eventChange(final EventChangeEvent eventChangeEvent)
	{
		for (final EventChangeListener listener : this.eventChangeListeners)
		{
			listener.eventChange(eventChangeEvent);
		}
		this.calendar.markAsDirty();
	}

	/**
	 * Over-ridden as the base implementation is broken.
	 */
	@Override
	public void addEventChangeListener(final EventChangeListener listener)
	{
		if (!this.eventChangeListeners.contains(listener))
		{
			this.eventChangeListeners.add(listener);
		}
	}
}