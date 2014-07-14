package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-07-13T17:00:34.971+1000")
@StaticMetamodel(QualificationType.class)
public class QualificationType_ extends BaseEntity_ {
	public static volatile SingularAttribute<QualificationType, String> name;
	public static volatile SingularAttribute<QualificationType, String> description;
	public static volatile SingularAttribute<QualificationType, Boolean> expires;
	public static volatile SingularAttribute<QualificationType, Period> validFor;
}
