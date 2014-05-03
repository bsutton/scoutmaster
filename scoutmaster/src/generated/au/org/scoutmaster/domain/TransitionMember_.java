package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(TransitionMember.class)
public class TransitionMember_ extends BaseEntity_
{

	public static volatile SingularAttribute<TransitionMember, Date> expectedStartDate;
	public static volatile SingularAttribute<TransitionMember, Contact> youthMember;
	public static volatile SingularAttribute<TransitionMember, Contact> transitionSupervisor;
	public static volatile SingularAttribute<TransitionMember, Section> toSection;
	public static volatile SingularAttribute<TransitionMember, Section> fromSection;
	public static volatile SingularAttribute<TransitionMember, Date> expextedCompletionDate;

}