package au.org.scoutmaster.util;

import java.io.Closeable;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marre.SmsSender;
import org.marre.sms.SmsException;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import com.vaadin.ui.Notification.Type;

import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.dao.CommunicationLogDao;
import au.org.scoutmaster.dao.CommunicationTypeDao;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.CommunicationLog;
import au.org.scoutmaster.domain.CommunicationType;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.views.wizards.bulkSMS.SMSTransmission;

public class SMSSession implements Closeable
{
	static private Logger logger = LogManager.getLogger(SMSSession.class);

	private final SmsSender smsSender;

	public SMSSession(final SMSProvider provider) throws SmsException, IOException
	{
		this.smsSender = SmsSender.getClickatellSender(provider.getUsername(), provider.getPassword(),
				provider.getApiId());
		Preconditions.checkNotNull(this.smsSender);
		this.smsSender.connect();
	}

	@Override
	public void close() throws IOException
	{
		if (this.smsSender != null)
		{
			try
			{
				this.smsSender.disconnect();
			}
			catch (final SmsException e)
			{
				SMSSession.logger.error(e, e);
			}
		}

	}

	public void send(final SMSTransmission transmission) throws SmsException, IOException
	{
		send(transmission, true);
	}

	/**
	 * Sends an SMSMessage and logs it if 'log' is set to true.
	 *
	 * @param transmission
	 * @param log
	 * @throws SmsException
	 * @throws IOException
	 */
	public void send(final SMSTransmission transmission, boolean log) throws SmsException, IOException
	{
		final User user = SMSession.INSTANCE.getLoggedInUser();

		try
		{
			// The message that you want to send.
			final String msg = transmission.getMessage().expandBody(user, transmission.getContact()).toString();

			// International number to target without leading "+"
			final Phone reciever = transmission.getRecipient();

			// Number of sender (not supported on all transports)
			this.smsSender.sendTextSms(msg, reciever.getPhoneNo().replaceAll("\\s", ""),
					transmission.getMessage().getSender().getPhoneNo());

			if (log)
			{
				// Log the activity
				final CommunicationLogDao daoActivity = new DaoFactory().getCommunicationLogDao();
				final CommunicationTypeDao daoActivityType = new DaoFactory().getActivityTypeDao();
				final CommunicationLog activity = new CommunicationLog();
				activity.setAddedBy(user);
				activity.setWithContact(transmission.getContact());
				activity.setSubject(transmission.getMessage().getSubject());
				activity.setType(daoActivityType.findByName(CommunicationType.BULK_SMS));
				activity.setDetails(transmission.getMessage().getBody());
				daoActivity.persist(activity);

				// Tag the contact
				final ContactDao daoContact = new DaoFactory().getContactDao();
				for (final Tag tag : transmission.getActivityTags())
				{
					daoContact.attachTag(transmission.getContact(), tag);
				}
				daoContact.merge(transmission.getContact());
			}

		}
		catch (final VelocityFormatException e)
		{
			SMNotification.show(e, Type.ERROR_MESSAGE);
		}

	}
}
