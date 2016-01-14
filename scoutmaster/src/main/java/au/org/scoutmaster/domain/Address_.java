package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-01-08T10:53:13.132+1100")
@StaticMetamodel(Address.class)
public class Address_ extends BaseEntity_ {
	public static volatile SingularAttribute<Address, String> street;
	public static volatile SingularAttribute<Address, String> city;
	public static volatile SingularAttribute<Address, String> postcode;
	public static volatile SingularAttribute<Address, String> state;
	public static volatile SingularAttribute<Address, String> country;
}
