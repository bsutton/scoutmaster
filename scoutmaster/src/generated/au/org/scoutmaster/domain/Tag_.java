package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(Tag.class)
public class Tag_ extends BaseEntity_
{

	public static volatile SingularAttribute<Tag, Boolean> detachable;
	public static volatile SingularAttribute<Tag, Boolean> builtin;
	public static volatile SingularAttribute<Tag, String> name;
	public static volatile SingularAttribute<Tag, String> description;
	public static volatile SetAttribute<Tag, Contact> contacts;

}