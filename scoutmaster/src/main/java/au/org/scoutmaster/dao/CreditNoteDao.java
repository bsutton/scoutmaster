package au.org.scoutmaster.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import au.org.scoutmaster.domain.accounting.CreditNote;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class CreditNoteDao extends JpaBaseDao<CreditNote, Long> implements Dao<CreditNote, Long>
{

	public CreditNoteDao()
	{
		// inherit the default per request em.
	}

	public CreditNoteDao(EntityManager em)
	{
		super(em);
	}

	@Override
	public JPAContainer<CreditNote> makeJPAContainer()
	{
		return super.makeJPAContainer(CreditNote.class);
	}

	public Long getNextCreditNote()
	{
		Long nextCredtiNote = -1L;

		Long highestNo = findHighestNo();
		nextCredtiNote = highestNo + 1;

		return nextCredtiNote;

	}

	public Long findHighestNo()
	{
		Query query = entityManager.createQuery("select max(cn.creditNoteNumber) from CreditNote cn", Integer.class);
		return (Long) query.getSingleResult();
	}
}
