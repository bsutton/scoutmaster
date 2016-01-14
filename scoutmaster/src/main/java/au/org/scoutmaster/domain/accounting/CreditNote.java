package au.org.scoutmaster.domain.accounting;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import au.org.scoutmaster.dao.CreditNoteDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.BaseEntity;

@Entity
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "CreditNote")
@Access(AccessType.FIELD)
public class CreditNote extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	private static String CREDIT_NOTE_PREFIX = "CN-";

	Long creditNoteNumber;

	@OneToMany(targetEntity = CreditNoteLine.class)
	List<CreditNoteLine> creditNoteLines = new ArrayList<>();

	/**
	 * The invoice this CreditNote is attached to.
	 */
	@OneToOne(targetEntity = Invoice.class, cascade =
	{ CascadeType.MERGE })
	Invoice associatedInvoice;

	@Override
	public String getName()
	{
		return CreditNote.CREDIT_NOTE_PREFIX + this.creditNoteNumber;
	}

	@PrePersist
	private void prePersist()
	{
		final CreditNoteDao daoCreditNote = new DaoFactory().getCreditNoteDao();
		this.creditNoteNumber = daoCreditNote.getNextCreditNote();
	}
}
