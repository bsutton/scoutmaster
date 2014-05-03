package au.org.scoutmaster.domain.access;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.BaseEntity_;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(Role.class)
public class Role_ extends BaseEntity_
{

	public static volatile ListAttribute<Role, Feature> permitted;
	public static volatile SingularAttribute<Role, String> name;
	public static volatile SingularAttribute<Role, String> description;

}