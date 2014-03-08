package au.org.scoutmaster.domain.access;

import au.org.scoutmaster.domain.BaseEntity_;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-03-08T13:11:02.193+1100")
@StaticMetamodel(SessionHistory.class)
public class SessionHistory_ extends BaseEntity_ {
	public static volatile SingularAttribute<SessionHistory, Date> start;
	public static volatile SingularAttribute<SessionHistory, Date> end;
	public static volatile SingularAttribute<SessionHistory, User> user;
}
