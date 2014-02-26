package au.org.scoutmaster.domain.access;

import au.org.scoutmaster.domain.BaseEntity_;
import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-02-26T22:43:35.082+1100")
@StaticMetamodel(SessionHistory.class)
public class SessionHistory_ extends BaseEntity_ {
	public static volatile SingularAttribute<SessionHistory, Date> start;
	public static volatile SingularAttribute<SessionHistory, Date> end;
	public static volatile SingularAttribute<SessionHistory, User> user;
}
