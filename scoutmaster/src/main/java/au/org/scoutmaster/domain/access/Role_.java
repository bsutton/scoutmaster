package au.org.scoutmaster.domain.access;

import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.security.eRole;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-02-27T20:54:00.201+1100")
@StaticMetamodel(Role.class)
public class Role_ extends BaseEntity_ {
	public static volatile SingularAttribute<Role, eRole> erole;
	public static volatile SingularAttribute<Role, String> description;
	public static volatile ListAttribute<Role, Permission> permitted;
}
