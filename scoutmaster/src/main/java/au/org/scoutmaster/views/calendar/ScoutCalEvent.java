package au.org.scoutmaster.views.calendar;

import java.util.Date;
import java.util.HashSet;

import au.org.scoutmaster.domain.Event;

import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEvent.EventChangeNotifier;

public class ScoutCalEvent implements CalendarEvent, EventChangeNotifier
{
	private static final long serialVersionUID = 1L;
	Event eventEntity;
	private final HashSet<EventChangeListener> listeners = new HashSet<>();

	public ScoutCalEvent(final Event eventEntity)
	{
		this.eventEntity = eventEntity;
	}

	public void updateEvent(final Event updatedEvent)
	{
		this.eventEntity = updatedEvent;
		fireEventChanged();
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
		// We use the colour name as part of the css style name
		// as this makes it easy to match the style to the colour.
		// But take out the leading # as css doesn't like it.
		return this.eventEntity.getColor().getCSS().substring(1);
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

	public void fireEventChanged()
	{
		for (final EventChangeListener listener : this.listeners)
		{
			listener.eventChange(new EventChangeEvent(this));
		}
	}

	@Override
	public void addEventChangeListener(final EventChangeListener listener)
	{
		this.listeners.add(listener);
	}

	@Override
	public void removeEventChangeListener(final EventChangeListener listener)
	{
		this.listeners.remove(listener);

	}

}