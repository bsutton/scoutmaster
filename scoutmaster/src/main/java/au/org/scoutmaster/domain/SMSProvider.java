package au.org.scoutmaster.domain;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

@Entity
@NamedQueries(
{
		@NamedQuery(name = SMSProvider.FIND_ALL, query = "SELECT smsprovider FROM SMSProvider smsprovider"),
		@NamedQuery(name = SMSProvider.FIND_BY_NAME, query = "SELECT smsprovider FROM SMSProvider smsprovider WHERE smsprovider.providerName like :name")
})

public class SMSProvider extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_BY_NAME = "findByName";
	public static final String FIND_ALL = "findAll";
	

	/**
	 * The name of the provider used to display the provider to th e user.
	 */
	private String providerName;
	
	private String description;

	private String username;

	private String password;

	private String ApiId;

	private Boolean active;

	private Boolean defaultProvider;
	
	/**
	 * Class path to the iSMSProvider
	 */
	String classPath;

	@Transient
	iSMSProvider provider;


	
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

	public Boolean isActive()
	{
		return active;
	}

	public void setActive(Boolean active)
	{
		this.active = active;
	}

	public Boolean isDefaultProvider()
	{
		return defaultProvider;
	}

	public void setDefaultProvider(Boolean defaultProvider)
	{
		this.defaultProvider = defaultProvider;
	}

	public void setProvider(iSMSProvider newInstance)
	{
		this.provider = newInstance;
	}

	public String getClassPath()
	{
		return this.classPath;
	}

	public String getProviderName()
	{
		return this.providerName;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

}
