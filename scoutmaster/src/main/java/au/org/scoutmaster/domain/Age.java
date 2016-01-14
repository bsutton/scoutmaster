package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.simpleframework.xml.Attribute;

/**
 * Used to represent the age of a person in Years, Months and days
 *
 * @author bsutton
 *
 */
@Embeddable
@Access(AccessType.FIELD)
public class Age
{
	@Basic
	@Min(value = 0)
	@Attribute
	protected Integer years;

	@Basic
	@Min(value = 0)
	@Attribute
	protected Integer months;

	@Basic
	@Min(value = 0)
	@Attribute
	protected Integer days;

	public Age()
	{

	}

	public Age(final DateTime birthDate)
	{
		final Interval interval = new Interval(birthDate, DateTime.now());
		this.years = interval.toPeriod().getYears();
		this.months = interval.toPeriod().getMonths();
		this.days = interval.toPeriod().getDays();
	}

	public Age(final Integer years, final Integer months, final Integer days)
	{
		this.years = years;
		this.months = months;
		this.days = days;

	}

	/**
	 * Takes an age string and attempts to parse it. Expected format is YYy MMy
	 * DDd
	 *
	 * @param age
	 */
	public Age(String age)
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
			final String[] ageParts = age.trim().split(" ");

			if (ageParts.length > 3)
			{
				throw new RuntimeException("An age must only contain three parts e.g. 10y 3m 31d");
			}

			if (ageParts.length > 0)
			{
				int index = 0;
				String part = ageParts[index].trim().toLowerCase();
				if (part.endsWith("y"))
				{
					// we have a year part.
					final String years = part.substring(0, part.length() - 1);
					this.years = Integer.valueOf(years);
					index++;
				}
				else
				{
					this.years = 0;
				}

				if (index < ageParts.length && ageParts[index].trim().toLowerCase().endsWith("m"))
				{
					part = ageParts[index].trim().toLowerCase();
					// we have a month part.
					final String month = part.substring(0, part.length() - 1);
					this.months = Integer.valueOf(month);
					index++;
				}
				else
				{
					this.months = 0;
				}

				if (index < ageParts.length && ageParts[index].trim().toLowerCase().endsWith("d"))
				{
					part = ageParts[index].trim().toLowerCase();
					// we have a day part.
					final String day = part.substring(0, part.length() - 1);
					this.days = Integer.valueOf(day);
					index++;
				}
				else
				{
					this.days = 0;
				}

				if (ageParts.length != index)
				{
					throw new RuntimeException("An age must only contain three parts e.g. 10y 3m 31d");
				}
			}

		}
	}

	public Integer getYears()
	{
		return this.years;
	}

	public Integer getMonths()
	{
		return this.months;
	}

	public Integer getDays()
	{
		return this.days;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();

		boolean prior = false;
		if (this.years > 0)
		{
			sb.append(this.years + "y");
			prior = true;
		}

		if (this.months > 0)
		{
			if (prior)
			{
				sb.append(", ");
			}
			sb.append(this.months + "m");
			prior = true;
		}
		if (this.days > 0)
		{
			if (prior)
			{
				sb.append(", ");
			}
			sb.append(this.days + "d");
		}
		return sb.toString();
	}

	public DateTime getDateTime()
	{
		return new DateTime(this.years, this.months, this.days, 0, 0);
	}

	Age withYears(final Integer years)
	{
		this.years = years;
		return this;
	}

	Age withMonths(final Integer months)
	{
		this.months = months;
		return this;
	}

	Age withDays(final Integer days)
	{
		this.days = days;
		return this;
	}

	public DateTime getBirthDate()
	{
		return new DateMidnight(DateTime.now()
				.minus(new org.joda.time.Period().withYears(getYears()).withMonths(getMonths()).withDays(getDays())))
						.toDateTime();
	}

}
