package au.org.scoutmaster.domain.accounting;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.BaseEntity_;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(CreditNote.class)
public class CreditNote_ extends BaseEntity_
{

	public static volatile ListAttribute<CreditNote, CreditNoteLine> creditNoteLines;
	public static volatile SingularAttribute<CreditNote, Long> creditNoteNumber;
	public static volatile SingularAttribute<CreditNote, Invoice> associatedInvoice;

}