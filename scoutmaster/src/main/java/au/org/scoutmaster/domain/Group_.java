package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-01-13T21:48:43.929+1100")
@StaticMetamodel(Group.class)
public class Group_ extends BaseEntity_ {
	public static volatile SingularAttribute<Group, String> name;
	public static volatile SingularAttribute<Group, GroupType> groupType;
	public static volatile SingularAttribute<Group, String> street;
	public static volatile SingularAttribute<Group, String> city;
	public static volatile SingularAttribute<Group, String> postcode;
	public static volatile SingularAttribute<Group, String> state;
	public static volatile SingularAttribute<Group, String> country;
	public static volatile SingularAttribute<Group, Phone> phone1;
	public static volatile SingularAttribute<Group, Phone> phone2;
	public static volatile SingularAttribute<Group, Phone> phone3;
}
