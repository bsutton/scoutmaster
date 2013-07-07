package au.org.scoutmaster.domain.accounting;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class CreditNote
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	Date created;

	@OneToMany(cascade = CascadeType.ALL)
	List<CreditNoteLine> creditNoteLines = new ArrayList<>();
	
	/**
	 * The invoice this CreditNote is attached to.
	 */
	@OneToOne
	Invoice associatedInvoice;
}
