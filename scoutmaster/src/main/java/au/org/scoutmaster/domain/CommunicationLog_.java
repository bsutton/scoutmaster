package au.org.scoutmaster.domain;

import au.org.scoutmaster.domain.security.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-08-07T16:53:32.879+1000")
@StaticMetamodel(CommunicationLog.class)
public class CommunicationLog_ extends BaseEntity_ {
	public static volatile SingularAttribute<CommunicationLog, String> guid;
	public static volatile SingularAttribute<CommunicationLog, CommunicationType> type;
	public static volatile SingularAttribute<CommunicationLog, User> addedBy;
	public static volatile SingularAttribute<CommunicationLog, Contact> withContact;
	public static volatile SingularAttribute<CommunicationLog, Date> activityDate;
	public static volatile SingularAttribute<CommunicationLog, String> subject;
	public static volatile SingularAttribute<CommunicationLog, String> details;
}
