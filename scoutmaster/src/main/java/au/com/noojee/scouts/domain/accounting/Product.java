package au.com.noojee.scouts.domain.accounting;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import au.com.noojee.scouts.domain.Organisation;

/**
 * Used to represent products or services that the Scouts group can charge for.
 * 
 * This can be from membership to activity fees.
 * 
 * @author bsutton
 * 
 */
@Entity
public class Product
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

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
	@Embedded
	@AttributeOverrides({
	@AttributeOverride(name="money", column=@Column(name="costtMoney"))
	, @AttributeOverride(name="taxPercentage", column=@Column(name="costTaxPercentage"))})
	MoneyWithTax cost;
	
	/**
	 * The standard amount the group charges for the product or service.
	 */
	@Embedded
	@AttributeOverrides({
	@AttributeOverride(name="money", column=@Column(name="chargeMoney"))
	, @AttributeOverride(name="taxPercentage", column=@Column(name="chargeTaxPercentage"))})
	MoneyWithTax charge;
	
	/**
	 * The supplier of the product or service. May be null.
	 * 
	 * A supplier should be tagged with the built-in tag 'SUPPLIER'.
	 * 
	 */
	@ManyToOne
	Organisation supplier;

}
