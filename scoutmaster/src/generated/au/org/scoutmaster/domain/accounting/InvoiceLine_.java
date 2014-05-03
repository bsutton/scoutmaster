package au.org.scoutmaster.domain.accounting;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.BaseEntity_;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(InvoiceLine.class)
public class InvoiceLine_ extends BaseEntity_
{

	public static volatile SingularAttribute<InvoiceLine, Product> product;
	public static volatile SingularAttribute<InvoiceLine, FixedDouble> quantity;
	public static volatile SingularAttribute<InvoiceLine, MoneyWithTax> lineTotal;
	public static volatile SingularAttribute<InvoiceLine, MoneyWithTax> itemCost;
	public static volatile SingularAttribute<InvoiceLine, Invoice> invoice;
	public static volatile SingularAttribute<InvoiceLine, Integer> ordinal;

}