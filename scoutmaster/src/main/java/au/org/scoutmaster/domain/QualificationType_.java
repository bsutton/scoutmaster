package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-11-24T19:08:01.011+1100")
@StaticMetamodel(QualificationType.class)
public class QualificationType_ extends BaseEntity_ {
	public static volatile SingularAttribute<QualificationType, String> name;
	public static volatile SingularAttribute<QualificationType, String> description;
	public static volatile SingularAttribute<QualificationType, Boolean> expires;
	public static volatile SingularAttribute<QualificationType, Period> validFor;
}
