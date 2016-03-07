package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-03-07T20:04:46.873+1100")
@StaticMetamodel(ScoutGroup.class)
public class ScoutGroup_ extends BaseEntity_ {
	public static volatile SingularAttribute<ScoutGroup, String> name;
	public static volatile SingularAttribute<ScoutGroup, ScoutGroupType> scoutGroupType;
	public static volatile SingularAttribute<ScoutGroup, String> street;
	public static volatile SingularAttribute<ScoutGroup, String> city;
	public static volatile SingularAttribute<ScoutGroup, String> postcode;
	public static volatile SingularAttribute<ScoutGroup, String> state;
	public static volatile SingularAttribute<ScoutGroup, String> country;
	public static volatile SingularAttribute<ScoutGroup, Phone> phone1;
	public static volatile SingularAttribute<ScoutGroup, Phone> phone2;
	public static volatile SingularAttribute<ScoutGroup, Phone> phone3;
}
