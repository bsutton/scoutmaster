package au.org.scoutmaster.views;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import au.com.vaadinutils.menu.Menu;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.VerticalLayout;
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

	public CalendarView()
	{
		setSizeFull();
		setMargin(true);

	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		// Customize the event provider for adding events
		// as entities
		ContainerEventProvider cep = new ScoutsContainerEventProvider();

		DateTime midnightToday = new LocalDate().toDateTimeAtStartOfDay();
		DateTime midnightMonday = midnightToday.withDayOfWeek(DateTimeConstants.MONDAY);

		// If your week starts on Sunday, you need to subtract one. Adjust
		// accordingly.
		DateTime midnightSunday = midnightMonday.plusDays(-1);

		DateTime midnightNextSaturday = midnightSunday.plusDays(6);

		final Calendar calendar = new Calendar("Scout Week");
		calendar.setTimeFormat(null);
		calendar.setWeeklyCaptionFormat("dd MMM yyyy");
		calendar.setStartDate(midnightSunday.toDate());
		calendar.setEndDate(midnightNextSaturday.toDate());

		// Monday to Friday
		calendar.setFirstVisibleDayOfWeek(1);
		calendar.setLastVisibleDayOfWeek(7);
		calendar.setFirstVisibleHourOfDay(5);
		calendar.setLastVisibleHourOfDay(22);
		calendar.setSizeFull();

		// Set the container as the data source
		calendar.setEventProvider(cep);

		//HorizontalLayout controls = new HorizontalLayout();

		// When an event has a section attached use this to filter the container
//		ComboBox section = new ComboBox("Section");
//		section.setContainerDataSource(new DaoFactory().getDao(SectionTypeDao.class).createVaadinContainer());
//		section.setImmediate(true);
//
//		section.addListener(new ValueChangeListener()
//		{
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event)
//			{
//				// filter calendar events by user immediately
//				calendar.eventSetChange(new EventSetChange(CalendarView.this));
//
//			}
//		});

		//this.addComponent(controls);
		this.addComponent(calendar);

	}

}