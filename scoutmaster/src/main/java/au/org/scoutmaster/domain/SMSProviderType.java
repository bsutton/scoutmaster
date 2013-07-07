package au.org.scoutmaster.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class SMSProviderType extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	String name;

	String description;

	@OneToOne(targetEntity = SMSProvider.class)
	iSMSProvider provider;

	public SMSProviderType()
	{

	}

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
		return this.provider;
	}

}
