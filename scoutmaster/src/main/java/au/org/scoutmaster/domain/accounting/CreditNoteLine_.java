package au.org.scoutmaster.domain.accounting;

import au.org.scoutmaster.domain.BaseEntity_;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-09-17T12:57:50.271+1000")
@StaticMetamodel(CreditNoteLine.class)
public class CreditNoteLine_ extends BaseEntity_ {
	public static volatile SingularAttribute<CreditNoteLine, CreditNote> creditNote;
	public static volatile SingularAttribute<CreditNoteLine, Integer> ordinal;
	public static volatile SingularAttribute<CreditNoteLine, BigDecimal> quantity;
	public static volatile SingularAttribute<CreditNoteLine, Product> product;
	public static volatile SingularAttribute<CreditNoteLine, MoneyWithTax> itemCost;
	public static volatile SingularAttribute<CreditNoteLine, MoneyWithTax> lineTotal;
}
