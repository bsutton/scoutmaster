package au.org.scoutmaster.domain.security;

import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.security.eSecurityRole;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-08-07T16:51:55.162+1000")
@StaticMetamodel(SecurityRole.class)
public class SecurityRole_ extends BaseEntity_ {
	public static volatile SingularAttribute<SecurityRole, eSecurityRole> erole;
	public static volatile SingularAttribute<SecurityRole, String> description;
	public static volatile ListAttribute<SecurityRole, Permission> permitted;
}
