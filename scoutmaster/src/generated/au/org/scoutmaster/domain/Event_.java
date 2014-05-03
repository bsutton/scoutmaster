package au.org.scoutmaster.domain;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(Event.class)
public class Event_ extends BaseEntity_
{

	public static volatile SingularAttribute<Event, Date> eventEndDateTime;
	public static volatile SingularAttribute<Event, Boolean> allDayEvent;
	public static volatile SingularAttribute<Event, Boolean> sectionMeeting;
	public static volatile SingularAttribute<Event, Color> color;
	public static volatile ListAttribute<Event, Document> documents;
	public static volatile SingularAttribute<Event, String> subject;
	public static volatile ListAttribute<Event, Contact> coordinators;
	public static volatile SingularAttribute<Event, String> details;
	public static volatile SingularAttribute<Event, Address> location;
	public static volatile SingularAttribute<Event, SectionType> sectionType;
	public static volatile SingularAttribute<Event, Date> eventStartDateTime;

}