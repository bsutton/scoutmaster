package au.org.scoutmaster.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@Entity(name="SMSProvider")
@Table(name="SMSProvider")
@NamedQueries(
{
		@NamedQuery(name = SMSProvider.FIND_ALL, query = "SELECT smsprovider FROM SMSProvider smsprovider"),
		@NamedQuery(name = SMSProvider.FIND_BY_NAME, query = "SELECT smsprovider FROM SMSProvider smsprovider WHERE smsprovider.providerName like :name")
})

public class SMSProvider extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_BY_NAME = "SMSProvider.findByName";
	public static final String FIND_ALL = "SMSProvider.findAll";
	

	/**
	 * The name of the provider used to display the provider to th e user.
	 */
	@NotBlank
	@Column(unique=true)
	private String providerName;
	
	private String description;

	@NotBlank
	private String username;

	@NotBlank
	private String password;

	@NotBlank
	private String ApiId;

	private Boolean active;

	private Boolean defaultProvider;
	
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

	public void setProviderName(String providerName)
	{
		this.providerName = providerName;
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
