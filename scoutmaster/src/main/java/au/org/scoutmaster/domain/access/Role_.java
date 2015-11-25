package au.org.scoutmaster.domain.access;

import au.org.scoutmaster.domain.BaseEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-11-24T19:08:00.993+1100")
@StaticMetamodel(Role.class)
public class Role_ extends BaseEntity_ {
	public static volatile SingularAttribute<Role, String> name;
	public static volatile SingularAttribute<Role, String> description;
	public static volatile ListAttribute<Role, Feature> permitted;
}
