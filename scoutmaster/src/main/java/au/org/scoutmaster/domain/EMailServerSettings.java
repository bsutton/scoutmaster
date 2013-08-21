package au.org.scoutmaster.domain;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.Min;

/**
 * Used to store the SMTP servers email settings.
 * 
 * There should only ever be a single row in this table.
 * 
 * @author bsutton
 *
 */
@Entity
@NamedQueries(
{
	@NamedQuery(name = EMailServerSettings.FIND_ALL, query = "SELECT emailServerSettings FROM EMailServerSettings emailServerSettings"),
})


public class EMailServerSettings extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	static public final String FIND_ALL = "EMailServerSettings.findAll";

	/**
	 * The FQDN or IP address of the smtp server.
	 */
	private String smtpFQDN;
	
	@Min(value=0)
	private Integer smtpPort;
	
	private String username;
	
	private String password;
	
	private String fromEmailAddress;

	private Boolean authRequired;

	
	public Integer getSmtpPort()
	{
		return smtpPort;
	}

	public void setSmtpPort(Integer smtpPort)
	{
		this.smtpPort = smtpPort;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getFromAddress()
	{
		return fromEmailAddress;
	}

	public void setFromEmailAddress(String fromEmailAddress)
	{
		this.fromEmailAddress = fromEmailAddress;
	}


	public String getSmtpFQDN()
	{
		return smtpFQDN;
	}

	public void setSmtpFQDN(String smtpFQDN)
	{
		this.smtpFQDN = smtpFQDN;
	}

	public Boolean isAuthRequired()
	{
		return this.authRequired;
	}

	public void setAuthRequired(Boolean authRequired)
	{
		this.authRequired = authRequired;
		
	}
	
}
