package au.org.scoutmaster.domain;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import au.com.vaadinutils.crud.ChildCrudEntity;
import au.com.vaadinutils.dao.JpaEntityHelper;

/**
 * Represents an Allocation of Raffle Books to a contact from a Raffle.
 *
 * @author bsutton
 *
 */
@Entity(name = "RaffleAllocation")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "RaffleAllocation")
@Access(AccessType.FIELD)
public class RaffleAllocation extends BaseEntity implements ChildCrudEntity
{
	private static final long serialVersionUID = 1L;

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
	 * The contact the book was allocated to.
	 */
	@ManyToOne(targetEntity = Contact.class)
	private Contact allocatedTo = new Contact();

	/**
	 * The date the book was allocated to a Contact.
	 */
	private Date dateAllocated = new Date(new java.util.Date().getTime());

	/**
	 * The contact that issued the book to the 'allocatedTo' contact.
	 */
	private Contact issuedBy;

	/**
	 * The date the book was actually given to the Contact.
	 */
	private Date dateIssued = new Date(new java.util.Date().getTime());

	/**
	 * Any special notes about the allocation
	 */
	private String notes;

	/**
	 * The set of raffle books allocated by this allocation.
	 */
	@OneToMany(mappedBy = "raffleAllocation")
	private Set<RaffleBook> books = new HashSet<>();

	@Override
	public String getName()
	{
		return "Allocated To: " + this.allocatedTo.getFullname() + " Date Allocated: " + this.dateIssued;
	}

	public Contact getAllocatedTo()
	{
		return this.allocatedTo;
	}

	public void setAllocatedTo(final Contact allocatedTo)
	{
		this.allocatedTo = allocatedTo;
	}

	public Date getDateAllocated()
	{
		return this.dateAllocated;
	}

	public void setDateAllocated(final Date dateAllocated)
	{
		this.dateAllocated = dateAllocated;
	}

	public Contact getIssuedBy()
	{
		return this.issuedBy;
	}

	public void setIssuedBy(final Contact issuedBy)
	{
		this.issuedBy = issuedBy;
	}

	public Date getDateIssued()
	{
		return this.dateIssued;
	}

	public void setDateIssued(final Date dateIssued)
	{
		this.dateIssued = dateIssued;
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

	public Set<RaffleBook> getBooks()
	{
		return this.books;
	}

	public void setBooks(final Set<RaffleBook> books)
	{
		this.books = books;
	}

	@Override
	public String toString()
	{
		return "To: " + (this.allocatedTo == null ? "(none)" : this.allocatedTo.getFullname()) + " By:"
				+ (this.issuedBy == null ? "none" : this.issuedBy.getFullname()) + " On: " + this.dateAllocated
				+ " Books: " + this.books.size();
	}

	public void addBook(final RaffleBook book)
	{
		this.books.add(book);

	}

	@Transient
	public Long getBookCount()
	{
		return new Long(this.books.size());
	}

	@Override
	public String getGuid()
	{
		return guid;
	}

}
