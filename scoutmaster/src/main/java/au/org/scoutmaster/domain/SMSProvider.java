package au.org.scoutmaster.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries(
{
		@NamedQuery(name = "SMSProvider.findAll", query = "SELECT smsprovider FROM SMSProvider smsprovider"),
		@NamedQuery(name = "SMSProvider.findByType"
		, query = "SELECT smsprovider FROM SMSProvider smsprovider "
				+ "join SMSProviderType smsprovidertype "
				+ "	on smsprovider.type_id = smsprovidertype.id "
				+ "WHERE smsprovidertype.name = :name") })

public class SMSProvider extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private SMSProviderType type;

	private String username;

	private String password;

	private String ApiId;

	private boolean active;

	private boolean defaultProvider;

	
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

	public String getApiId()
	{
		return ApiId;
	}

	public void setApiId(String ApiId)
	{
		this.ApiId = ApiId;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public boolean isDefaultProvider()
	{
		return defaultProvider;
	}

	public void setDefaultProvider(boolean defaultProvider)
	{
		this.defaultProvider = defaultProvider;
	}


	public SMSProviderType getType()
	{
		return type;
	}

	public void setType(SMSProviderType type)
	{
		this.type = type;
	}

}
