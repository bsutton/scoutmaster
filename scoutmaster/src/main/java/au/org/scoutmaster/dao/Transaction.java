package au.org.scoutmaster.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class Transaction implements AutoCloseable
{
	private final EntityTransaction transaction;

	public Transaction(final EntityManager em)
	{
		this.transaction = em.getTransaction();
		this.transaction.begin();
	}

	@Override
	public void close()
	{
		if (this.transaction.isActive())
		{
			rollback();
		}
	}

	private void rollback()
	{
		this.transaction.rollback();
	}

	public void commit()
	{
		this.transaction.commit();

	}

	/*
	 * Begins a transaction. You don't normally need to call this as the ctor
	 * automatically calls begin. If however you want to re-use the transaction
	 * in a try/finally block you can call commit and then begin again.
	 */
	public void begin()
	{
		if (!this.transaction.isActive())
		{
			throw new IllegalStateException("Begin has already been called on the transaction");
		}

		this.transaction.begin();
	}

}
