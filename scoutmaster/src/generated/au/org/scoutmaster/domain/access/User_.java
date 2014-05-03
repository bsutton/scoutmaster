package au.org.scoutmaster.domain.access;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.BaseEntity_;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(User.class)
public class User_ extends BaseEntity_
{

	public static volatile SingularAttribute<User, String> saltedPassword;
	public static volatile SingularAttribute<User, String> firstname;
	public static volatile SingularAttribute<User, String> emailAddress;
	public static volatile SingularAttribute<User, Boolean> deleted;
	public static volatile SingularAttribute<User, String> surname;
	public static volatile SingularAttribute<User, String> senderMobile;
	public static volatile ListAttribute<User, Role> belongsTo;
	public static volatile SingularAttribute<User, Boolean> enabled;
	public static volatile SingularAttribute<User, String> emailSignature;
	public static volatile SingularAttribute<User, String> username;

}