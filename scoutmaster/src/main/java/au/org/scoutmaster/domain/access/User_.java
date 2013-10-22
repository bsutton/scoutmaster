package au.org.scoutmaster.domain.access;

import au.org.scoutmaster.domain.BaseEntity_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-10-22T22:20:10.043+1100")
@StaticMetamodel(User.class)
public class User_ extends BaseEntity_ {
	public static volatile SingularAttribute<User, String> username;
	public static volatile SingularAttribute<User, String> saltedPassword;
	public static volatile SingularAttribute<User, String> emailAddress;
	public static volatile SingularAttribute<User, Boolean> enabled;
	public static volatile SingularAttribute<User, Boolean> deleted;
	public static volatile ListAttribute<User, Role> belongsTo;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, String> firstname;
	public static volatile SingularAttribute<User, String> surname;
}
