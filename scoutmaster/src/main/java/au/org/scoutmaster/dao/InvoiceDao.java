package au.org.scoutmaster.dao;

import javax.persistence.Query;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.accounting.Invoice;

public class InvoiceDao extends JpaBaseDao<Invoice, Long> implements Dao<Invoice, Long>
{

	public InvoiceDao()
	{
		// inherit the default per request em.
	}

	

	@Override
	public JPAContainer<Invoice> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

	public Long getNextInvoice()
	{
		Long nextCredtiNote = -1L;

		final Long highestNo = findHighestNo();
		nextCredtiNote = highestNo + 1;

		return nextCredtiNote;

	}

	public Long findHighestNo()
	{
		final Query query = JpaBaseDao.getEntityManager().createQuery("select max(i.InvoiceNumber) from Invoice i", Integer.class);
		return (Long) query.getSingleResult();
	}
}
