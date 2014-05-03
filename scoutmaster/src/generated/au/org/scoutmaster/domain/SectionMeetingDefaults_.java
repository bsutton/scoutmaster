package au.org.scoutmaster.domain;

import java.sql.Time;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(SectionMeetingDefaults.class)
public class SectionMeetingDefaults_ extends BaseEntity_
{

	public static volatile SingularAttribute<SectionMeetingDefaults, Time> EndTime;
	public static volatile SingularAttribute<SectionMeetingDefaults, Section> section;
	public static volatile SingularAttribute<SectionMeetingDefaults, Time> startTime;
	public static volatile SingularAttribute<SectionMeetingDefaults, Address> location;
	public static volatile SingularAttribute<SectionMeetingDefaults, String> meetingSubject;
	public static volatile SingularAttribute<SectionMeetingDefaults, String> meetingDetails;
	public static volatile SingularAttribute<SectionMeetingDefaults, Night> meetingNight;

}