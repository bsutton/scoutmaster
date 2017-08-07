package au.org.scoutmaster.domain.access;

import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.security.eRole;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-08-07T16:55:00.372+1000")
@StaticMetamodel(Role.class)
public class Role_ extends BaseEntity_ {
	public static volatile SingularAttribute<Role, eRole> erole;
	public static volatile SingularAttribute<Role, String> description;
	public static volatile ListAttribute<Role, Permission> permitted;
}
