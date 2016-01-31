package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-01-31T13:56:23.258+1100")
@StaticMetamodel(Tag.class)
public class Tag_ extends BaseEntity_ {
	public static volatile SingularAttribute<Tag, String> name;
	public static volatile SingularAttribute<Tag, String> description;
	public static volatile SingularAttribute<Tag, Boolean> builtin;
	public static volatile SingularAttribute<Tag, Boolean> detachable;
	public static volatile SetAttribute<Tag, Contact> contacts;
}
