package au.org.scoutmaster.views.calendar;

import java.util.Date;

import org.joda.time.DateTime;
import org.vaadin.peter.buttongroup.ButtonGroup;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.ui.components.calendar.ContainerEventProvider;
import com.vaadin.ui.components.calendar.event.CalendarEvent.EventChangeEvent;
import com.vaadin.ui.components.calendar.event.CalendarEvent.EventChangeListener;
import com.vaadin.ui.themes.Reindeer;

import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.GroupDao;
import au.org.scoutmaster.domain.Group;

/** A start view for navigating to the main view */
@Menu(display = "Month View", path = "Calendar")
public class CalendarView extends VerticalLayout
		implements View, au.org.scoutmaster.views.calendar.EventDetails.SaveEventListener, EventChangeListener
{
	public static final String NAME = "Calendar";

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private MyCalendar calendar;

	private Button dailyView;

	private Button weeklyView;

	private Button monthlyView;

	private ScoutCalEvent currentEvent;

	private EventDetails eventDetails;

	private boolean isPublic;

	public CalendarView()
	{
		setSizeFull();
		setMargin(true);

	}

	public void setPublic(final boolean isPublic)
	{
		this.isPublic = isPublic;

	}

	enum Interval
	{
		Monthly, Weekly, Daily
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		if (SMSession.INSTANCE.getGroup() == null)
		{
			// extract the group id from the paramter
			// expected URL format is:
			// https://www.scoutmaster.org.au/public#!PublicCalendar/group_id=1
			if (event.getParameters() != null)
			{
				String[] msgs = event.getParameters().split("=");
				if (msgs.length != 2)
					throw new IllegalArgumentException(
							"The Group ID must be passed in the form of: https://www.scoutmaster.org.au/public#!PublicCalendar/group_id=1");

				if (!msgs[0].equals("group_id"))
					throw new IllegalArgumentException(
							"The Group ID must be passed in the form of: https://www.scoutmaster.org.au/public#!PublicCalendar/group_id=1");

				int groupId = Integer.valueOf(msgs[1]);

				GroupDao daoGroup = new DaoFactory().getGroupDao();
				Group group = daoGroup.findById(groupId);
				if (group == null)
					throw new IllegalArgumentException("Unknown Group ID passed.");
				else
					SMSession.INSTANCE.setGroup(group);

			}
			else
				throw new IllegalArgumentException(
						"The Group ID must be passed in the form of: https://www.scoutmaster.org.au/public#!PublicCalendar/group_id=1");
		}
		final HorizontalLayout calendarLabel = buildTitleArea();
		this.addComponent(calendarLabel);
		calendarLabel.setWidth("100%");

		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		this.addComponent(horizontalLayout);
		setComponentAlignment(calendarLabel, Alignment.MIDDLE_CENTER);
		setExpandRatio(horizontalLayout, 1.0f);

		this.calendar = buildCalendar();

		if (this.isPublic)
		{
			this.calendar.setReadOnly(true);
		}

		final VerticalLayout calendarArea = new VerticalLayout();
		calendarArea.setSizeFull();

		calendarArea.addComponent(buildControlArea());
		calendarArea.addComponent(this.calendar);
		calendarArea.setExpandRatio(this.calendar, 1.0f);
		calendarArea.setSpacing(false);

		horizontalLayout.addComponent(calendarArea);
		buildEditor(horizontalLayout);
		horizontalLayout.setExpandRatio(calendarArea, 1.0f);

	}

	private void buildEditor(final HorizontalLayout horizontalLayout)
	{
		this.eventDetails = new EventDetails(this, this.isPublic);
		this.eventDetails.setWidth("400px");
		// eventDetails.setHeight("100%");
		horizontalLayout.addComponent(this.eventDetails);
	}

	private HorizontalLayout buildTitleArea()
	{
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");

		final Group ourGroup = SMSession.INSTANCE.getGroup();
		final Label calendarLabel = new Label("<b>" + ourGroup.getName() + " Calendar</b>");
		calendarLabel.setContentMode(ContentMode.HTML);
		layout.addComponent(calendarLabel);
		layout.setWidth(null);
		layout.setExpandRatio(calendarLabel, 1.0f);
		layout.setComponentAlignment(calendarLabel, Alignment.TOP_CENTER);

		if (!this.isPublic)
		{
			final Button newButton = new Button("New Event");
			newButton.addClickListener(new NewButtonListener());
			layout.addComponent(newButton);
			layout.setComponentAlignment(newButton, Alignment.MIDDLE_RIGHT);
		}
		return layout;
	}

	private MyCalendar buildCalendar()
	{
		final MyCalendar calendar = new MyCalendar();
		calendar.setTimeFormat(null);
		calendar.setWeeklyCaptionFormat("dd MMM yyyy");
		calendar.setStartDate(new Date());

		// Monday to Friday
		calendar.setFirstVisibleDayOfWeek(1);
		calendar.setLastVisibleDayOfWeek(7);
		calendar.setFirstVisibleHourOfDay(5);
		calendar.setLastVisibleHourOfDay(22);
		calendar.setSizeFull();

		// Customize the event provider for adding events
		// as entities
		final ContainerEventProvider cep = new ScoutsContainerEventProvider(calendar);
		// Set the container as the data source
		calendar.setEventProvider(cep);
		cep.addEventChangeListener(this);

		// added handler so we can edit an event by clicking it.
		calendar.setHandler(new EventClickHander());

		// Set the initial interval.
		new IntervalChangeListener(calendar, this).setInterval(Interval.Monthly);

		return calendar;
	}

	private AbstractLayout buildControlArea()
	{

		final HorizontalLayout intervalLayout = new HorizontalLayout();
		intervalLayout.setSpacing(true);

		this.dailyView = createIntervalButton(Interval.Daily);
		this.weeklyView = createIntervalButton(Interval.Weekly);
		this.monthlyView = createIntervalButton(Interval.Monthly);
		setActiveViewButton(this.monthlyView);
		final ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.addButton(this.monthlyView);
		buttonGroup.addButton(this.weeklyView);
		buttonGroup.addButton(this.dailyView);

		intervalLayout.addComponent(buttonGroup);

		final HorizontalLayout controlLayout = new HorizontalLayout();
		controlLayout.setWidth("100%");
		controlLayout.setMargin(new MarginInfo(false, false, true, false));
		final Button previous = new Button("Prev");
		previous.addClickListener(new ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event)
			{
				CalendarView.this.calendar.fireNavigationEvent(false);

			}
		});
		final Button next = new Button("Next");
		next.addClickListener(new ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event)
			{
				CalendarView.this.calendar.fireNavigationEvent(true);

			}
		});

		controlLayout.addComponent(previous);
		controlLayout.addComponent(intervalLayout);
		controlLayout.setComponentAlignment(intervalLayout, Alignment.MIDDLE_CENTER);
		controlLayout.addComponent(next);
		controlLayout.setExpandRatio(intervalLayout, 1.0f);

		return controlLayout;
	}

	void setActiveViewButton(final Button active)
	{
		this.dailyView.removeStyleName(Reindeer.BUTTON_DEFAULT);
		this.weeklyView.removeStyleName(Reindeer.BUTTON_DEFAULT);
		this.monthlyView.removeStyleName(Reindeer.BUTTON_DEFAULT);
		this.dailyView.markAsDirty();
		this.weeklyView.markAsDirty();
		this.monthlyView.markAsDirty();

		active.addStyleName(Reindeer.BUTTON_DEFAULT);
	}

	private Button createIntervalButton(final Interval interval)
	{
		final Button intervalButton = new Button(interval.name());
		intervalButton.setData(interval);
		intervalButton.addClickListener(new IntervalChangeListener(this.calendar, this));

		return intervalButton;
	}

	class NewButtonListener implements ClickListener
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(final ClickEvent event)
		{
			final au.org.scoutmaster.domain.Event calEvent = new au.org.scoutmaster.domain.Event();
			if (CalendarView.this.calendar.getStartDate().after(calEvent.getEventStartDateTime()))
			{
				final DateTime calFirstDate = new DateTime(CalendarView.this.calendar.getStartDate());
				calEvent.setEventStartDateTime(calFirstDate.withHourOfDay(12).toDate());
				calEvent.setEventEndDateTime(calFirstDate.withHourOfDay(14).toDate());
			}
			CalendarView.this.currentEvent = new ScoutCalEvent(calEvent);
			CalendarView.this.eventDetails.setEvent(calEvent, true);
		}
	}

	class EventClickHander implements EventClickHandler
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void eventClick(final EventClick event)
		{
			if (event != null)
			{
				CalendarView.this.currentEvent = (ScoutCalEvent) event.getCalendarEvent();
				CalendarView.this.eventDetails.setEvent(CalendarView.this.currentEvent.getEntity(), false);
			}
			else
			{
				CalendarView.this.currentEvent = null;
				CalendarView.this.eventDetails.setEvent(null, false);
			}
		}
	}

	@Override
	public void eventSaved(final au.org.scoutmaster.domain.Event event, final boolean newEvent)
	{
		if (newEvent)
		{
			CalendarView.this.calendar.addEvent(this.currentEvent);
		}
		else
		{
			this.currentEvent.updateEvent(event);
		}
	}

	@Override
	public void eventDeleted(final au.org.scoutmaster.domain.Event event)
	{
		this.currentEvent.updateEvent(event);

	}

	class MyCalendar extends Calendar
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void fireNavigationEvent(final boolean forward)
		{
			super.fireNavigationEvent(forward);
		}

	}

	/**
	 * If the events properties change we need to tell the event details to
	 * update.
	 */
	@Override
	public void eventChange(final EventChangeEvent eventChangeEvent)
	{
		final ScoutCalEvent changedEvent = (ScoutCalEvent) eventChangeEvent.getCalendarEvent();

		if (changedEvent.getEntity().getId().equals(this.currentEvent.getEntity().getId()))
		{
			this.eventDetails.setEvent(changedEvent.getEntity(), false);
		}
	}

}
