package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-27T23:08:58.289+1000")
@StaticMetamodel(SMSProvider.class)
public class SMSProvider_ extends BaseEntity_ {
	public static volatile SingularAttribute<SMSProvider, String> providerName;
	public static volatile SingularAttribute<SMSProvider, String> description;
	public static volatile SingularAttribute<SMSProvider, String> username;
	public static volatile SingularAttribute<SMSProvider, String> password;
	public static volatile SingularAttribute<SMSProvider, String> ApiId;
	public static volatile SingularAttribute<SMSProvider, Boolean> active;
	public static volatile SingularAttribute<SMSProvider, Boolean> defaultProvider;
}