package au.com.noojee.scouts.domain.accounting;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

import org.joda.money.Money;

@Embeddable
public class MoneyWithTax
{
	private final static BigDecimal PERCENTAGEDENOMINATOR = new BigDecimal(100);
	private final static BigDecimal UNITY = new BigDecimal(1);
	/**
	 * The percentage of tax applied to this amount
	 */
	@Basic
	private BigDecimal taxPercentage;
	
	/**
	 * The money without the tax included.
	 */
	@Basic
	private Money money;
	
	MoneyWithTax(Money money, BigDecimal taxPercentage)
	{
		this.money = money;
		this.taxPercentage = taxPercentage;
	}

	Money getAmountWithTax()
	{
		return money.multipliedBy(taxPercentage.add(UNITY).divide(PERCENTAGEDENOMINATOR), RoundingMode.HALF_EVEN);
	}
}
