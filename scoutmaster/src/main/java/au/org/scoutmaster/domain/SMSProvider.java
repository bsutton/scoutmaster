package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.hibernate.validator.constraints.NotBlank;

@Entity(name = "SMSProvider")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "SMSProvider")
@Access(AccessType.FIELD)
@NamedQueries(
{ @NamedQuery(name = SMSProvider.FIND_BY_NAME, query = "SELECT smsprovider FROM SMSProvider smsprovider WHERE smsprovider.providerName like :name") })
public class SMSProvider extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_NAME = "SMSProvider.findByName";

	/**
	 * The name of the provider used to display the provider to the user.
	 */
	@NotBlank
	@Column(unique = true)
	private String providerName;

	private String description;

	@NotBlank
	private String username;

	@NotBlank
	private String password;

	@NotBlank
	private String ApiId;

	private Boolean active;

	/**
	 * The default sender id used when sending a text message. The sender id is
	 * usually a mobile phone number but in some countries can be a test message
	 * e.g. 'Heidelberg Scouts'. Clickatell require that the senderid is
	 * registered to stop spam/spoofing. The mobile phone of the person who
	 * created the account will be registered by default.
	 */
	private String defaultSenderID;

	public String getDefaultSenderID()
	{
		return this.defaultSenderID;
	}

	public void setDefaultSenderID(final String defaultSenderID)
	{
		this.defaultSenderID = defaultSenderID;
	}

	public Boolean getActive()
	{
		return this.active;
	}

	private Boolean defaultProvider;

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

	public String getApiId()
	{
		return this.ApiId;
	}

	public void setApiId(final String ApiId)
	{
		this.ApiId = ApiId;
	}

	public Boolean isActive()
	{
		return this.active;
	}

	public void setActive(final Boolean active)
	{
		this.active = active;
	}

	public Boolean isDefaultProvider()
	{
		return this.defaultProvider;
	}

	public void setDefaultProvider(final Boolean defaultProvider)
	{
		this.defaultProvider = defaultProvider;
	}

	public void setProviderName(final String providerName)
	{
		this.providerName = providerName;
	}

	public String getProviderName()
	{
		return this.providerName;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	@Override
	public String getName()
	{
		return this.providerName;
	}

}
