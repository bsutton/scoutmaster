package au.org.scoutmaster.dao;

import java.io.IOException;
import java.util.List;

import javax.persistence.Query;

import org.marre.SmsSender;
import org.marre.sms.SmsException;

import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.PhoneType;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.util.ProgressListener;
import au.org.scoutmaster.views.messagingWizard.Message;

public class SMSProviderDao extends JpaBaseDao<SMSProvider, Long> implements Dao<SMSProvider, Long>
{

	private SmsSender smsSender;

	// Send SMS with clickatell
	// "bsutton", "SdKXfdTCXCYFAK", "api_id=3431385"

	@Override
	public List<SMSProvider> findAll()
	{
		Query query = entityManager.createNamedQuery("SMSProvider.findAll");
		@SuppressWarnings("unchecked")
		List<SMSProvider> list = query.getResultList();
		return list;
	}

	public void send(SMSProvider provider, List<Phone> targets, Message message, ProgressListener listener) throws SmsException, IOException
	{

		init(provider);

		int max = targets.size();
		int count = 0;
		for (Phone target : targets)
		{
			if (target.getPhoneType() == PhoneType.MOBILE)
			{
				send(target.getPhoneNo(), message);
			}
			count++;
			listener.progress(count, max);
		}
		listener.complete();
	}

	public void send(SMSProvider provider, String phoneNo, Message message, ProgressListener listener) throws SmsException, IOException
	{

		init(provider);

		listener.progress(0, 1);
		send(phoneNo, message);
		listener.complete();
	}

	private void send(String phone, Message message) throws SmsException, IOException
	{
		// The message that you want to send.
		String msg = message.getSubject();
		
		// International number to target without leading "+"
		String reciever = phone;
		
		// Number of sender (not supported on all transports)
		smsSender.connect();
		smsSender.sendTextSms(msg, reciever, message.getSender());
		smsSender.disconnect();

	}

	private void init(SMSProvider provider) throws SmsException
	{
		smsSender = SmsSender.getClickatellSender(provider.getUsername(), provider.getPassword(),
				provider.getApiId());

	}

}
