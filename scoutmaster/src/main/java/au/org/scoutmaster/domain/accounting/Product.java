package au.org.scoutmaster.domain.accounting;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.domain.Organisation;

/**
 * Used to represent products or services that the Scouts group can charge for.
 * 
 * This can be from membership to activity fees.
 * 
 * @author bsutton
 * 
 */
@Entity
@Table(name = "Product")
public class Product extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The name of the product.
	 */
	String name;

	/**
	 * A description of the product.
	 */
	String desription;

	/**
	 * The cost of the product or service.
	 */
	@AttributeOverrides(
	{
			@AttributeOverride(name = "taxPercentage.fixedDoubleValue", column = @Column(name = "costTaxPercentageValue")),
			@AttributeOverride(name = "taxPercentage.precision", column = @Column(name = "costTaxPercentagePrecision")),
			@AttributeOverride(name = "money.fixedDoubleValue", column = @Column(name = "costMoneyValue")),
			@AttributeOverride(name = "money.precision", column = @Column(name = "costMoneyPrecision")),

	})
	MoneyWithTax cost;

	/**
	 * The standard amount the group charges for the product or service.
	 */
	@Embedded
	@AttributeOverrides(
	{
			@AttributeOverride(name = "money.fixedDoubleValue", column = @Column(name = "chargeMoneyValue")),
			@AttributeOverride(name = "money.precision", column = @Column(name = "chargeMoneyPrecision")),
			@AttributeOverride(name = "taxPercentage.fixedDoubleValue", column = @Column(name = "chargeTaxPercentageValue")),
			@AttributeOverride(name = "taxPercentage.precision", column = @Column(name = "chargeTaxPercentagePrecision"))

	})
	MoneyWithTax charge;

	/**
	 * The supplier of the product or service. May be null.
	 * 
	 * A supplier should be tagged with the built-in tag 'SUPPLIER'.
	 * 
	 */
	@ManyToOne
	Organisation supplier;

	@Override
	public String getName()
	{
		return name;
	}

}
