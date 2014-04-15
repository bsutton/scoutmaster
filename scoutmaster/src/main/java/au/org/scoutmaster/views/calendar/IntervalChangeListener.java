package au.org.scoutmaster.views.calendar;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.org.scoutmaster.views.calendar.CalendarView.Interval;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Calendar;

public class IntervalChangeListener implements ClickListener
{
	private static final long serialVersionUID = 1L;

	private final Calendar calendar;

	private final CalendarView view;

	IntervalChangeListener(final Calendar calendar, final CalendarView view)
	{
		this.calendar = calendar;
		this.view = view;
	}

	@Override
	public void buttonClick(final ClickEvent event)
	{
		final Interval interval = (Interval) event.getButton().getData();
		setInterval(interval);
		this.view.setActiveViewButton(event.getButton());

	}

	void setInterval(final Interval interval)
	{
		final DateTime calendarsFirstDate = new DateTime(this.calendar.getStartDate());

		final LocalDate localDate = calendarsFirstDate.toLocalDate();

		final DateTime startOfDay = localDate.toDateTimeAtStartOfDay();
		final DateTime startOfNextDay = localDate.plusDays(1).toDateTimeAtStartOfDay();

		org.joda.time.DateTime.Property monthField;
		switch (interval)
		{
			case Daily:
				// calendar.setCaption("Day view");
				this.calendar.setStartDate(startOfDay.toDate());
				this.calendar.setEndDate(startOfNextDay.toDate());
				break;

			case Weekly:
				// calendar.setCaption("Week view");
				//
				// DateTime midnightMonday =
				// startOfDay.withDayOfWeek(DateTimeConstants.MONDAY);
				//
				// // If your week starts on Sunday, you need to subtract one.
				// // Adjust
				// // accordingly.
				// DateTime midnightSunday = midnightMonday.plusDays(-1);
				//
				// DateTime midnightNextSaturday = midnightSunday.plusDays(6);
				//
				// calendar.setStartDate(midnightSunday.toDate());
				// calendar.setEndDate(midnightNextSaturday.toDate());

				this.calendar.setEndDate(calendarsFirstDate.plusDays(6).toDate());
				break;

			case Monthly:
				// calendar.setCaption("Month view");

				monthField = calendarsFirstDate.dayOfMonth();
				final int lastDayOfMonth = monthField.getMaximumValue();
				this.calendar.setStartDate(calendarsFirstDate.minusDays(calendarsFirstDate.getDayOfMonth() - 1)
						.toDate());
				this.calendar.setEndDate(calendarsFirstDate.plusDays(
						lastDayOfMonth - calendarsFirstDate.getDayOfMonth()).toDate());

				// calendar.setEndDate(calendarsFirstDate.plusDays(lastDayOfMonth).toDate());
				break;

		}
	}

}
