package au.org.scoutmaster.domain.accounting;

import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.domain.Contact;
import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-03-16T17:00:13.832+1100")
@StaticMetamodel(Invoice.class)
public class Invoice_ extends BaseEntity_ {
	public static volatile SingularAttribute<Invoice, Date> created;
	public static volatile SingularAttribute<Invoice, Long> invoiceNo;
	public static volatile SingularAttribute<Invoice, Contact> billTo;
	public static volatile SingularAttribute<Invoice, Date> invoiceDate;
	public static volatile SingularAttribute<Invoice, Integer> terms;
	public static volatile ListAttribute<Invoice, InvoiceLine> invoiceLines;
	public static volatile ListAttribute<Invoice, Payment> payments;
	public static volatile SingularAttribute<Invoice, String> notes;
}
