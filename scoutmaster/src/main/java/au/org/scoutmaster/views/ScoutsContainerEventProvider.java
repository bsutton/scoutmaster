package au.org.scoutmaster.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.EventDao;
import au.org.scoutmaster.domain.Event;
import au.org.scoutmaster.domain.Event_;
import au.org.scoutmaster.util.SMNotification;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventResize;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.MoveEvent;
import com.vaadin.ui.components.calendar.ContainerEventProvider;
import com.vaadin.ui.components.calendar.event.CalendarEvent;

final class ScoutsContainerEventProvider extends ContainerEventProvider
{
	private Logger logger = Logger.getLogger(ScoutsContainerEventProvider.class);

	private final JPAContainer<au.org.scoutmaster.domain.Event> container;
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	ScoutsContainerEventProvider()
	{
		super(new DaoFactory().getEventDao().createVaadinContainer());
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

		// Wrap work entries as CalendarEvents for the view
		ArrayList<CalendarEvent> arrayList = new ArrayList<CalendarEvent>();
		for (Event event : entries)
		{
			arrayList.add(new ScoutCalEvent(event));
		}

		return arrayList;
	}
	
	public class ScoutCalEvent implements CalendarEvent
	{
		private static final long serialVersionUID = 1L;
		private Event eventEntity;

		public ScoutCalEvent(Event eventEntity)
		{
			this.eventEntity = eventEntity;
		}

		@Override
		public Date getStart()
		{
			return this.eventEntity.getEventStartDateTime();
		}

		@Override
		public Date getEnd()
		{
			return this.eventEntity.getEventEndDateTime();
		}

		@Override
		public String getCaption()
		{
			return this.eventEntity.getSubject();
		}

		@Override
		public String getDescription()
		{
			return this.eventEntity.getDetails();
		}

		@Override
		public String getStyleName()
		{
			return null;
		}

		@Override
		public boolean isAllDay()
		{
			return this.eventEntity.getAllDayEvent();
		}

		public Event getEntity()
		{
			return this.eventEntity;
		}

	}


}