package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-27T23:08:58.288+1000")
@StaticMetamodel(SectionType.class)
public class SectionType_ extends BaseEntity_ {
	public static volatile SingularAttribute<SectionType, String> name;
	public static volatile SingularAttribute<SectionType, String> description;
	public static volatile SingularAttribute<SectionType, Age> startingAge;
	public static volatile SingularAttribute<SectionType, Age> endingAge;
	public static volatile ListAttribute<SectionType, QualificationType> leaderRequirements;
	public static volatile ListAttribute<SectionType, QualificationType> assistentLeaderRequirements;
	public static volatile ListAttribute<SectionType, QualificationType> parentHelperRequirements;
}
