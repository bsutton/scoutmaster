package au.org.scoutmaster.domain;

import au.org.scoutmaster.domain.accounting.MoneyWithTax;
import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-03-20T22:06:36.997+1100")
@StaticMetamodel(SectionTryout.class)
public class SectionTryout_ extends BaseEntity_ {
	public static volatile SingularAttribute<SectionTryout, SectionTryoutType> type;
	public static volatile SingularAttribute<SectionTryout, Contact> trialYouthMember;
	public static volatile SingularAttribute<SectionTryout, Boolean> trailPaperWorkCompleted;
	public static volatile SingularAttribute<SectionTryout, Date> startDate;
	public static volatile SingularAttribute<SectionTryout, Date> expectedCompletionDate;
	public static volatile SingularAttribute<SectionTryout, Date> actualCompletionDate;
	public static volatile SingularAttribute<SectionTryout, TryoutOutcome> outcome;
	public static volatile SingularAttribute<SectionTryout, MoneyWithTax> cost;
	public static volatile SingularAttribute<SectionTryout, Boolean> paid;
}
