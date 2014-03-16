package au.org.scoutmaster.domain.accounting;

import java.math.BigDecimal;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.BaseEntity_;

@Generated(value="Dali", date="2014-02-26T22:43:35.116+1100")
@StaticMetamodel(PurchaseLine.class)
public class PurchaseLine_ extends BaseEntity_ {
	public static volatile SingularAttribute<PurchaseLine, Purchase> purchase;
	public static volatile SingularAttribute<PurchaseLine, Integer> ordinal;
	public static volatile SingularAttribute<PurchaseLine, BigDecimal> quantity;
	public static volatile SingularAttribute<PurchaseLine, String> description;
	public static volatile SingularAttribute<PurchaseLine, MoneyWithTax> itemCost;
	public static volatile SingularAttribute<PurchaseLine, MoneyWithTax> lineTotal;
}
