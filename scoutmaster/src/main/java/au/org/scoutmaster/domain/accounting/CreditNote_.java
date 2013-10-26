package au.org.scoutmaster.domain.accounting;

import au.org.scoutmaster.domain.BaseEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-10-23T14:21:31.718+1100")
@StaticMetamodel(CreditNote.class)
public class CreditNote_ extends BaseEntity_ {
	public static volatile SingularAttribute<CreditNote, Long> creditNoteNumber;
	public static volatile ListAttribute<CreditNote, CreditNoteLine> creditNoteLines;
	public static volatile SingularAttribute<CreditNote, Invoice> associatedInvoice;
}
