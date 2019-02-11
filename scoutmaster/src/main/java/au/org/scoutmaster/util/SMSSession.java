package au.org.scoutmaster.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SMSSession

/*
 * MARRE is missing implements Closeable
 */
{
	static private Logger logger = LogManager.getLogger(SMSSession.class);

	/*
	 * MARRE is missing
	 *
	 * private final SmsSender smsSender;
	 */

	/**
	 * MARRE is missing public SMSSession(final SMSProvider provider) throws
	 * SmsException, IOException { this.smsSender =
	 * SmsSender.getClickatellSender(provider.getUsername(),
	 * provider.getPassword(), provider.getApiId());
	 * Preconditions.checkNotNull(this.smsSender); this.smsSender.connect(); }
	 */

	/*
	 * MARRE is missing
	 *
	 * @Override public void close() throws IOException { if (this.smsSender !=
	 * null) { try { this.smsSender.disconnect(); } catch (final SmsException e)
	 * { SMSSession.logger.error(e, e); } }
	 *
	 * }
	 */

	/*
	 * MARRE is missing
	 *
	 * public void send(final SMSTransmission transmission) throws SmsException,
	 * IOException { send(transmission, true); }
	 */

	/**
	 * Sends an SMSMessage and logs it if 'log' is set to true.
	 *
	 * @param transmission
	 * @param log
	 * @throws SmsException
	 * @throws IOException
	 */

	/*
	 * MARRE is missing
	 *
	 * public void send(final SMSTransmission transmission, boolean log) throws
	 * SmsException, IOException { final User user =
	 * SMSession.INSTANCE.getLoggedInUser();
	 *
	 * try { final ContactDao daoContact = new DaoFactory().getContactDao();
	 *
	 * Contact contact = transmission.getContact();
	 *
	 * // re-attach the contact. contact = daoContact.merge(contact);
	 *
	 * // The message that you want to send. final String msg =
	 * transmission.getMessage().expandBody(user, contact).toString();
	 *
	 * // International number to target without leading "+" final Phone
	 * reciever = transmission.getRecipient();
	 *
	 * // Number of sender (not supported on all transports)
	 * this.smsSender.sendTextSms(msg, reciever.getPhoneNo().replaceAll("\\s",
	 * ""), transmission.getMessage().getSender().getPhoneNo());
	 *
	 * if (log) { // Log the activity final CommunicationLogDao daoActivity =
	 * new DaoFactory().getCommunicationLogDao(); final CommunicationTypeDao
	 * daoActivityType = new DaoFactory().getActivityTypeDao(); final
	 * CommunicationLog activity = new CommunicationLog();
	 * activity.setAddedBy(user); activity.setWithContact(contact);
	 * activity.setSubject(transmission.getMessage().getSubject());
	 * activity.setType(daoActivityType.findByName(CommunicationType.BULK_SMS));
	 * activity.setDetails(transmission.getMessage().getBody());
	 * daoActivity.persist(activity);
	 *
	 * // Tag the contact for (final Tag tag : transmission.getActivityTags()) {
	 * daoContact.attachTag(contact, tag); } }
	 *
	 * } catch (final VelocityFormatException e) { SMNotification.show(e,
	 * Type.ERROR_MESSAGE); }
	 *
	 * }
	 */
}
