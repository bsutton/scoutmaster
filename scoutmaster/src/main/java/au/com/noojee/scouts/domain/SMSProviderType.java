package au.com.noojee.scouts.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class SMSProviderType
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	String name;
	
	String description;
	
	@OneToOne(targetEntity=SMSProvider.class)
	iSMSProvider provider;
	
	


	public SMSProviderType(String name, String description, iSMSProvider provider)
	{
		super();
		this.name = name;
		this.description = description;
		this.provider = provider;
	}

	static public void initDB()
	{
		new SMSProviderType("Clickatell", "", new ClickATellProvider());
	}

	public iSMSProvider getProvider()
	{
		return provider;
	}

}
