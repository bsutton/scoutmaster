package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import java.util.List;

import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.RaffleBook;

public class Allocation
{
	/**
	 * The contact the allocation is being made to.
	 *
	 */
	private final Contact allocatedTo;

	/**
	 * The list of books to be allocated.
	 */
	private final List<RaffleBook> books;

	public Allocation(final Contact allocatedTo, final List<RaffleBook> books)
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
		return this.books;
	}

	@Override
	public String toString()
	{
		return this.allocatedTo.getFullname() + ", Book Count:" + this.books.size();
	}
}
