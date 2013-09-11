package au.org.scoutmaster.domain.accounting;

import au.org.scoutmaster.domain.BaseEntity_;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-09-11T21:56:55.438+1000")
@StaticMetamodel(InvoiceLine.class)
public class InvoiceLine_ extends BaseEntity_ {
	public static volatile SingularAttribute<InvoiceLine, Invoice> invoice;
	public static volatile SingularAttribute<InvoiceLine, Integer> ordinal;
	public static volatile SingularAttribute<InvoiceLine, BigDecimal> quantity;
	public static volatile SingularAttribute<InvoiceLine, Product> product;
	public static volatile SingularAttribute<InvoiceLine, MoneyWithTax> itemCost;
	public static volatile SingularAttribute<InvoiceLine, MoneyWithTax> lineTotal;
}
