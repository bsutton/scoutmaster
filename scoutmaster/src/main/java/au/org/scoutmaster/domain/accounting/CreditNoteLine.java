package au.org.scoutmaster.domain.accounting;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import au.org.scoutmaster.domain.BaseEntity;

@Entity
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "CreditNoteLine")
public class CreditNoteLine extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The invoice this InvoiceLine belongs to.
	 */
	@ManyToOne(targetEntity = CreditNote.class)
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
	@ManyToOne(targetEntity = Product.class)
	Product product;

	/**
	 * The cost of of one of the products.
	 */
	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "money.fixedDoubleValue", column = @Column(name = "itemCostMoneyValue") ),
			@AttributeOverride(name = "money.precision", column = @Column(name = "itemCostMoneyPrecision") ),
			@AttributeOverride(name = "taxPercentage.fixedDoubleValue", column = @Column(name = "itemCostTaxPrecentageValue") ),
			@AttributeOverride(name = "taxPercentage.precision", column = @Column(name = "itemCostTaxPercentagePrecision") ) })
	MoneyWithTax itemCost;

	/**
	 * The line total calculated by multiplying the itemCost by the quantity.
	 */
	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "money.fixedDoubleValue", column = @Column(name = "lineTotalMoneyValue") ),
			@AttributeOverride(name = "money.precision", column = @Column(name = "lineTotalMoneyPrecision") ),
			@AttributeOverride(name = "taxPercentage.fixedDoubleValue", column = @Column(name = "lineTotalTaxPercentageValue") ),
			@AttributeOverride(name = "taxPercentage.precision", column = @Column(name = "lineTotalTaxPercentagePrecision") ) })
	MoneyWithTax lineTotal;

	@Override
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "UWF_UNWRITTEN_FIELD", justification = "JPA injection")
	public String getName()
	{
		return this.creditNote.getName() + " " + this.ordinal;
	}

}
