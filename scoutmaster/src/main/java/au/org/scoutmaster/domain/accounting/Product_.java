package au.org.scoutmaster.domain.accounting;

import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.domain.Organisation;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-06-29T20:00:48.638+1000")
@StaticMetamodel(Product.class)
public class Product_ extends BaseEntity_ {
	public static volatile SingularAttribute<Product, String> name;
	public static volatile SingularAttribute<Product, String> desription;
	public static volatile SingularAttribute<Product, MoneyWithTax> cost;
	public static volatile SingularAttribute<Product, MoneyWithTax> charge;
	public static volatile SingularAttribute<Product, Organisation> supplier;
}
