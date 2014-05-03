package au.org.scoutmaster.domain.access;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.BaseEntity_;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(LoginAttempt.class)
public class LoginAttempt_ extends BaseEntity_
{

	public static volatile SingularAttribute<LoginAttempt, Date> dateOfAttempt;
	public static volatile SingularAttribute<LoginAttempt, User> user;
	public static volatile SingularAttribute<LoginAttempt, Boolean> succeeded;

}