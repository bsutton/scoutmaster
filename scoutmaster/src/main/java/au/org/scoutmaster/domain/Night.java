package au.org.scoutmaster.domain;

import java.util.Calendar;

public enum Night
{
	SUNDAY(Calendar.SUNDAY), MONDAY(Calendar.MONDAY), TUESDAY(Calendar.TUESDAY), WEDNESDAY(Calendar.WEDNESDAY), THURSDAY(
			Calendar.THURSDAY), FRIDAY(Calendar.FRIDAY), SATURDAY(Calendar.SATURDAY);

	private Integer dayOfWeek;

	Night(Integer dayOfWeek)
	{
		this.dayOfWeek = dayOfWeek;
	}

	public Integer getDayOfWeek()
	{
		return dayOfWeek;
	}

}
