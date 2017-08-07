package au.org.scoutmaster.domain.security;

import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.security.Action;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-08-07T16:51:55.161+1000")
@StaticMetamodel(Permission.class)
public class Permission_ extends BaseEntity_ {
	public static volatile SingularAttribute<Permission, Feature> feature;
	public static volatile SingularAttribute<Permission, Action> action;
	public static volatile SetAttribute<Permission, SecurityRole> accessibleBy;
	public static volatile SingularAttribute<Permission, Boolean> editedByUser;
	public static volatile SingularAttribute<Permission, Integer> roleHash;
}
