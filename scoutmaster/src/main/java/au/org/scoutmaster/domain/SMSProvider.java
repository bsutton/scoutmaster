package au.org.scoutmaster.domain;

import java.net.URL;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class SMSProvider extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	@ManyToOne
	SMSProviderType type;

	String username;

	String password;

	URL ApiURL;

	boolean active;

	boolean defaultProvider;

	public void send(List<Phone> targets, String msg)
	{
		iSMSProvider provider = this.type.getProvider();

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
