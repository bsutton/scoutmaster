package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-27T23:08:58.282+1000")
@StaticMetamodel(School.class)
public class School_ extends BaseEntity_ {
	public static volatile SingularAttribute<School, String> name;
	public static volatile SingularAttribute<School, String> description;
	public static volatile ListAttribute<School, Address> location;
	public static volatile SingularAttribute<School, Contact> principle;
	public static volatile SingularAttribute<School, Contact> advertisingContact;
	public static volatile ListAttribute<School, Contact> youth;
}