package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-01-13T08:53:30.042+1100")
@StaticMetamodel(SMTPServerSetting.class)
public class SMTPServerSetting_ extends BaseEntity_ {
	public static volatile SingularAttribute<SMTPServerSetting, String> smtpFQDN;
	public static volatile SingularAttribute<SMTPServerSetting, Integer> smtpPort;
	public static volatile SingularAttribute<SMTPServerSetting, String> username;
	public static volatile SingularAttribute<SMTPServerSetting, String> password;
	public static volatile SingularAttribute<SMTPServerSetting, String> fromEmailAddress;
	public static volatile SingularAttribute<SMTPServerSetting, String> bounceEmailAddress;
	public static volatile SingularAttribute<SMTPServerSetting, Boolean> authRequired;
	public static volatile SingularAttribute<SMTPServerSetting, Boolean> useSSL;
}
