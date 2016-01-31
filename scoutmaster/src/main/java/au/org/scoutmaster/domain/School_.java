package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-01-31T13:50:58.569+1100")
@StaticMetamodel(School.class)
public class School_ extends BaseEntity_ {
	public static volatile SingularAttribute<School, String> name;
	public static volatile SingularAttribute<School, String> description;
	public static volatile SingularAttribute<School, Address> primaryAddress;
	public static volatile ListAttribute<School, Address> locations;
	public static volatile SingularAttribute<School, Contact> principle;
	public static volatile SingularAttribute<School, Contact> advertisingContact;
	public static volatile ListAttribute<School, Contact> youth;
	public static volatile SingularAttribute<School, String> generalEmailAddress;
	public static volatile SingularAttribute<School, String> mainPhoneNo;
	public static volatile SingularAttribute<School, String> webAddress;
	public static volatile SingularAttribute<School, SchoolGender> schoolGender;
	public static volatile SingularAttribute<School, SchoolType> schoolType;
}
