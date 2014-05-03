package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.joda.money.Money;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(SectionTryoutType.class)
public class SectionTryoutType_ extends BaseEntity_
{

	public static volatile SingularAttribute<SectionTryoutType, Integer> weeks;
	public static volatile SingularAttribute<SectionTryoutType, Money> cost;
	public static volatile SingularAttribute<SectionTryoutType, String> name;
	public static volatile SingularAttribute<SectionTryoutType, String> description;
	public static volatile SingularAttribute<SectionTryoutType, Boolean> paperWorkRequired;
	public static volatile ListAttribute<SectionTryoutType, Section> sections;

}