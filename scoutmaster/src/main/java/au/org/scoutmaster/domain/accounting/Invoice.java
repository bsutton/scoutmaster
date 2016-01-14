package au.org.scoutmaster.domain.accounting;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.InvoiceDao;
import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.domain.Contact;

@Entity
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "Invoice")
@Access(AccessType.FIELD)
public class Invoice extends BaseEntity
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
	@ManyToOne(targetEntity = Contact.class)
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

	@OneToMany(targetEntity = InvoiceLine.class)
	List<InvoiceLine> invoiceLines = new ArrayList<>();

	/**
	 * The set of payments attached to this invoice.
	 */
	@ManyToMany(targetEntity = Payment.class)
	List<Payment> payments = new ArrayList<>();

	/**
	 * A descriptive note that appears on the invoice.
	 */
	String notes;

	@Override
	public String getName()
	{
		return Invoice.INVOICED_PREFIX + this.invoiceNo;
	}

	@PrePersist
	private void prePersist()
	{
		final InvoiceDao daoInvoice = new DaoFactory().getDao(InvoiceDao.class);
		this.invoiceNo = daoInvoice.getNextInvoice();
	}

}
