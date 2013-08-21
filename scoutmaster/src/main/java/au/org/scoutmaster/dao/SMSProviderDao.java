package au.org.scoutmaster.dao;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.marre.SmsSender;
import org.marre.sms.SmsException;

import au.org.scoutmaster.domain.Activity;
import au.org.scoutmaster.domain.ActivityType;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.util.ProgressListener;
import au.org.scoutmaster.views.wizards.messaging.Message;
import au.org.scoutmaster.views.wizards.messaging.SMSTransmission;

import com.vaadin.ui.UI;

public class SMSProviderDao extends JpaBaseDao<SMSProvider, Long> implements Dao<SMSProvider, Long>
{
	Logger logger = Logger.getLogger(SMSProviderDao.class);
	private SmsSender smsSender;

	@Override
	public List<SMSProvider> findAll()
	{
		return super.findAll(SMSProvider.FIND_ALL);
	}

	public List<SMSProvider> findByName(String name)
	{
		return super.findListBySingleParameter(SMSProvider.FIND_BY_NAME, "name", name);
	}

	public void send(SMSProvider provider, List<SMSTransmission> targets, Message message,
			ProgressListener<SMSTransmission> listener) throws SmsException 
	{

		init(provider);

		int max = targets.size();
		int count = 0;
		for (SMSTransmission transmission : targets)
		{
			try
			{
				count++;
				listener.progress(count, max, transmission);
				send(transmission);
			}
			catch (SmsException | IOException e)
			{
				transmission.setException(e);
				listener.itemError(e, transmission);
				logger.error(e, e);
			}

		}
		listener.complete();
	}

	public void send(SMSProvider provider, SMSTransmission transmission, ProgressListener<SMSTransmission> listener)
			throws SmsException, IOException
	{

		init(provider);

		send(transmission);
		listener.progress(1, 1, transmission);
		listener.complete();
	}

	private void send(SMSTransmission transmission) throws SmsException, IOException
	{
		// The message that you want to send.
		String msg = transmission.getMessage().getSubject();

		// International number to target without leading "+"
		Phone reciever = transmission.getPhone();

		// Number of sender (not supported on all transports)
		smsSender.connect();
		smsSender.sendTextSms(msg, reciever.getPhoneNo(), transmission.getMessage().getSender());
		smsSender.disconnect();

		// Log the activity
		ActivityDao daoActivity = new DaoFactory().getActivityDao();
		ActivityTypeDao daoActivityType = new DaoFactory().getActivityTypeDao();
		Activity activity = new Activity();
		User user = (User) UI.getCurrent().getSession().getAttribute("user");
		activity.setAddedBy(user);
		activity.setSubject("SMSMessage sent");
		activity.setType(daoActivityType.findByName(ActivityType.BULK_SMS));
		activity.setActivityDate((Date) new DateTime().toDate());
		activity.setDetails("Subject: " + transmission.getMessage().getSubject() + "Phone: " + reciever + " Message:"
				+ transmission.getMessage().getBody());
		daoActivity.persist(activity);
	}

	private void init(SMSProvider provider) throws SmsException
	{
		smsSender = SmsSender.getClickatellSender(provider.getUsername(), provider.getPassword(), provider.getApiId());

	}

}
