package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-27T23:08:58.264+1000")
@StaticMetamodel(Address.class)
public class Address_ extends BaseEntity_ {
	public static volatile SingularAttribute<Address, String> street;
	public static volatile SingularAttribute<Address, String> city;
	public static volatile SingularAttribute<Address, String> postcode;
	public static volatile SingularAttribute<Address, String> state;
}
