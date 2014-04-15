package au.org.scoutmaster.domain.accounting;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
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
		this.precision = Money.defaultPrecision;
		this.fixedDoubleValue = 0.0;
	}

	public Money(final double dValue)
	{
		this.precision = Money.defaultPrecision;
		this.fixedDoubleValue = FixedDouble.Round(dValue, this.precision);
	}

	public Money(final FixedDouble rhs)
	{
		this.precision = rhs.getPrecision();
		this.fixedDoubleValue = FixedDouble.Round(rhs.getFixedDoubleValue(), rhs.getPrecision());
	}

	/**
	 * Casts a string to a money value.
	 *
	 * @param value
	 */
	public Money(String amount)
	{
		if (amount == null || amount.trim().length() == 0)
		{
			amount = "0.00";
		}
		this.precision = Money.defaultPrecision;
		// TODO internationalise this.
		this.fixedDoubleValue = org.joda.money.Money.parse("AUD " + amount).getAmount().doubleValue();
	}

	public Money add(final Money rhs)
	{
		return new Money(getFixedDoubleValue() + rhs.getFixedDoubleValue());
	}

	private double getFixedDoubleValue()
	{
		return this.fixedDoubleValue;
	}

	private int getPrecision()
	{
		return this.precision;
	}

	public Money subtract(final Money rhs)
	{
		return new Money(getFixedDoubleValue() - rhs.getFixedDoubleValue());
	}

	public Money multiply(final Money rhs)
	{
		return new Money(getFixedDoubleValue() * rhs.getFixedDoubleValue());
	}

	public Money multiply(final FixedDouble rhs)
	{
		return new Money(getFixedDoubleValue() * rhs.getFixedDoubleValue());
	}

	public Money multiply(final int rhs)
	{
		return new Money(getFixedDoubleValue() * rhs);
	}

	public Money multiply(final double rhs)
	{
		return new Money(getFixedDoubleValue() * rhs);
	}

	public Money divide(final Money rhs)
	{
		return new Money(getFixedDoubleValue() / rhs.getFixedDoubleValue());
	}

	public boolean equals(final Money rhs)
	{
		return rhs != null && Math.abs(getFixedDoubleValue() - rhs.getFixedDoubleValue()) < HalfAPrecision();
	}

	@Override
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value =
{ "HE_EQUALS_USE_HASHCODE" }, justification = "Explicit implementation of equals as above.")
	public boolean equals(final Object rhs)
	{
		return rhs != null && equals((Money) rhs);
	}

	@Override
	public int hashCode()
	{
		assert false : "You can't put money into a map/hashtable as a key.";
	return 42; // any arbitrary constant will do
	}

	public boolean lessThan(final Money rhs)
	{
		return rhs.getFixedDoubleValue() - getFixedDoubleValue() >= HalfAPrecision();
	}

	public boolean lessThan(final int rhs)
	{
		return rhs - getFixedDoubleValue() >= HalfAPrecision();
	}

	public boolean greaterThan(final Money rhs)
	{
		return !(getFixedDoubleValue() == rhs.getFixedDoubleValue() || getFixedDoubleValue() < rhs
				.getFixedDoubleValue());
	}

	public boolean lessThanOrEqual(final Money rhs)
	{
		return getFixedDoubleValue() == rhs.getFixedDoubleValue() || getFixedDoubleValue() < rhs.getFixedDoubleValue();
	}

	public boolean greaterThanOrEqual(final Money rhs)
	{
		return getFixedDoubleValue() == rhs.getFixedDoubleValue() || getFixedDoubleValue() > rhs.getFixedDoubleValue();
	}

	@Override
	public String toString()
	{
		return FixedDouble.toString(getFixedDoubleValue(), getPrecision());
	}

	private double HalfAPrecision()
	{
		return FixedDouble.HalfAPrecision(getPrecision());
	}

	public Money multiply(final double lhs, final Money rhs)
	{
		return new Money(lhs * rhs.getFixedDoubleValue());
	}

	public Money add(final double lhs, final Money rhs)
	{
		return new Money(lhs + rhs.getFixedDoubleValue());
	}

	public Money subtract(final double lhs, final Money rhs)
	{
		return new Money(lhs - rhs.getFixedDoubleValue());
	}

	// returns the absolute value of the currency.
	public Money abs(final Money val)
	{
		return val.lessThan(0) ? val.multiply(-1) : val;
	}

	public static int getDefaultPrecision()
	{
		return Money.defaultPrecision;
	}

}
