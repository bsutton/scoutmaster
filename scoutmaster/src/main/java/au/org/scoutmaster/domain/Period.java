package au.org.scoutmaster.domain;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

import org.joda.time.Interval;

@Embeddable
public class Period
{
	@Basic
	private int years;
	@Basic
	private int months;
	@Basic
	private int days;

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
}

class Builder
{
	private int years;
	private int months;
	private int days;

	Period build()
	{
		return new Period(this.years, this.months, this.days);
	}

	Builder setYears(int years)
	{
		this.years = years;
		return this;
	}

	Builder setMonths(int months)
	{
		this.months = months;
		return this;
	}

	Builder setDays(int days)
	{
		this.days = days;
		return this;
	}

}
