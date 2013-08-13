package au.org.scoutmaster.dao;

import java.io.IOException;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.marre.SmsSender;
import org.marre.sms.SmsException;

import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.PhoneType;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.domain.iSMSProvider;
import au.org.scoutmaster.util.ProgressListener;
import au.org.scoutmaster.views.wizards.messaging.Message;

public class SMSProviderDao extends JpaBaseDao<SMSProvider, Long> implements Dao<SMSProvider, Long>
{
	Logger logger = Logger.getLogger(SMSProviderDao.class);
	private SmsSender smsSender;

	@Override
	public List<SMSProvider> findAll()
	{
		Query query = entityManager.createNamedQuery("SMSProvider.findAll");
		@SuppressWarnings("unchecked")
		List<SMSProvider> list = query.getResultList();
		initProviders(list);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<SMSProvider> findByName(String name)
	{
		Query query = entityManager.createNamedQuery(SMSProvider.FIND_BY_NAME);
		query.setParameter("name", name);
		List<SMSProvider> results = query.getResultList();
		initProviders(results);
		return results;
	}

	/**
	 * Initialise each of the specified SMS Providers
	 * 
	 * @param list
	 */
	private void initProviders(List<SMSProvider> list)
	{
		for (SMSProvider provider : list)
		{
			try
			{
				@SuppressWarnings("unchecked")
				Class<iSMSProvider> classz = (Class<iSMSProvider>) Class.forName(provider.getClassPath());
				provider.setProvider(classz.newInstance());
			}
			catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
			{
				logger.error(e, e);
				throw new RuntimeException(e);
			}
		}

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
