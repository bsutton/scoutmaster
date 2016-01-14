package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import au.com.vaadinutils.crud.ChildCrudEntity;
import au.com.vaadinutils.dao.JpaEntityHelper;
import au.org.scoutmaster.domain.accounting.Money;

/**
 * Represents a Raffle ticket Book that has been sent from Branch to be sold.
 *
 * @author bsutton
 *
 */
@Entity(name = "RaffleBook")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "RaffleBook")
@Access(AccessType.FIELD)
@NamedQueries(
{ @NamedQuery(name = RaffleBook.FIND_ALL_UNALLOCATED, query = "SELECT rafflebook FROM RaffleBook rafflebook WHERE rafflebook.raffle = :raffle and rafflebook.raffleAllocation is null order by rafflebook.firstNo"),
		@NamedQuery(name = RaffleBook.FIND_BY_ALLOCATION, query = "SELECT rafflebook FROM RaffleBook rafflebook WHERE rafflebook.raffleAllocation.id  = :raffleAllocationId") })
public class RaffleBook extends BaseEntity implements ChildCrudEntity
{
	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL_UNALLOCATED = "RaffleBook.findAllUnallocated";

	public static final String FIND_BY_ALLOCATION = "RaffleBook.findByAllocation";

	/**
	 * Entities used in child cruds must have a guid to help uniquely identify
	 * each child.
	 */
	@NotNull
	@Column(updatable = false)
	String guid = JpaEntityHelper.getGuid(this);

	/**
	 * The raffle this book is attached to.
	 */
	@ManyToOne(targetEntity = Raffle.class)
	Raffle raffle;

	/**
	 * The allocation that this book was issued to a Contact under. Will be null
	 * if the book hasn't been issued.
	 */
	@ManyToOne(optional = true)
	RaffleAllocation raffleAllocation;

	/**
	 * The no. of tickets in the book.
	 */
	private Integer ticketCount = new Integer(10);

	/**
	 * The first ticket no. in the book.
	 */
	private Integer firstNo = new Integer(0);

	/**
	 * The contact the book was allocated to.
	 */
	// @ManyToOne(targetEntity=Contact.class)
	// private Contact allocatedTo;
	//
	// /**
	// * The date the book was allocated to a Contact.
	// */
	// private Date dateAllocated;
	//
	// /**
	// * The contact that issued the book to the 'allocatedTo' contact.
	// */
	// private Contact issuedBy;
	//
	// /**
	// * The date the book was actually given to the Contact.
	// */
	// private Date dateIssued;

	/**
	 * The no. of tickets that have been returned.
	 */
	private Integer ticketsReturned = new Integer(0);

	/**
	 * The amount of money returned (in $) for this book.
	 */
	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "fixedDoubleValue", column = @Column(name = "amountReturnedMoneyValue") ),
			@AttributeOverride(name = "precision", column = @Column(name = "amountReturnedMoneyPrecision") ) })
	private Money amountReturned = new Money(0);

	/**
	 * The date the money and tickets stubs were returned.
	 */
	private Date dateReturned;

	/**
	 * The contact that collected the money/tickets
	 */
	private Contact collectedBy;

	/**
	 * True if a receipt has been issued.
	 */
	private Boolean receiptIssued = new Boolean(false);

	/**
	 * Any special notes about the book
	 */
	private String notes;

	@Override
	public String getName()
	{
		return this.firstNo + (this.raffleAllocation == null ? " Available"
				: " Allocated To: " + this.raffleAllocation.getAllocatedTo().getFullname());
	}

	public Integer getTicketCount()
	{
		return this.ticketCount;
	}

	public void setTicketCount(final Integer ticketCount)
	{
		this.ticketCount = ticketCount;
	}

	public Integer getFirstNo()
	{
		return this.firstNo;
	}

	public void setFirstNo(final Integer firstNo)
	{
		this.firstNo = firstNo;
	}

	public Integer getTicketsReturned()
	{
		return this.ticketsReturned;
	}

	public void setTicketsReturned(final Integer ticketsReturned)
	{
		this.ticketsReturned = ticketsReturned;
	}

	public Money getAmountReturned()
	{
		return this.amountReturned;
	}

	public void setAmountReturned(final Money amountReturned)
	{
		this.amountReturned = amountReturned;
	}

	public Date getDateReturned()
	{
		return this.dateReturned;
	}

	public void setDateReturned(final Date dateReturned)
	{
		this.dateReturned = dateReturned;
	}

	public Contact getCollectedBy()
	{
		return this.collectedBy;
	}

	public void setCollectedBy(final Contact collectedBy)
	{
		this.collectedBy = collectedBy;
	}

	public Boolean getReceiptIssued()
	{
		return this.receiptIssued;
	}

	public void setReceiptIssued(final Boolean receiptIssued)
	{
		this.receiptIssued = receiptIssued;
	}

	public String getNotes()
	{
		return this.notes;
	}

	public void setNotes(final String notes)
	{
		this.notes = notes;
	}

	public void setRaffle(final Raffle raffle)
	{
		this.raffle = raffle;
	}

	public RaffleAllocation getRaffleAllocation()
	{
		return this.raffleAllocation;
	}

	public void setRaffleAllocation(final RaffleAllocation allocation)
	{
		this.raffleAllocation = allocation;

	}

	public int getLastTicketNo()
	{
		return this.firstNo + this.ticketCount - 1;
	}

	public String getGUID()
	{
		return guid;
	}

	@Override
	public String toString()
	{
		return "First Ticket No.: " + this.firstNo + ", Last Ticket No: " + getLastTicketNo();
	}

	@Override
	public String getGuid()
	{
		return guid;
	}

}
