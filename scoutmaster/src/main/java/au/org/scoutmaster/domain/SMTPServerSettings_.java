package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-09-10T08:01:10.255+1000")
@StaticMetamodel(SMTPServerSettings.class)
public class SMTPServerSettings_ extends BaseEntity_ {
	public static volatile SingularAttribute<SMTPServerSettings, String> smtpFQDN;
	public static volatile SingularAttribute<SMTPServerSettings, Integer> smtpPort;
	public static volatile SingularAttribute<SMTPServerSettings, String> username;
	public static volatile SingularAttribute<SMTPServerSettings, String> password;
	public static volatile SingularAttribute<SMTPServerSettings, String> fromEmailAddress;
	public static volatile SingularAttribute<SMTPServerSettings, String> bounceEmailAddress;
	public static volatile SingularAttribute<SMTPServerSettings, Boolean> authRequired;
	public static volatile SingularAttribute<SMTPServerSettings, Boolean> useSSL;
}
