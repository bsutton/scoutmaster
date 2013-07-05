package au.com.noojee.scouts.domain;

import java.util.Calendar;

public enum Night
{
	SUNDAY(Calendar.SUNDAY), MONDAY(Calendar.MONDAY), TUESDAY(Calendar.TUESDAY), WEDNESDAY(Calendar.WEDNESDAY), THURSDAY(
			Calendar.THURSDAY), FRIDAY(Calendar.FRIDAY), SATURDAY(Calendar.SATURDAY);

	private int dayOfWeek;

	Night(int dayOfWeek)
	{
		this.dayOfWeek = dayOfWeek;
	}

	public int getDayOfWeek()
	{
		return dayOfWeek;
	}

}
