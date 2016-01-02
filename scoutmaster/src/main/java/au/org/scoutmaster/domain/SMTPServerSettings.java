package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.eclipse.persistence.annotations.Multitenant;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Used to store the SMTP servers email settings.
 *
 * There should only ever be a single row in this table.
 *
 * @author bsutton
 *
 */
@Entity(name = "EMailServerSettings")
@Multitenant
@Table(name = "EMailServerSettings")
@Access(AccessType.FIELD)
@NamedQueries(
{})
public class SMTPServerSettings extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The FQDN or IP address of the SMTP server.
	 */
	@NotBlank
	private String smtpFQDN;

	/**
	 * The port used to connect to the SMTP server.
	 */

	@Min(value = 0)
	@NotNull
	private Integer smtpPort;

	/**
	 * The username used to authenticate against the SMTP server if authRequired
	 * is true.
	 */
	private String username;

	/**
	 * The password used to authenticate against the SMTP server if authRequired
	 * is true.
	 */
	private String password;

	/**
	 * A default from address which is used when the 'system' is sending out
	 * emails.
	 */
	@NotBlank
	@Email
	private String fromEmailAddress;

	/**
	 * The email address to which the receiving SMTP server should send bounced
	 * emails.
	 */
	@Email
	private String bounceEmailAddress;

	/**
	 * If true then the email server requires authentication.
	 */
	private Boolean authRequired = false;

	/**
	 * If set we will use SSL when connecting to the email server if it is
	 * supported.
	 */
	private Boolean useSSL = false;

	public Integer getSmtpPort()
	{
		return this.smtpPort;
	}

	public void setSmtpPort(final Integer smtpPort)
	{
		this.smtpPort = smtpPort;
	}

	public String getUsername()
	{
		return this.username;
	}

	public void setUsername(final String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return this.password;
	}

	public void setPassword(final String password)
	{
		this.password = password;
	}

	public String getFromEmailAddress()
	{
		return this.fromEmailAddress;
	}

	public void setFromEmailAddress(final String fromEmailAddress)
	{
		this.fromEmailAddress = fromEmailAddress;
	}

	public String getSmtpFQDN()
	{
		return this.smtpFQDN;
	}

	public void setSmtpFQDN(final String smtpFQDN)
	{
		this.smtpFQDN = smtpFQDN;
	}

	public Boolean isAuthRequired()
	{
		return this.authRequired;
	}

	public void setAuthRequired(final Boolean authRequired)
	{
		this.authRequired = authRequired;

	}

	public Boolean getUseSSL()
	{
		return this.useSSL;
	}

	public void setUseSSL(final Boolean useSSL)
	{
		this.useSSL = useSSL;
	}

	public String getBounceEmailAddress()
	{
		return this.bounceEmailAddress;
	}

	public void setBounceEmailAddress(final String bounceEmailAddress)
	{
		this.bounceEmailAddress = bounceEmailAddress;
	}

	@Override
	public String getName()
	{
		return this.smtpFQDN;
	}

}
