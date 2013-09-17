package au.org.scoutmaster.domain;

import java.sql.Time;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-09-17T12:57:50.411+1000")
@StaticMetamodel(SectionMeetingDefaults.class)
public class SectionMeetingDefaults_ extends BaseEntity_ {
	public static volatile SingularAttribute<SectionMeetingDefaults, Section> section;
	public static volatile SingularAttribute<SectionMeetingDefaults, Night> meetingNight;
	public static volatile SingularAttribute<SectionMeetingDefaults, Time> startTime;
	public static volatile SingularAttribute<SectionMeetingDefaults, Time> EndTime;
	public static volatile SingularAttribute<SectionMeetingDefaults, String> meetingSubject;
	public static volatile SingularAttribute<SectionMeetingDefaults, String> meetingDetails;
	public static volatile SingularAttribute<SectionMeetingDefaults, Address> location;
}
