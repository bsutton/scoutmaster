package au.org.scoutmaster.domain.accounting;

import au.org.scoutmaster.domain.BaseEntity_;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-06-29T20:00:48.639+1000")
@StaticMetamodel(PurchaseLine.class)
public class PurchaseLine_ extends BaseEntity_ {
	public static volatile SingularAttribute<PurchaseLine, Purchase> purchase;
	public static volatile SingularAttribute<PurchaseLine, Integer> ordinal;
	public static volatile SingularAttribute<PurchaseLine, BigDecimal> quantity;
	public static volatile SingularAttribute<PurchaseLine, String> description;
	public static volatile SingularAttribute<PurchaseLine, MoneyWithTax> itemCost;
	public static volatile SingularAttribute<PurchaseLine, MoneyWithTax> lineTotal;
}
