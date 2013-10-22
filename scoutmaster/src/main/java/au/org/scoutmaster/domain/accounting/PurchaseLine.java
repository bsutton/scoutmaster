package au.org.scoutmaster.domain.accounting;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import au.org.scoutmaster.domain.BaseEntity;

@Entity
@Table(name="PurchaseLine")
public class PurchaseLine  extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The purchase this PurchaseLine belongs to.
	 */
	@ManyToOne
	Purchase purchase;
	
	/**
	 * Controls the order this line item appears in the purchase.
	 */
	Integer ordinal; 	
	
	/**
	 * The number of items that where purchased. 
	 * Defaults to 1
	 */
	BigDecimal quantity = new BigDecimal(1.00);
	
	
	/**
	 * A description of the item purchased.
	 */
	String description;
	
	/**
	 * The cost of of purchased item.
	 */
	@Embedded 
	@AttributeOverrides({
	@AttributeOverride(name="money", column=@Column(name="itemCostMoney"))
	, @AttributeOverride(name="taxPercentage", column=@Column(name="itemCostTaxPercentage"))})
	MoneyWithTax itemCost;
	
	/**
	 * The line total calculated by multiplying the itemCost by the quantity.
	 */
	@Embedded
	@AttributeOverrides({
	@AttributeOverride(name="money", column=@Column(name="lineTotalMoney"))
	, @AttributeOverride(name="taxPercentage", column=@Column(name="lineTotalTaxPercentage"))})
	MoneyWithTax lineTotal;

	@Override
	public String getName()
	{
		return description;
	}
	
}
