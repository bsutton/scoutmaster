package au.org.scoutmaster.views;

import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.VerticalLayout;

/** A start view for navigating to the main view */
@Menu(display="Appointment")
public class AppointmentView extends VerticalLayout implements View
{
	public static final String NAME = "Appointment";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AppointmentView()
	{
		setSizeFull();
		setMargin(true);

	}

	@Override
	public void enter(ViewChangeEvent event)
	{

		final JPAContainer<au.org.scoutmaster.domain.Event> container = JPAContainerFactory.make(
				au.org.scoutmaster.domain.Event.class, "scoutmaster");
		// EntityProvider<au.com.noojee.scouts.domain.Event> ep =
		// container.getEntityProvider();

		// // Customize the event provider for adding events
		// // as entities
		// ContainerEventProvider cep = new ContainerEventProvider(container)
		// {
		// @Override
		// public void addEvent(CalendarEvent event)
		// {
		// au.com.noojee.scouts.domain.Event entity = new
		// au.com.noojee.scouts.domain.Event(event.getCaption(),
		// event.getDescription(), event.getStart(), event.getEnd(),
		// event.getStyleName());
		// container.addEntity(entity);
		// }
		// };
		DateTime midnightToday = new LocalDate().toDateTimeAtStartOfDay();
		DateTime midnightMonday = midnightToday.withDayOfWeek(DateTimeConstants.MONDAY);

		// If your week starts on Sunday, you need to subtract one. Adjust
		// accordingly.
		DateTime midnightSunday = midnightMonday.plusDays(-1);

		DateTime midnightNextSaturday = midnightSunday.plusDays(6);

		Calendar calendar = new Calendar("Scout Week");
		GregorianCalendar.getInstance().getFirstDayOfWeek();
		calendar.setStartDate(midnightSunday.toDate());
		calendar.setEndDate(midnightNextSaturday.toDate());

		// Monday to Friday
		calendar.setFirstVisibleDayOfWeek(1);
		calendar.setLastVisibleDayOfWeek(5);
		calendar.setFirstVisibleHourOfDay(8);
		calendar.setLastVisibleHourOfDay(17);

		// Set the container as the data source
		calendar.setContainerDataSource(container);

		this.addComponent(calendar);

	}
}