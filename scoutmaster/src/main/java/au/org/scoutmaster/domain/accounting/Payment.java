package au.org.scoutmaster.domain.accounting;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Payment
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** 
	 * The date this payment record was created.
	 */
	Date created;
	
	/**
	 * The date the payment was made.
	 */
	Date paymentDate;
	
	/**
	 * The set of invoices this payment applies to.
	 */
	@ManyToMany
	List<Invoice> invoices;
	
	/**
	 * The amount paid 
	 */
	MoneyWithTax amount;
	
	/**
	 * A reference for the payment. Typically a cheque no. or a bank transaction id
	 */
	String reference;
	
	/**
	 * A note that optionally describes the purpose of the payment.
	 */
	String note;
}
