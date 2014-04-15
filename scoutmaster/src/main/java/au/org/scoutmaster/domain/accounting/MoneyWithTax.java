package au.org.scoutmaster.domain.accounting;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@Access(AccessType.FIELD)
public class MoneyWithTax
{
	private final static FixedDouble PERCENTAGEDENOMINATOR = new FixedDouble(100, 0);
	private final static FixedDouble UNITY = new FixedDouble(1, 0);

	/**
	 * The money without the tax included.
	 */
	@Embedded
	private Money money;

	/**
	 * The percentage of tax applied to this amount
	 */
	@Embedded
	private FixedDouble taxPercentage;

	MoneyWithTax()
	{

	}

	MoneyWithTax(final Money money, final FixedDouble taxPercentage)
	{
		this.money = money;
		this.taxPercentage = taxPercentage;
	}

	Money getAmountWithTax()
	{
		return this.money.multiply(this.taxPercentage.add(MoneyWithTax.UNITY)
				.divide(MoneyWithTax.PERCENTAGEDENOMINATOR));
	}

	// public FixedDouble getTaxPercentage()
	// {
	// return taxPercentage;
	// }
	//
	// public void setTaxPercentage(FixedDouble taxPercentage)
	// {
	// this.taxPercentage = taxPercentage;
	// }

	// public MyCurrency getMoney()
	// {
	// return money;
	// }
	//
	// public void setMoney(MyCurrency money)
	// {
	// this.money = money;
	// }
	//

}
