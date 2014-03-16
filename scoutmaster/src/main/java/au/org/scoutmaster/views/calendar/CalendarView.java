package au.org.scoutmaster.views.calendar;

import java.util.Date;

import org.joda.time.DateTime;
import org.vaadin.peter.buttongroup.ButtonGroup;

import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.OrganisationDao;
import au.org.scoutmaster.domain.Organisation;

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

/** A start view for navigating to the main view */
@Menu(display = "Month View", path="Calendar")
public class CalendarView extends VerticalLayout implements View,
		au.org.scoutmaster.views.calendar.EventDetails.SaveEventListener, EventChangeListener
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

	public void setPublic(boolean isPublic)
	{
		this.isPublic = isPublic;

	}

	enum Interval
	{
		Monthly, Weekly, Daily
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		HorizontalLayout calendarLabel = buildTitleArea();
		this.addComponent(calendarLabel);
		calendarLabel.setWidth("100%");
		

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		this.addComponent(horizontalLayout);
		this.setComponentAlignment(calendarLabel, Alignment.MIDDLE_CENTER);
		this.setExpandRatio(horizontalLayout, 1.0f);

		this.calendar = buildCalendar();
		
		if (isPublic)
		{
			this.calendar.setReadOnly(true);
		}

		VerticalLayout calendarArea = new VerticalLayout();
		calendarArea.setSizeFull();

		calendarArea.addComponent(buildControlArea());
		calendarArea.addComponent(calendar);
		calendarArea.setExpandRatio(calendar, 1.0f);
		calendarArea.setSpacing(false);

		horizontalLayout.addComponent(calendarArea);
		buildEditor(horizontalLayout);
		horizontalLayout.setExpandRatio(calendarArea, 1.0f);

	}

	private void buildEditor(HorizontalLayout horizontalLayout)
	{
		eventDetails = new EventDetails(this, isPublic);
		eventDetails.setWidth("400px");
		// eventDetails.setHeight("100%");
		horizontalLayout.addComponent(eventDetails);
	}

	private HorizontalLayout buildTitleArea()
	{
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");

		OrganisationDao daoOrganisation = new DaoFactory().getOrganisationDao();
		Organisation ourGroup = daoOrganisation.findOurScoutGroup();
		Label calendarLabel = new Label("<b>" + ourGroup.getName() + " Calendar</b>");
		calendarLabel.setContentMode(ContentMode.HTML);
		layout.addComponent(calendarLabel);
		layout.setWidth(null);
		layout.setExpandRatio(calendarLabel, 1.0f);
		layout.setComponentAlignment(calendarLabel, Alignment.TOP_CENTER);

		if (!isPublic)
		{
			Button newButton = new Button("New Event");
			newButton.addClickListener(new NewButtonListener());
			layout.addComponent(newButton);
			layout.setComponentAlignment(newButton, Alignment.MIDDLE_RIGHT);
		}
		return layout;
	}

	private MyCalendar buildCalendar()
	{
		MyCalendar calendar = new MyCalendar();
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
		ContainerEventProvider cep = new ScoutsContainerEventProvider(calendar);
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

		HorizontalLayout intervalLayout = new HorizontalLayout();
		intervalLayout.setSpacing(true);

		dailyView = createIntervalButton(Interval.Daily);
		weeklyView = createIntervalButton(Interval.Weekly);
		monthlyView = createIntervalButton(Interval.Monthly);
		setActiveViewButton(monthlyView);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.addButton(monthlyView);
		buttonGroup.addButton(weeklyView);
		buttonGroup.addButton(dailyView);

		intervalLayout.addComponent(buttonGroup);

		HorizontalLayout controlLayout = new HorizontalLayout();
		controlLayout.setWidth("100%");
		controlLayout.setMargin(new MarginInfo(false, false, true, false));
		Button previous = new Button("Prev");
		previous.addClickListener(new ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				calendar.fireNavigationEvent(false);

			}
		});
		Button next = new Button("Next");
		next.addClickListener(new ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				calendar.fireNavigationEvent(true);

			}
		});

		controlLayout.addComponent(previous);
		controlLayout.addComponent(intervalLayout);
		controlLayout.setComponentAlignment(intervalLayout, Alignment.MIDDLE_CENTER);
		controlLayout.addComponent(next);
		controlLayout.setExpandRatio(intervalLayout, 1.0f);

		return controlLayout;
	}

	void setActiveViewButton(Button active)
	{
		dailyView.removeStyleName(Reindeer.BUTTON_DEFAULT);
		weeklyView.removeStyleName(Reindeer.BUTTON_DEFAULT);
		monthlyView.removeStyleName(Reindeer.BUTTON_DEFAULT);
		dailyView.markAsDirty();
		weeklyView.markAsDirty();
		monthlyView.markAsDirty();

		active.addStyleName(Reindeer.BUTTON_DEFAULT);
	}

	private Button createIntervalButton(Interval interval)
	{
		Button intervalButton = new Button(interval.name());
		intervalButton.setData(interval);
		intervalButton.addClickListener(new IntervalChangeListener(calendar, this));

		return intervalButton;
	}

	class NewButtonListener implements ClickListener
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event)
		{
			au.org.scoutmaster.domain.Event calEvent = new au.org.scoutmaster.domain.Event();
			if (CalendarView.this.calendar.getStartDate().after(calEvent.getEventStartDateTime()))
			{
				DateTime calFirstDate = new DateTime(CalendarView.this.calendar.getStartDate());
				calEvent.setEventStartDateTime(calFirstDate.withHourOfDay(12).toDate());
				calEvent.setEventEndDateTime(calFirstDate.withHourOfDay(14).toDate());
			}
			currentEvent = new ScoutCalEvent(calEvent);
			eventDetails.setEvent(calEvent, true);
		}
	}

	class EventClickHander implements EventClickHandler
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void eventClick(EventClick event)
		{
			if (event != null)
			{
				currentEvent = (ScoutCalEvent) event.getCalendarEvent();
				eventDetails.setEvent(currentEvent.getEntity(), false);
			}
			else
			{
				currentEvent = null;
				eventDetails.setEvent(null, false);
			}
		}
	}

	@Override
	public void eventSaved(au.org.scoutmaster.domain.Event event, boolean newEvent)
	{
		if (newEvent)
		{
			CalendarView.this.calendar.addEvent(currentEvent);
		}
		else
		{
			currentEvent.updateEvent(event);
		}
	}

	@Override
	public void eventDeleted(au.org.scoutmaster.domain.Event event)
	{
		currentEvent.updateEvent(event);

	}

	class MyCalendar extends Calendar
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void fireNavigationEvent(boolean forward)
		{
			super.fireNavigationEvent(forward);
		}

	}

	/**
	 * If the events properties change we need to tell the event details to
	 * update.
	 */
	@Override
	public void eventChange(EventChangeEvent eventChangeEvent)
	{
		ScoutCalEvent changedEvent = (ScoutCalEvent) eventChangeEvent.getCalendarEvent();

		if (changedEvent.getEntity().getId().equals(this.currentEvent.getEntity().getId()))
		{
			this.eventDetails.setEvent(changedEvent.getEntity(), false);
		}
	}

}
