package au.org.scoutmaster.domain.accounting;

import java.text.DecimalFormat;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.google.common.base.Preconditions;

@Embeddable
@Access(AccessType.FIELD)
public class FixedDouble
{
	static final int pMultiplier[] =
		{ 1, 10, 100, 1000, 10000, 100000, 1000000 };

	/**
	 * Always stored as a rounded value. Which means that when used in
	 * operations we don't need to round it except when assigning a new value to
	 * it.
	 *
	 */
	@Column(name = "fixedDoubleValue")
	private double fixedDoubleValue;

	/**
	 * The no. of decimal places to store the number to.
	 */
	@Column(name = "precision")
	private int precision;

	public FixedDouble()
	{
		this.precision = 2;
		this.fixedDoubleValue = 0.0;
	}

	FixedDouble(final int precision)
	{
		this.precision = precision;
		this.fixedDoubleValue = 0.0;
	}

	FixedDouble(final FixedDouble rhs)
	{
		this.precision = rhs.precision;
		this.fixedDoubleValue = FixedDouble.Round(rhs.fixedDoubleValue, rhs.precision);
	}

	FixedDouble(final double dValue, final int precision)
	{
		this.precision = precision;
		this.fixedDoubleValue = FixedDouble.Round(dValue, precision);
	}

	FixedDouble add(final FixedDouble rhs)
	{
		return new FixedDouble(this.fixedDoubleValue + rhs.fixedDoubleValue, this.precision);
	}

	public FixedDouble divide(final FixedDouble rhs)
	{
		return new FixedDouble(getFixedDoubleValue() / rhs.getFixedDoubleValue(), this.precision);

	}

	double toDouble()
	{
		return this.fixedDoubleValue;
	}

	protected double getFixedDoubleValue()
	{
		return this.fixedDoubleValue;
	}

	protected int getPrecision()
	{
		return this.precision;
	}

	/**
	 * Calculates the rounding factor which should be used when comparing two
	 * numbers. ie. if the precision is 2 then then HalfAPrecision is 0.005.
	 *
	 * @param nPrecision
	 * @return
	 */
	static double HalfAPrecision(final int nPrecision)
	{
		return 5 / Math.pow(10, nPrecision + 1);
	}

	//
	// The following group of functions convert a number to
	// a String displaying significant decimal places.
	//
	static public String toString(final double dNum, final int nPrecision)
	{
		Preconditions.checkArgument(nPrecision >= 0 && nPrecision <= Double.MAX_EXPONENT);

		final StringBuilder sb = new StringBuilder();
		sb.append("#.");
		for (int i = 0; i < nPrecision; i++)
		{
			sb.append("0");
		}

		final DecimalFormat numberFormat = new DecimalFormat(sb.toString());
		return numberFormat.format(dNum);
	}

	static double Round(final double dRawValue, final int nPrecision)
	{
		double dRoundedValue = 0.0;

		// For speed and accuracy we avoid using pow to work out the Multiplier.
		// Use an array of multipliers and Precision as an index.
		// We will set an arbitrary maximum precision of 6.
		Preconditions.checkArgument(nPrecision >= 0 && nPrecision < 7);

		final int nMultiplier = FixedDouble.pMultiplier[nPrecision];
		double dDecider = 5.00 / (nMultiplier * 10.00);

		// if rounding a negative value, ADDING the decider will put the
		// result out by 1 least significant digit.
		if (dRawValue < 0.0)
		{
			dDecider = -1 * dDecider;
		}

		dRoundedValue = (int) ((dRawValue + dDecider) * nMultiplier);
		dRoundedValue /= nMultiplier;

		return dRoundedValue;
	}

	//
	// Parse a string to a double. If the parse succeeded return TRUE and the
	// double value
	// in 'd', otherwise return FALSE.
	// NOTE: No assumption is made on precision, both on the string and result.
	//
	static double Parse(final String text)
	{
		Preconditions.checkNotNull(text);

		return Double.valueOf(text.trim());
	}
}