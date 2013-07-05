package au.com.noojee.scouts.domain;

import java.net.URL;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class SMSProvider
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	SMSProviderType type;
	
	String username;
	
	String password;
	
	URL ApiURL;
	
	boolean active;
	
	boolean defaultProvider;
	
	public void send(List<Phone> targets, String msg)
	{
		iSMSProvider provider = type.getProvider();
		
		provider.init();
		
		for (Phone target : targets)
		{
			if (target.phoneType == PhoneType.MOBILE)
			{
				provider.send(target, msg);
			}
		}
	}
}
