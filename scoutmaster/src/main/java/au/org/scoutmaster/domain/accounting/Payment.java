package au.org.scoutmaster.domain.accounting;

import java.sql.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import au.org.scoutmaster.domain.BaseEntity;

@Entity
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "Payment")
@Access(AccessType.FIELD)
public class Payment extends BaseEntity
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The date the payment was made.
	 */
	Date paymentDate;

	/**
	 * The set of invoices this payment applies to.
	 */
	@ManyToMany(targetEntity = Invoice.class)
	List<Invoice> invoices;

	/**
	 * The amount paid
	 */
	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "money.fixedDoubleValue", column = @Column(name = "amountMoneyValue") ),
			@AttributeOverride(name = "money.precision", column = @Column(name = "amountMoneyPrecision") ),
			@AttributeOverride(name = "taxPercentage.fixedDoubleValue", column = @Column(name = "amountTaxPrecentageValue") ),
			@AttributeOverride(name = "taxPercentage.precision", column = @Column(name = "amountTaxPercentagePrecision") ) })
	MoneyWithTax amount;

	/**
	 * A reference for the payment. Typically a cheque no. or a bank transaction
	 * id
	 */
	String reference;

	/**
	 * A note that optionally describes the purpose of the payment.
	 */
	String note;

	@Override
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value =
	{ "NP_UNWRITTEN_FIELD", "UWF_UNWRITTEN_FIELD" }, justification = "JPA injection")
	public String getName()
	{
		return this.paymentDate.toString() + this.note;
	}
}
