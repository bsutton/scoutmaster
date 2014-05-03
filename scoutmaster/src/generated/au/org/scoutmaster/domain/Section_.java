package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(Section.class)
public class Section_ extends BaseEntity_
{

	public static volatile ListAttribute<Section, Contact> youthMembers;
	public static volatile ListAttribute<Section, Contact> adultHelpers;
	public static volatile SingularAttribute<Section, String> name;
	public static volatile ListAttribute<Section, SectionTryout> trialMembers;
	public static volatile ListAttribute<Section, Contact> leaders;
	public static volatile SingularAttribute<Section, SectionType> type;
	public static volatile ListAttribute<Section, TransitionMember> transitioningYouthMembers;
	public static volatile SingularAttribute<Section, SectionMeetingDefaults> meetingDefaults;

}