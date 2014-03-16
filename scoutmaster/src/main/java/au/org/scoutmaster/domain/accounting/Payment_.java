package au.org.scoutmaster.domain.accounting;

import java.sql.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.BaseEntity_;

@Generated(value="Dali", date="2014-02-26T22:43:35.108+1100")
@StaticMetamodel(Payment.class)
public class Payment_ extends BaseEntity_ {
	public static volatile SingularAttribute<Payment, Date> paymentDate;
	public static volatile ListAttribute<Payment, Invoice> invoices;
	public static volatile SingularAttribute<Payment, MoneyWithTax> amount;
	public static volatile SingularAttribute<Payment, String> reference;
	public static volatile SingularAttribute<Payment, String> note;
}
