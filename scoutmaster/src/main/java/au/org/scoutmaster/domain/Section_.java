package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-15T13:07:13.915+1000")
@StaticMetamodel(Section.class)
public class Section_ extends BaseEntity_ {
	public static volatile SingularAttribute<Section, String> name;
	public static volatile SingularAttribute<Section, SectionType> type;
	public static volatile ListAttribute<Section, Contact> leaders;
	public static volatile ListAttribute<Section, Contact> adultHelpers;
	public static volatile ListAttribute<Section, Contact> youthMembers;
	public static volatile ListAttribute<Section, TransitionMember> transitioningYouthMembers;
	public static volatile ListAttribute<Section, SectionTryout> trialMembers;
	public static volatile SingularAttribute<Section, SectionMeetingDefaults> meetingDefaults;
}
