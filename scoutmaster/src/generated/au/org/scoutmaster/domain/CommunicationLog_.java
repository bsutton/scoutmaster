package au.org.scoutmaster.domain;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.access.User;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(CommunicationLog.class)
public class CommunicationLog_ extends BaseEntity_
{

	public static volatile SingularAttribute<CommunicationLog, Contact> withContact;
	public static volatile SingularAttribute<CommunicationLog, Date> activityDate;
	public static volatile SingularAttribute<CommunicationLog, User> addedBy;
	public static volatile SingularAttribute<CommunicationLog, String> subject;
	public static volatile SingularAttribute<CommunicationLog, String> details;
	public static volatile SingularAttribute<CommunicationLog, CommunicationType> type;

}