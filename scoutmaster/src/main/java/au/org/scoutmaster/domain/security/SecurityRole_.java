package au.org.scoutmaster.domain.security;

import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.security.eSecurityRole;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-03-12T10:27:58.486+1100")
@StaticMetamodel(SecurityRole.class)
public class SecurityRole_ extends BaseEntity_ {
	public static volatile SingularAttribute<SecurityRole, eSecurityRole> erole;
	public static volatile SingularAttribute<SecurityRole, String> description;
	public static volatile ListAttribute<SecurityRole, Permission> permitted;
}
