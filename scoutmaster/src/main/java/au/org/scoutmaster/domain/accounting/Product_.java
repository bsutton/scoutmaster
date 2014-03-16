package au.org.scoutmaster.domain.accounting;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.domain.Organisation;

@Generated(value="Dali", date="2014-02-26T22:43:35.111+1100")
@StaticMetamodel(Product.class)
public class Product_ extends BaseEntity_ {
	public static volatile SingularAttribute<Product, String> name;
	public static volatile SingularAttribute<Product, String> desription;
	public static volatile SingularAttribute<Product, MoneyWithTax> cost;
	public static volatile SingularAttribute<Product, MoneyWithTax> charge;
	public static volatile SingularAttribute<Product, Organisation> supplier;
}
