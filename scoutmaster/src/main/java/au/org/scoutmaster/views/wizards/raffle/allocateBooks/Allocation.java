package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import java.util.List;

import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.RaffleBook;

public class Allocation
{
	/** The contact the allocation is being made to.
	 * 
	 */
	private Contact allocatedTo;
	
	/**
	 * The list of books to be allocated.
	 */
	private List<RaffleBook> books;

	public Allocation(Contact allocatedTo, List<RaffleBook> books)
	{
		this.allocatedTo = allocatedTo;
		this.books = books;
	}

	public Contact getAllocatedTo()
	{
		return this.allocatedTo;
	}

	public List<RaffleBook> getBooks()
	{
		return books;
	}
}
