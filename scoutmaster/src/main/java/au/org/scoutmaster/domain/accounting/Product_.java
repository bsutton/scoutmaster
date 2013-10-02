package au.org.scoutmaster.domain.accounting;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.domain.Organisation;

@Generated(value="Dali", date="2013-09-19T09:12:29.117+1000")
@StaticMetamodel(Product.class)
public class Product_ extends BaseEntity_ {
	public static volatile SingularAttribute<Product, String> name;
	public static volatile SingularAttribute<Product, String> desription;
	public static volatile SingularAttribute<Product, MoneyWithTax> cost;
	public static volatile SingularAttribute<Product, MoneyWithTax> charge;
	public static volatile SingularAttribute<Product, Organisation> supplier;
}
