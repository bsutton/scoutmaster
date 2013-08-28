package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-27T23:08:58.278+1000")
@StaticMetamodel(Phone.class)
public class Phone_ extends BaseEntity_ {
	public static volatile SingularAttribute<Phone, PhoneType> phoneType;
	public static volatile SingularAttribute<Phone, Boolean> primaryPhone;
	public static volatile SingularAttribute<Phone, String> phoneNo;
}
