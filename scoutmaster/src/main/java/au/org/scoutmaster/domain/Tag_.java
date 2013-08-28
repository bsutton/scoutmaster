package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-27T23:08:58.291+1000")
@StaticMetamodel(Tag.class)
public class Tag_ extends BaseEntity_ {
	public static volatile SingularAttribute<Tag, String> name;
	public static volatile SingularAttribute<Tag, String> description;
	public static volatile SingularAttribute<Tag, Boolean> builtin;
	public static volatile SingularAttribute<Tag, Boolean> detachable;
}
