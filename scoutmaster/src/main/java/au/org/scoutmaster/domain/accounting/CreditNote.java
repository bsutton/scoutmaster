package au.org.scoutmaster.domain.accounting;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import au.org.scoutmaster.dao.CreditNoteDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.BaseEntity;

@Entity
@Table(name="CreditNote")
public class CreditNote  extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	private static String CREDIT_NOTE_PREFIX = "CN-";

	Long creditNoteNumber;
	
	@OneToMany(cascade = CascadeType.ALL)
	List<CreditNoteLine> creditNoteLines = new ArrayList<>();
	
	/**
	 * The invoice this CreditNote is attached to.
	 */
	@OneToOne
	Invoice associatedInvoice;

	@Override
	public String getName()
	{
		return CREDIT_NOTE_PREFIX + creditNoteNumber;
	}
	
	@PrePersist
	private void prePersist()
	{
		CreditNoteDao daoCreditNote = new DaoFactory().getCreditNoteDao();
		creditNoteNumber = daoCreditNote.getNextCreditNote();
	}
}
