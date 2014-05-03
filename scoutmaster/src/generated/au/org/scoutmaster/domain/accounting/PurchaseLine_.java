package au.org.scoutmaster.domain.accounting;

import java.math.BigDecimal;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.BaseEntity_;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(PurchaseLine.class)
public class PurchaseLine_ extends BaseEntity_
{

	public static volatile SingularAttribute<PurchaseLine, BigDecimal> quantity;
	public static volatile SingularAttribute<PurchaseLine, MoneyWithTax> lineTotal;
	public static volatile SingularAttribute<PurchaseLine, MoneyWithTax> itemCost;
	public static volatile SingularAttribute<PurchaseLine, Purchase> purchase;
	public static volatile SingularAttribute<PurchaseLine, String> description;
	public static volatile SingularAttribute<PurchaseLine, Integer> ordinal;

}