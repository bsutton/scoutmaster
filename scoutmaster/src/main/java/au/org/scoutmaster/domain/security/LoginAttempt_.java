package au.org.scoutmaster.domain.security;

import au.org.scoutmaster.domain.BaseEntity_;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-08-07T16:51:55.160+1000")
@StaticMetamodel(LoginAttempt.class)
public class LoginAttempt_ extends BaseEntity_ {
	public static volatile SingularAttribute<LoginAttempt, User> user;
	public static volatile SingularAttribute<LoginAttempt, Date> dateOfAttempt;
	public static volatile SingularAttribute<LoginAttempt, Boolean> succeeded;
}
