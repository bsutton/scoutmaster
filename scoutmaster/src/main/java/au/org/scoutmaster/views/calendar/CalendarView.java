package au.org.scoutmaster.views.calendar;

import java.util.Arrays;
import java.util.Date;

import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.forms.EventForm;
import au.org.scoutmaster.forms.EventForm.SaveEventListener;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.ui.components.calendar.ContainerEventProvider;

/** A start view for navigating to the main view */
@Menu(display = "Calendar")
public class CalendarView extends VerticalLayout implements View
{
	public static final String NAME = "Calendar";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Calendar calendar;

	public CalendarView()
	{
		setSizeFull();
		setMargin(true);

	}

	enum Interval
	{
		Monthly, Weekly, Daily
	}

	@Override
	public void enter(ViewChangeEvent event)
	{

		HorizontalLayout intervalLayout = new HorizontalLayout();
		intervalLayout.setSpacing(true);
		intervalLayout.setMargin(true);
		Label intervalLabel = new Label("Interval");
		ComboBox intervalField = new ComboBox(null, Arrays.asList(Interval.values()));
		intervalField.setNullSelectionAllowed(false);
		intervalField.setTextInputAllowed(false);
		intervalField.setImmediate(true);

		calendar = new Calendar((String) null);
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

		intervalField.addValueChangeListener(new IntervalChangeListener(calendar));
		intervalField.setValue(Interval.Monthly);

		Label calendarLabel = new Label("<b>Scout event Calendar</b>");
		calendarLabel.setContentMode(ContentMode.HTML);
		this.addComponent(calendarLabel);
		intervalLayout.addComponent(intervalLabel);
		intervalLayout.addComponent(intervalField);
		Button newButton = new Button("New Event");
		newButton.addClickListener(new NewButtonListener());
		intervalLayout.addComponent(newButton);
		this.addComponent(intervalLayout);
		this.addComponent(calendar);
		this.setExpandRatio(calendar, 1.0f);
		this.setSpacing(false);

		// added handler so we can edit an event by clicking it.
		calendar.setHandler(new EventClickHander());

		// HorizontalLayout controls = new HorizontalLayout();

		// When an event has a section attached use this to filter the container
		// ComboBox section = new ComboBox("Section");
		// section.setContainerDataSource(new
		// DaoFactory().getDao(SectionTypeDao.class).createVaadinContainer());
		// section.setImmediate(true);
		//
		// section.addListener(new ValueChangeListener()
		// {
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public void valueChange(com.vaadin.data.Property.ValueChangeEvent
		// event)
		// {
		// // filter calendar events by user immediately
		// calendar.eventSetChange(new EventSetChange(CalendarView.this));
		//
		// }
		// });

		// this.addComponent(controls);

	}

	class NewButtonListener implements ClickListener, SaveEventListener
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event)
		{
			Window mailWindow = new Window("New Event");
			mailWindow.setWidth("450");
			mailWindow.setHeight("420");
			mailWindow.setContent(new EventForm(mailWindow, new au.org.scoutmaster.domain.Event(), this));
			mailWindow.setVisible(true);
			mailWindow.center();
			UI.getCurrent().addWindow(mailWindow);
		}

		@Override
		public void eventSaved(au.org.scoutmaster.domain.Event event)
		{
			CalendarView.this.calendar.addEvent(new ScoutCalEvent(event));

		}
	}

	class EventClickHander implements EventClickHandler, SaveEventListener
	{
		private static final long serialVersionUID = 1L;
		private ScoutCalEvent e;

		@Override
		public void eventClick(EventClick event)
		{
			{
				e = (ScoutCalEvent) event.getCalendarEvent();

				Window mailWindow = new Window("New Event");
				mailWindow.setWidth("450");
				mailWindow.setHeight("420");
				mailWindow.setContent(new EventForm(mailWindow, e.getEntity(), this));
				mailWindow.setVisible(true);
				mailWindow.center();
				UI.getCurrent().addWindow(mailWindow);

			}
		}

		@Override
		public void eventSaved(au.org.scoutmaster.domain.Event event)
		{
			e.updateEvent(event);
		}
	}

}