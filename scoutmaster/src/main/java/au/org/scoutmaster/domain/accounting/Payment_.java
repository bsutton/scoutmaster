package au.org.scoutmaster.domain.accounting;

import au.org.scoutmaster.domain.BaseEntity_;
import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-06-29T20:00:48.637+1000")
@StaticMetamodel(Payment.class)
public class Payment_ extends BaseEntity_ {
	public static volatile SingularAttribute<Payment, Date> paymentDate;
	public static volatile ListAttribute<Payment, Invoice> invoices;
	public static volatile SingularAttribute<Payment, MoneyWithTax> amount;
	public static volatile SingularAttribute<Payment, String> reference;
	public static volatile SingularAttribute<Payment, String> note;
}
