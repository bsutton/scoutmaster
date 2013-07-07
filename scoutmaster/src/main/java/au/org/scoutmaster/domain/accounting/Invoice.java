package au.org.scoutmaster.domain.accounting;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import au.org.scoutmaster.domain.Contact;


@Entity
public class Invoice
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * The date the invoice was created.
	 */
	Date created;
	
	String invoiceNo;
	
	/**
	 * The contact the invoice has been raised against.
	 */
	@ManyToOne
	Contact billTo;
	
	/**
	 * The date the invoice was raised.
	 */
	Date invoiceDate;
	
	/**
	 * The no. of days from the invoiceDate that the invoice is due.
	 */
	int terms;
	
	/**
	 * The date the invoice is due.
	 */
	@Transient
	Date dueDate;
	
	@OneToMany(cascade = CascadeType.ALL)
	List<InvoiceLine> invoiceLines = new ArrayList<>();
	
	/**
	 * The set of payments attached to this invoice.
	 */
	@ManyToMany
	List<Payment> payments = new ArrayList<>();
	
	/**
	 * A descriptive note that appears on the invoice.
	 */
	String notes;
}
