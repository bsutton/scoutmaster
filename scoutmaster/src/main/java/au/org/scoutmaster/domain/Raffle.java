package au.org.scoutmaster.domain;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.hibernate.validator.constraints.NotBlank;

import au.org.scoutmaster.domain.accounting.Money;

/**
 * Represents a Raffle that is being held by the group.
 *
 * @author bsutton
 *
 */
@Entity(name = "Raffle")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "Raffle")
@Access(AccessType.FIELD)
public class Raffle extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The name of the raffle (e.g. Monster raffle March 2013
	 */
	@NotBlank
	@Column(unique = true)
	String name;

	/**
	 * Any special notes about the raffle
	 */
	String notes;

	/**
	 * Date the raffle commences
	 */
	Date startDate = new Date(new java.util.Date().getTime());

	/**
	 * The date the raffle tickets needs to be collected by.
	 */
	Date collectionsDate;

	/**
	 * The date the tickets need to be returned to branch.
	 */
	Date returnDate;

	/**
	 * Person in the group who is responsible for running the raffle.
	 */
	@ManyToOne
	Contact groupRaffleManager;

	/**
	 * The contact at branch who is responsible for the raffle or our primary
	 * contact for raffle matters.
	 */
	@ManyToOne
	Contact branchRaffleContact;

	/**
	 * Any non-numeric prefix used for the raffle book no.s
	 */
	String raffleNoPrefix = new String();

	/**
	 * The no. of tickets in each book.
	 */
	Integer ticketsPerBook = new Integer(10);

	/*
	 * Transient value used to track the no. of tickets sold
	 */
	Integer totalTicketsSold = new Integer(0);

	/**
	 * Transient used to track the number of tickets which have been allocated
	 * to a contact but not returned.
	 */
	Integer ticketsOutstanding = new Integer(0);

	/**
	 * The sale price of each ticket
	 */
	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "fixedDoubleValue", column = @Column(name = "salePricePerTicketMoneyValue") ),
			@AttributeOverride(name = "precision", column = @Column(name = "salePricePerTicketMoneyPrecision") ) })
	Money salePricePerTicket;

	/**
	 * The revenue target for the raffle.
	 */
	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "fixedDoubleValue", column = @Column(name = "revenueTargetMoneyValue") ),
			@AttributeOverride(name = "precision", column = @Column(name = "revenueTargetMoneyPrecision") ) })
	Money revenueTarget;

	/*
	 * Transient value used to track the total revenue raised by the raffle.
	 */
	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "fixedDoubleValue", column = @Column(name = "revenueRaisedMoneyValue") ),
			@AttributeOverride(name = "precision", column = @Column(name = "revenueRaisedMoneyPrecision") ) })
	Money revenueRaised;

	/**
	 * The set of available books (books recieved from branch but not allocated
	 * to a contact.
	 */
	@OneToMany(mappedBy = "raffle", orphanRemoval = true)
	private Set<RaffleBook> available = new HashSet<>();

	/**
	 * The set of allocations. Allocations represent a set of books that have
	 * been allocated to contacts.
	 */
	@OneToMany(mappedBy = "raffle", orphanRemoval = true)
	private Set<RaffleAllocation> allocated = new HashSet<>();

	@Override
	public String getName()
	{
		return this.name;
	}

	public void addRaffleBook(final RaffleBook child)
	{
		this.available.add(child);
	}

	public int getTicketsPerBook()
	{
		return this.ticketsPerBook;
	}

	public void addRaffleAllocation(final RaffleAllocation child)
	{
		this.allocated.add(child);

	}

	public Set<RaffleBook> getImportedBooks()
	{
		return this.available;
	}

}
