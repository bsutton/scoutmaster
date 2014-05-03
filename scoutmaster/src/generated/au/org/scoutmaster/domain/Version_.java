package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(Version.class)
public class Version_ extends BaseEntity_
{

	public static volatile SingularAttribute<Version, Integer> microVersion;
	public static volatile SingularAttribute<Version, Integer> majorVersion;
	public static volatile SingularAttribute<Version, Integer> minorVersion;

}