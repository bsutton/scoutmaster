package au.com.noojee.scouts.domain;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

@Embeddable
public class Period
{
	@Basic
	private int years;
	@Basic
	private int months;
	@Basic
	private  int days;

	public Period(int years, int months, int days)
	{
		this.years = years;
		this.months = months;
		this.days = days;

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
