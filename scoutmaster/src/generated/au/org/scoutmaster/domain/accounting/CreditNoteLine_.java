package au.org.scoutmaster.domain.accounting;

import java.math.BigDecimal;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.BaseEntity_;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(CreditNoteLine.class)
public class CreditNoteLine_ extends BaseEntity_
{

	public static volatile SingularAttribute<CreditNoteLine, CreditNote> creditNote;
	public static volatile SingularAttribute<CreditNoteLine, Product> product;
	public static volatile SingularAttribute<CreditNoteLine, BigDecimal> quantity;
	public static volatile SingularAttribute<CreditNoteLine, MoneyWithTax> lineTotal;
	public static volatile SingularAttribute<CreditNoteLine, MoneyWithTax> itemCost;
	public static volatile SingularAttribute<CreditNoteLine, Integer> ordinal;

}