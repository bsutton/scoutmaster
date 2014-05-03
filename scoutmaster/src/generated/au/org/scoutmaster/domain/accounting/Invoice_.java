package au.org.scoutmaster.domain.accounting;

import java.sql.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.domain.Contact;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(Invoice.class)
public class Invoice_ extends BaseEntity_
{

	public static volatile SingularAttribute<Invoice, String> notes;
	public static volatile ListAttribute<Invoice, InvoiceLine> invoiceLines;
	public static volatile SingularAttribute<Invoice, Integer> terms;
	public static volatile SingularAttribute<Invoice, Date> created;
	public static volatile SingularAttribute<Invoice, Contact> billTo;
	public static volatile ListAttribute<Invoice, Payment> payments;
	public static volatile SingularAttribute<Invoice, Long> invoiceNo;
	public static volatile SingularAttribute<Invoice, Date> invoiceDate;

}