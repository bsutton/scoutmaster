package au.org.scoutmaster.domain;

import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-09-19T09:12:29.173+1000")
@StaticMetamodel(TransitionMember.class)
public class TransitionMember_ extends BaseEntity_ {
	public static volatile SingularAttribute<TransitionMember, Contact> youthMember;
	public static volatile SingularAttribute<TransitionMember, Contact> transitionSupervisor;
	public static volatile SingularAttribute<TransitionMember, Section> fromSection;
	public static volatile SingularAttribute<TransitionMember, Section> toSection;
	public static volatile SingularAttribute<TransitionMember, Date> expectedStartDate;
	public static volatile SingularAttribute<TransitionMember, Date> expextedCompletionDate;
}
