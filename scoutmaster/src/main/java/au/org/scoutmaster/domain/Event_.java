package au.org.scoutmaster.domain;

import au.com.vaadinutils.fields.Color;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-01-31T18:30:51.305+1100")
@StaticMetamodel(Event.class)
public class Event_ extends BaseEntity_ {
	public static volatile SingularAttribute<Event, String> subject;
	public static volatile SingularAttribute<Event, String> details;
	public static volatile SingularAttribute<Event, Boolean> allDayEvent;
	public static volatile SingularAttribute<Event, Date> eventStartDateTime;
	public static volatile SingularAttribute<Event, Date> eventEndDateTime;
	public static volatile SingularAttribute<Event, Address> location;
	public static volatile ListAttribute<Event, Contact> coordinators;
	public static volatile ListAttribute<Event, Document> documents;
	public static volatile SingularAttribute<Event, Color> color;
}
