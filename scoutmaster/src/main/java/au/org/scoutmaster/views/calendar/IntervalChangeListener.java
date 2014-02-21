package au.org.scoutmaster.views.calendar;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.org.scoutmaster.views.calendar.CalendarView.Interval;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.Calendar;

public class IntervalChangeListener implements ClickListener 
{
	private static final long serialVersionUID = 1L;

	private Calendar calendar;

	private CalendarView view;

	IntervalChangeListener(Calendar calendar, CalendarView view)
	{
		this.calendar = calendar;
		this.view = view;
	}

	@Override
	public void buttonClick(ClickEvent event)
	{
		Interval interval = (Interval) event.getButton().getData();
		setInterval(interval);
		view.setActiveViewButton(event.getButton());

	}

	void setInterval(Interval interval)
	{
		DateTime calendarsFirstDate = new DateTime(calendar.getStartDate());

		LocalDate localDate = calendarsFirstDate.toLocalDate();

		DateTime startOfDay = localDate.toDateTimeAtStartOfDay();
		DateTime startOfNextDay = localDate.plusDays(1).toDateTimeAtStartOfDay();

		org.joda.time.DateTime.Property monthField;
		switch (interval)
		{
			case Daily:
				//calendar.setCaption("Day view");
				calendar.setStartDate(startOfDay.toDate());
				calendar.setEndDate(startOfNextDay.toDate());
				break;
				
			case Weekly:
				//calendar.setCaption("Week view");
//
//				DateTime midnightMonday = startOfDay.withDayOfWeek(DateTimeConstants.MONDAY);
//
//				// If your week starts on Sunday, you need to subtract one.
//				// Adjust
//				// accordingly.
//				DateTime midnightSunday = midnightMonday.plusDays(-1);
//
//				DateTime midnightNextSaturday = midnightSunday.plusDays(6);
//
//				calendar.setStartDate(midnightSunday.toDate());
//				calendar.setEndDate(midnightNextSaturday.toDate());
				
				calendar.setEndDate(calendarsFirstDate.plusDays(6).toDate());
				break;
				
			case Monthly:
			//	calendar.setCaption("Month view");

				monthField = calendarsFirstDate.dayOfMonth(); 
				int lastDayOfMonth = monthField.getMaximumValue();
				calendar.setStartDate(calendarsFirstDate.minusDays(calendarsFirstDate.getDayOfMonth() - 1).toDate());
				calendar.setEndDate(calendarsFirstDate.plusDays(lastDayOfMonth - calendarsFirstDate.getDayOfMonth()).toDate());
				
//				calendar.setEndDate(calendarsFirstDate.plusDays(lastDayOfMonth).toDate());
				break;

		}
	}


}
