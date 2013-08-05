package au.org.scoutmaster.domain;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

import org.joda.time.DateTime;
import org.joda.time.Interval;

@Embeddable
public class Period
{
	@Basic
	protected int years;
	@Basic
	protected int months;
	@Basic
	protected int days;

	public Period()
	{

	}

	public Period(int years, int months, int days)
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

	public int getYears()
	{
		return years;
	}

	public int getMonths()
	{
		return months;
	}

	public int getDays()
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
	
	Period withYears(int years)
	{
		this.years = years;
		return this;
	}
	
	Period withMonths(int months)
	{
		this.months = months;
		return this;
	}
	Period withDays(int days)
	{
		this.days = days;
		return this;
	}
}

