package au.org.scoutmaster.domain.accounting;

import javax.persistence.Embeddable;


@Embeddable
public class Money
{
	static final private int defaultPrecision = 2;
	
	private final double fixedDoubleValue;

	/**
	 * The no. of decimal places to store the number to.
	 */
	private final int precision;


	Money()
	{
		this.precision = defaultPrecision;
		this.fixedDoubleValue = 0.0;
	}

	Money(final double dValue)
	{
		this.precision = defaultPrecision;
		this.fixedDoubleValue = FixedDouble.Round(dValue, precision);
	}
	
	Money(final FixedDouble rhs)
	{
		this.precision = rhs.getPrecision();
		this.fixedDoubleValue = FixedDouble.Round(rhs.getFixedDoubleValue(), rhs.getPrecision());
	}


	Money add(final Money rhs)
	{
		return new Money(getFixedDoubleValue() + rhs.getFixedDoubleValue());
	}

	private double getFixedDoubleValue()
	{
		return fixedDoubleValue;
	}
	
	private int getPrecision()
	{
		return precision;
	}


	Money subtract(final Money rhs)
	{
		return new Money(getFixedDoubleValue() - rhs.getFixedDoubleValue());
	}

	Money multiply(final Money rhs)
	{
		return new Money(getFixedDoubleValue() * rhs.getFixedDoubleValue());
	}

	Money multiply(final FixedDouble rhs)
	{
		return new Money(getFixedDoubleValue() * rhs.getFixedDoubleValue());
	}

	Money multiply(final int rhs)
	{
		return new Money(getFixedDoubleValue() * rhs);
	}
	
	public Money multiply(double rhs)
	{
		return new Money(getFixedDoubleValue() * rhs);
	}

	Money divide(final Money rhs)
	{
		return new Money(getFixedDoubleValue() / rhs.getFixedDoubleValue());
	}

	boolean equals(final Money rhs)
	{
		return (Math.abs(getFixedDoubleValue() - rhs.getFixedDoubleValue()) < HalfAPrecision());
	}

	boolean lessThan(final Money rhs)
	{
		return ((rhs.getFixedDoubleValue() - getFixedDoubleValue()) >= HalfAPrecision());
	}

	boolean lessThan(final int rhs)
	{
		return ((rhs - getFixedDoubleValue()) >= HalfAPrecision());
	}

	boolean greaterThan(final Money rhs) 
	{
	    return !(getFixedDoubleValue() == rhs.getFixedDoubleValue() || getFixedDoubleValue() < rhs.getFixedDoubleValue());
	}

	boolean lessThanOrEqual(final Money rhs) 
	{
	    return (getFixedDoubleValue() == rhs.getFixedDoubleValue() || getFixedDoubleValue() < rhs.getFixedDoubleValue());
	}

	boolean greaterThanOrEqual(final Money rhs) 
	{
	    return (getFixedDoubleValue() == rhs.getFixedDoubleValue() || getFixedDoubleValue() > rhs.getFixedDoubleValue());
	}

	public String toString()
	{
		return FixedDouble.toString(getFixedDoubleValue(), getPrecision());
	}


	

	double HalfAPrecision()
	{
		return FixedDouble.HalfAPrecision(getPrecision());
	}

	
	Money multiply(double lhs, final Money rhs)
	{
		return new Money(lhs * rhs.getFixedDoubleValue());
	}

	Money add(double lhs, final Money rhs)
	{
		return new Money(lhs + rhs.getFixedDoubleValue());
	}

	Money subtract(double lhs, final Money rhs)
	{
		return new Money(lhs - rhs.getFixedDoubleValue());
	}

	// returns the absolute value of the currency.
	Money abs(final Money val)
	{
		return (val.lessThan(0)) ? val.multiply(-1) : val;
	}

	public static int getDefaultPrecision()
	{
		return defaultPrecision;
	}

	
}
