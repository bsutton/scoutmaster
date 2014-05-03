package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.accounting.MoneyWithTax;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(SectionTryout.class)
public class SectionTryout_ extends BaseEntity_
{

	public static volatile SingularAttribute<SectionTryout, Contact> trialYouthMember;
	public static volatile SingularAttribute<SectionTryout, MoneyWithTax> cost;
	public static volatile SingularAttribute<SectionTryout, Boolean> trailPaperWorkCompleted;
	public static volatile SingularAttribute<SectionTryout, Date> actualCompletionDate;
	public static volatile SingularAttribute<SectionTryout, Boolean> paid;
	public static volatile SingularAttribute<SectionTryout, Date> expectedCompletionDate;
	public static volatile SingularAttribute<SectionTryout, SectionTryoutType> type;
	public static volatile SingularAttribute<SectionTryout, Date> startDate;
	public static volatile SingularAttribute<SectionTryout, TryoutOutcome> outcome;

}