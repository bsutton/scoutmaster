package au.org.scoutmaster.domain.accounting;

import au.org.scoutmaster.domain.BaseEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-03-16T17:00:13.836+1100")
@StaticMetamodel(InvoiceLine.class)
public class InvoiceLine_ extends BaseEntity_ {
	public static volatile SingularAttribute<InvoiceLine, Invoice> invoice;
	public static volatile SingularAttribute<InvoiceLine, Integer> ordinal;
	public static volatile SingularAttribute<InvoiceLine, FixedDouble> quantity;
	public static volatile SingularAttribute<InvoiceLine, Product> product;
	public static volatile SingularAttribute<InvoiceLine, MoneyWithTax> itemCost;
	public static volatile SingularAttribute<InvoiceLine, MoneyWithTax> lineTotal;
}
