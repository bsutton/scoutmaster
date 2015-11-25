package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-11-24T19:08:01.031+1100")
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
