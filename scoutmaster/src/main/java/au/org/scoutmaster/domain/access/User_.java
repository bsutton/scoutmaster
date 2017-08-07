package au.org.scoutmaster.domain.access;

import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.domain.ScoutGroup;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-08-07T16:55:00.373+1000")
@StaticMetamodel(User.class)
public class User_ extends BaseEntity_ {
	public static volatile SingularAttribute<User, ScoutGroup> scoutGroup;
	public static volatile SingularAttribute<User, String> username;
	public static volatile SingularAttribute<User, String> saltedPassword;
	public static volatile SingularAttribute<User, String> firstname;
	public static volatile SingularAttribute<User, String> lastname;
	public static volatile SingularAttribute<User, String> emailAddress;
	public static volatile SingularAttribute<User, Boolean> enabled;
	public static volatile SingularAttribute<User, Boolean> deleted;
	public static volatile SingularAttribute<User, String> senderMobile;
	public static volatile SingularAttribute<User, String> emailSignature;
	public static volatile ListAttribute<User, Role> belongsTo;
}
