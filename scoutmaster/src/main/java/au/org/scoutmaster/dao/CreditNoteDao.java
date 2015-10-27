package au.org.scoutmaster.dao;

import javax.persistence.Query;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.accounting.CreditNote;

public class CreditNoteDao extends JpaBaseDao<CreditNote, Long> implements Dao<CreditNote, Long>
{

	public CreditNoteDao()
	{
		// inherit the default per request em.
	}


	@Override
	public JPAContainer<CreditNote> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

	public Long getNextCreditNote()
	{
		Long nextCredtiNote = -1L;

		final Long highestNo = findHighestNo();
		nextCredtiNote = highestNo + 1;

		return nextCredtiNote;

	}

	public Long findHighestNo()
	{
		final Query query = JpaBaseDao.getEntityManager().createQuery("select max(cn.creditNoteNumber) from CreditNote cn",
				Integer.class);
		return (Long) query.getSingleResult();
	}
}
