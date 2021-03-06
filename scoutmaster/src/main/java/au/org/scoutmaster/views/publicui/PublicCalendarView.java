package au.org.scoutmaster.views.publicui;

import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.views.calendar.CalendarView;

/** A start view for navigating to the main view */
@Menu(display = "Public View", path = "Public")
public class PublicCalendarView extends CalendarView
{
	public static final String NAME = "PublicCalendar";

	private static final long serialVersionUID = 1L;

	public PublicCalendarView()
	{
		super.setPublic(true);
	}
}