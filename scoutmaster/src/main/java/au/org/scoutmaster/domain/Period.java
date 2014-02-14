package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Embeddable;

import org.joda.time.DateTime;
import org.joda.time.Interval;

@Embeddable
@Access(AccessType.FIELD)
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

	/**
	 * Takes an Period string and attempts to parse it. Expected format is YYy MMy
	 * DDd
	 * 
	 * @param age
	 */
	public Period(String age)
	{
		if (age == null || age.trim().length() == 0)
		{
			this.years = 0;
			this.months = 0;
			this.days = 0;
		}
		else
		{
			// Strip spaces out before each of the component designators if the
			// user has inserted them.
			age = age.replace(" y", "y");
			age = age.replace(" m", "m");
			age = age.replace(" d", "d");
			// strip comma's out.
			age = age.replace(",", "");
			String[] ageParts = age.trim().split(" ");

			if (ageParts.length > 3)
				throw new RuntimeException("An age must only contain three parts e.g. 10y 3m 31d");

			if (ageParts.length > 0)
			{
				int index = 0;
				String part = ageParts[index].trim().toLowerCase();
				if (part.endsWith("y"))
				{
					// we have a year part.
					String years = part.substring(0, part.length() - 1);
					this.years = Integer.valueOf(years);
					index++;
				}
				else
					this.years = 0;

				if (index < ageParts.length && ageParts[index].trim().toLowerCase().endsWith("m"))
				{
					part = ageParts[index].trim().toLowerCase();
					// we have a month part.
					String month = part.substring(0, part.length() - 1);
					this.months = Integer.valueOf(month);
					index++;
				}
				else
					this.months = 0;

				if (index < ageParts.length && ageParts[index].trim().toLowerCase().endsWith("d"))
				{
					part = ageParts[index].trim().toLowerCase();
					// we have a day part.
					String day = part.substring(0, part.length() - 1);
					this.days = Integer.valueOf(day);
					index++;
				}
				else
					this.days = 0;

				if (ageParts.length != index)
				{
					throw new RuntimeException("An age must only contain three parts e.g. 10y 3m 31d");
				}
			}

		}
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

