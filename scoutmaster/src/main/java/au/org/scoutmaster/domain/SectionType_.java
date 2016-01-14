package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-01-09T14:28:46.932+1100")
@StaticMetamodel(SectionType.class)
public class SectionType_ extends BaseEntity_ {
	public static volatile SingularAttribute<SectionType, String> name;
	public static volatile SingularAttribute<SectionType, String> description;
	public static volatile SingularAttribute<SectionType, Age> startingAge;
	public static volatile SingularAttribute<SectionType, Age> endingAge;
	public static volatile SingularAttribute<SectionType, Color> colour;
	public static volatile SetAttribute<SectionType, QualificationType> leaderRequirements;
	public static volatile SetAttribute<SectionType, QualificationType> assistentLeaderRequirements;
	public static volatile SetAttribute<SectionType, QualificationType> parentHelperRequirements;
}
