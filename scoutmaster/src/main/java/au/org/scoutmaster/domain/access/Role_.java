package au.org.scoutmaster.domain.access;

import au.org.scoutmaster.domain.BaseEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-10-03T10:29:32.647+1000")
@StaticMetamodel(Role.class)
public class Role_ extends BaseEntity_ {
	public static volatile SingularAttribute<Role, String> name;
	public static volatile SingularAttribute<Role, String> description;
	public static volatile ListAttribute<Role, Feature> permitted;
}
