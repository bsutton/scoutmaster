package au.org.scoutmaster.domain.access;

import au.org.scoutmaster.domain.BaseEntity_;
import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-27T23:08:58.245+1000")
@StaticMetamodel(SessionHistory.class)
public class SessionHistory_ extends BaseEntity_ {
	public static volatile SingularAttribute<SessionHistory, Date> start;
	public static volatile SingularAttribute<SessionHistory, Date> end;
	public static volatile SingularAttribute<SessionHistory, User> user;
}