package au.org.scoutmaster.domain;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

import org.joda.time.DateTime;
import org.joda.time.Interval;

@Embeddable
public class Period
{
	@Basic
	protected Integer years;
	@Basic
	protected Integer months;
	@Basic
	protected Integer days;

	public Period()
	{

	}

	public Period(Integer years, Integer months, Integer days)
	{
		this.years = years;
		this.months = months;
		this.days = days;

	}

	public Period(Interval interval)
	{
		this.years = interval.toPeriod().getYears();
		this.months = interval.toPeriod().getMonths();
		this.days = interval.toPeriod().getDays();
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
	
	Period withYears(Integer years)
	{
		this.years = years;
		return this;
	}
	
	Period withMonths(Integer months)
	{
		this.months = months;
		return this;
	}
	Period withDays(Integer days)
	{
		this.days = days;
		return this;
	}
}

