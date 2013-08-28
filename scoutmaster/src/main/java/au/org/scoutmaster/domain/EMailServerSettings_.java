package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-27T23:08:58.269+1000")
@StaticMetamodel(EMailServerSettings.class)
public class EMailServerSettings_ extends BaseEntity_ {
	public static volatile SingularAttribute<EMailServerSettings, String> smtpFQDN;
	public static volatile SingularAttribute<EMailServerSettings, Integer> smtpPort;
	public static volatile SingularAttribute<EMailServerSettings, String> username;
	public static volatile SingularAttribute<EMailServerSettings, String> password;
	public static volatile SingularAttribute<EMailServerSettings, String> fromEmailAddress;
	public static volatile SingularAttribute<EMailServerSettings, String> bounceEmailAddress;
	public static volatile SingularAttribute<EMailServerSettings, Boolean> authRequired;
	public static volatile SingularAttribute<EMailServerSettings, Boolean> useSSL;
}
