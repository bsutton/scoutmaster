package au.org.scoutmaster.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.accounting.Invoice;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class InvoiceDao extends JpaBaseDao<Invoice, Long> implements Dao<Invoice, Long>
{

	public InvoiceDao()
	{
		// inherit the default per request em.
	}

	public InvoiceDao(EntityManager em)
	{
		super(em);
	}

	@Override
	public JPAContainer<Invoice> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

	public Long getNextInvoice()
	{
		Long nextCredtiNote = -1L;

		Long highestNo = findHighestNo();
		nextCredtiNote = highestNo + 1;

		return nextCredtiNote;

	}

	public Long findHighestNo()
	{
		Query query = entityManager.createQuery("select max(i.InvoiceNumber) from Invoice i", Integer.class);
		return (Long) query.getSingleResult();
	}
}
