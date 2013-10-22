package au.org.scoutmaster.domain.accounting;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import au.org.scoutmaster.dao.InvoiceDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.domain.Contact;


@Entity
@Table(name="Invoice")
public class Invoice  extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	private static String INVOICED_PREFIX = "V-";


	/**
	 * The date the invoice was created.
	 */
	Date created;
	
	Long invoiceNo;
	
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
	Integer terms;
	
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

	@Override
	public String getName()
	{
		return INVOICED_PREFIX + invoiceNo;
	}
	
	@PrePersist
	private void prePersist()
	{
		InvoiceDao daoInvoice = new DaoFactory().getDao(InvoiceDao.class);
		invoiceNo = daoInvoice.getNextInvoice();
	}

}
