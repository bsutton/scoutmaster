package au.org.scoutmaster.domain.access;

import au.org.scoutmaster.domain.BaseEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-09-19T09:12:29.089+1000")
@StaticMetamodel(User.class)
public class User_ extends BaseEntity_ {
	public static volatile SingularAttribute<User, String> username;
	public static volatile SingularAttribute<User, String> saltedPassword;
	public static volatile SingularAttribute<User, String> emailAddress;
	public static volatile SingularAttribute<User, Boolean> enabled;
	public static volatile SingularAttribute<User, Boolean> deleted;
	public static volatile ListAttribute<User, Role> belongsTo;
	public static volatile SingularAttribute<User, String> password;
}
