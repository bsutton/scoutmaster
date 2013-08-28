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
@Table(name="CreditNoteLine")
public class CreditNoteLine  extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The invoice this InvoiceLine belongs to.
	 */
	@ManyToOne
	CreditNote creditNote;
	
	/**
	 * Controls the order this line item appears in the invoice.
	 */
	Integer ordinal; 	
	
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
