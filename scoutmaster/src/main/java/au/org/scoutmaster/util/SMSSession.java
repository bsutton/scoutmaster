package au.org.scoutmaster.util;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Date;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.marre.SmsSender;
import org.marre.sms.SmsException;

import au.org.scoutmaster.dao.ActivityDao;
import au.org.scoutmaster.dao.ActivityTypeDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Activity;
import au.org.scoutmaster.domain.ActivityType;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.views.wizards.messaging.SMSTransmission;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import com.vaadin.ui.UI;

public class SMSSession implements Closeable
{
	static private Logger logger = Logger.getLogger(SMSSession.class);

	private SmsSender smsSender;

	public SMSSession(SMSProvider provider) throws SmsException, IOException
	{
		smsSender = SmsSender.getClickatellSender(provider.getUsername(), provider.getPassword(), provider.getApiId());
		Preconditions.checkNotNull(smsSender);
		smsSender.connect();
	}

	@Override
	public void close() throws IOException
	{
		if (smsSender != null)
			try
			{
				smsSender.disconnect();
			}
			catch (SmsException e)
			{
				logger.error(e, e);
			}

	}

	public void send(SMSTransmission transmission) throws SmsException, IOException
	{
		// The message that you want to send.
		String msg = transmission.getMessage().getBody();

		// International number to target without leading "+"
		Phone reciever = transmission.getRecipient();

		// Number of sender (not supported on all transports)
		smsSender.sendTextSms(msg, reciever.getPhoneNo().replaceAll("\\s",""), transmission.getMessage().getSender().getPhoneNo());

		// Log the activity
		ActivityDao daoActivity = new DaoFactory().getActivityDao();
		ActivityTypeDao daoActivityType = new DaoFactory().getActivityTypeDao();
		Activity activity = new Activity();
		User user = (User) UI.getCurrent().getSession().getAttribute("user");
		activity.setAddedBy(user);
		activity.setWithContact(transmission.getContact());
		activity.setSubject(transmission.getMessage().getSubject());
		activity.setType(daoActivityType.findByName(ActivityType.BULK_SMS));
		activity.setActivityDate(new Date(new DateTime().toDate().getTime()));
		activity.setDetails(transmission.getMessage().getBody());
		daoActivity.persist(activity);
	}
}
