package au.org.scoutmaster.domain.accounting;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import au.org.scoutmaster.domain.BaseEntity;

@Entity
@Table(name = "InvoiceLine")
@Access(AccessType.FIELD)
public class InvoiceLine extends BaseEntity
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The invoice this InvoiceLine belongs to.
	 */
	@ManyToOne(targetEntity=Invoice.class)
	Invoice invoice;

	/**
	 * Controls the order this line item appears in the invoice.
	 */
	Integer ordinal;

	/**
	 * The number of items that need are to be invoiced.
	 */
	@Embedded
	FixedDouble quantity;

	/**
	 * The product or service the person is being billed for.
	 */
	@ManyToOne(targetEntity=Product.class)
	Product product;

	/**
	 * The cost of of one of the products.
	 */
	@Embedded
	@AttributeOverrides(
	{
			@AttributeOverride(name = "money.fixedDoubleValue", column = @Column(name = "lineTotalMoneyValue")),
			@AttributeOverride(name = "money.precision", column = @Column(name = "lineTotalMoneyPrecision")),
			@AttributeOverride(name = "taxPercentage.fixedDoubleValue", column = @Column(name = "itemCostTaxPercentageValue")),
			@AttributeOverride(name = "taxPercentage.precision", column = @Column(name = "itemCostTaxPercentagePrecision")) })
	MoneyWithTax itemCost;

	/**
	 * The line total calculated by multiplying the itemCost by the quantity.
	 */
	@Embedded
	@AttributeOverrides(
	{
			@AttributeOverride(name = "money.fixedDoubleValue", column = @Column(name = "lineTotalMoneyValue")),
			@AttributeOverride(name = "money.precision", column = @Column(name = "lineTotalMoneyPrecision")),
			@AttributeOverride(name = "taxPercentage.fixedDoubleValue", column = @Column(name = "lineTotalTaxPercentageValue")),
			@AttributeOverride(name = "taxPercentage.precision", column = @Column(name = "lineTotalTaxPercentagePrecision")) })
	MoneyWithTax lineTotal;

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return invoice.getName() + " " + ordinal;
	}

}
