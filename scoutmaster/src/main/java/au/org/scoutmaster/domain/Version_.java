package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-27T23:08:58.294+1000")
@StaticMetamodel(Version.class)
public class Version_ extends BaseEntity_ {
	public static volatile SingularAttribute<Version, Integer> majorVersion;
	public static volatile SingularAttribute<Version, Integer> minorVersion;
	public static volatile SingularAttribute<Version, Integer> microVersion;
}
