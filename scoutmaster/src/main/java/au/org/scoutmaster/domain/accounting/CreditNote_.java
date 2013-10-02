package au.org.scoutmaster.domain.accounting;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.BaseEntity_;

@Generated(value="Dali", date="2013-09-19T09:12:29.091+1000")
@StaticMetamodel(CreditNote.class)
public class CreditNote_ extends BaseEntity_ {
	public static volatile ListAttribute<CreditNote, CreditNoteLine> creditNoteLines;
	public static volatile SingularAttribute<CreditNote, Invoice> associatedInvoice;
}
