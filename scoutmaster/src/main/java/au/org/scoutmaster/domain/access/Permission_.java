package au.org.scoutmaster.domain.access;

import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.security.Action;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-08-07T16:55:00.371+1000")
@StaticMetamodel(Permission.class)
public class Permission_ extends BaseEntity_ {
	public static volatile SingularAttribute<Permission, String> featureName;
	public static volatile SingularAttribute<Permission, Action> action;
	public static volatile ListAttribute<Permission, Role> accessibleBy;
	public static volatile SingularAttribute<Permission, Boolean> editedByUser;
	public static volatile SingularAttribute<Permission, Integer> roleHash;
}
