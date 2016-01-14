package au.org.scoutmaster.domain.accounting;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
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
@Table(name = "PurchaseLine")
@Access(AccessType.FIELD)
public class PurchaseLine extends BaseEntity
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
	 * The number of items that where purchased. Defaults to 1
	 */
	BigDecimal quantity = new BigDecimal(1.00);

	/**
	 * A description of the item purchased.
	 */
	String description;

	// CREATE TABLE PurchaseLine (ID BIGINT AUTO_INCREMENT NOT NULL,
	// CONSISTENCYVERSION BIGINT NOT NULL
	// , CREATED DATE, DESCRIPTION VARCHAR(255), ORDINAL INTEGER, QUANTITY
	// DECIMAL(38), UPDATED DATE
	// , itemCostMoney LONGBLOB, itemCostTaxPercentage DECIMAL(38)
	// , lineTotalMoney LONGBLOB
	// , lineTotalTaxPercentage DECIMAL(38), PURCHASE_ID BIGINT, PRIMARY KEY
	// (ID))
	/**
	 * The cost of of purchased item.
	 */
	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "money.fixedDoubleValue", column = @Column(name = "itemCostMoneyValue") ),
			@AttributeOverride(name = "money.precision", column = @Column(name = "itemCostMoneyPrecision") ),
			@AttributeOverride(name = "taxPercentage.fixedDoubleValue", column = @Column(name = "itemCostTaxPercentageValue") ),
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

	PurchaseLine()
	{

	}

	@Override
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "UWF_UNWRITTEN_FIELD", justification = "JPA injection")
	public String getName()
	{
		return this.description;
	}

}
