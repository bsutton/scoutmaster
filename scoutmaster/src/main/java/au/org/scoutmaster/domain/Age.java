package au.org.scoutmaster.domain;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * Used to represent the age of a person in Years, Months and days
 * 
 * @author bsutton
 * 
 */
@Embeddable
public class Age
{
	@Basic
	@Min(value=0)
	protected Integer years;
	
	@Basic
	@Min(value=0)
	protected Integer months;
	
	@Basic
	@Min(value=0)
	protected Integer days;

	public Age()
	{

	}

	public Age(DateTime birthDate)
	{
		Interval interval = new Interval(birthDate, DateTime.now());
		this.years = interval.toPeriod().getYears();
		this.months = interval.toPeriod().getMonths();
		this.days = interval.toPeriod().getDays();
	}

	public Age(Integer years, Integer months, Integer days)
	{
		this.years = years;
		this.months = months;
		this.days = days;

	}

	public Integer getYears()
	{
		return years;
	}

	public Integer getMonths()
	{
		return months;
	}

	public Integer getDays()
	{
		return days;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		boolean prior = false;
		if (years > 0)
		{
			sb.append(years + "y");
			prior = true;
		}

		if (months > 0)
		{
			if (prior)
				sb.append(", ");
			sb.append(months + "m");
			prior = true;
		}
		if (days > 0)
		{
			if (prior)
				sb.append(", ");
			sb.append(days + "d");
		}
		return sb.toString();
	}

	public DateTime getDateTime()
	{
		return new DateTime(years, months, days, 0, 0);
	}

	Age withYears(Integer years)
	{
		this.years = years;
		return this;
	}

	Age withMonths(Integer months)
	{
		this.months = months;
		return this;
	}

	Age withDays(Integer days)
	{
		this.days = days;
		return this;
	}

	public DateTime getBirthDate()
	{
		return new DateMidnight(DateTime.now().minus(
				new org.joda.time.Period().withYears(getYears()).withMonths(getMonths()).withDays(getDays())))
				.toDateTime();
	}

}
