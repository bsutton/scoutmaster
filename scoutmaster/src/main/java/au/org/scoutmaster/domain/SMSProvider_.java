package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-11-24T19:08:01.031+1100")
@StaticMetamodel(SMSProvider.class)
public class SMSProvider_ extends BaseEntity_ {
	public static volatile SingularAttribute<SMSProvider, String> providerName;
	public static volatile SingularAttribute<SMSProvider, String> description;
	public static volatile SingularAttribute<SMSProvider, String> username;
	public static volatile SingularAttribute<SMSProvider, String> password;
	public static volatile SingularAttribute<SMSProvider, String> ApiId;
	public static volatile SingularAttribute<SMSProvider, Boolean> active;
	public static volatile SingularAttribute<SMSProvider, String> defaultSenderID;
	public static volatile SingularAttribute<SMSProvider, Boolean> defaultProvider;
}
