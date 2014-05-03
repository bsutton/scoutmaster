package au.org.scoutmaster.domain.accounting;

import java.sql.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.BaseEntity_;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(Payment.class)
public class Payment_ extends BaseEntity_
{

	public static volatile SingularAttribute<Payment, String> reference;
	public static volatile SingularAttribute<Payment, String> note;
	public static volatile SingularAttribute<Payment, MoneyWithTax> amount;
	public static volatile ListAttribute<Payment, Invoice> invoices;
	public static volatile SingularAttribute<Payment, Date> paymentDate;

}