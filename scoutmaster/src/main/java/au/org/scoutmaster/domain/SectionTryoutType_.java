package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.joda.money.Money;

@Generated(value="Dali", date="2015-11-24T19:08:01.030+1100")
@StaticMetamodel(SectionTryoutType.class)
public class SectionTryoutType_ extends BaseEntity_ {
	public static volatile ListAttribute<SectionTryoutType, Section> sections;
	public static volatile SingularAttribute<SectionTryoutType, String> name;
	public static volatile SingularAttribute<SectionTryoutType, String> description;
	public static volatile SingularAttribute<SectionTryoutType, Boolean> paperWorkRequired;
	public static volatile SingularAttribute<SectionTryoutType, Integer> weeks;
	public static volatile SingularAttribute<SectionTryoutType, Money> cost;
}
