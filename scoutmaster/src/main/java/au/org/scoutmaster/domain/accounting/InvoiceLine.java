package au.org.scoutmaster.domain.accounting;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class InvoiceLine
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * The invoice this InvoiceLine belongs to.
	 */
	@ManyToOne
	Invoice invoice;
	
	/**
	 * Controls the order this line item appears in the invoice.
	 */
	int ordinal; 	
	
	/**
	 * The number of items that need are to be invoiced.
	 */
	BigDecimal quantity;
	
	
	/**
	 * The product or service the person is being billed for.
	 */
	@ManyToOne
	Product product; 
	
	/**
	 * The cost of of one of the products.
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
	
}
