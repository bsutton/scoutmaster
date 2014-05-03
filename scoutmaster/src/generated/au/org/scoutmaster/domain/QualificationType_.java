package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(QualificationType.class)
public class QualificationType_ extends BaseEntity_
{

	public static volatile SingularAttribute<QualificationType, Boolean> expires;
	public static volatile SingularAttribute<QualificationType, Period> validFor;
	public static volatile SingularAttribute<QualificationType, String> name;
	public static volatile SingularAttribute<QualificationType, String> description;

}