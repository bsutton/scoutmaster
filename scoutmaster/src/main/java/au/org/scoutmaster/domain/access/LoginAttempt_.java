package au.org.scoutmaster.domain.access;

import au.org.scoutmaster.domain.BaseEntity_;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-15T13:07:13.706+1000")
@StaticMetamodel(LoginAttempt.class)
public class LoginAttempt_ extends BaseEntity_ {
	public static volatile SingularAttribute<LoginAttempt, User> user;
	public static volatile SingularAttribute<LoginAttempt, Date> dateOfAttempt;
	public static volatile SingularAttribute<LoginAttempt, Boolean> succeeded;
}
