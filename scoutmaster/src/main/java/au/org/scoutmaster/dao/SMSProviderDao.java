package au.org.scoutmaster.dao;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marre.sms.SmsException;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.com.vaadinutils.listener.CancelListener;
import au.com.vaadinutils.listener.ProgressListener;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.util.SMSSession;
import au.org.scoutmaster.views.wizards.bulkSMS.Message;
import au.org.scoutmaster.views.wizards.bulkSMS.SMSTransmission;

public class SMSProviderDao extends JpaBaseDao<SMSProvider, Long> implements Dao<SMSProvider, Long>, CancelListener
{
	Logger logger = LogManager.getLogger(SMSProviderDao.class);
	private boolean cancel = false;

	public List<SMSProvider> findByName(final String name)
	{
		return super.findListBySingleParameter(SMSProvider.FIND_BY_NAME, "name", name);
	}

	/**
	 * Sends a list of SMS messages using a single provider session.
	 *
	 * @param provider
	 * @param targets
	 * @param message
	 * @param listener
	 * @return
	 * @throws SmsException
	 * @throws IOException
	 */
	public CancelListener send(final SMSProvider provider, final List<SMSTransmission> targets, final Message message,
			final ProgressListener<SMSTransmission> listener) throws SmsException, IOException
	{
		final int max = targets.size();
		int sent = 0;

		try (SMSSession session = new SMSSession(provider))
		{
			for (final SMSTransmission transmission : targets)
			{
				if (this.cancel == true)
				{
					break;
				}
				try
				{
					session.send(transmission);
					sent++;
					listener.progress(sent, max, transmission);
				}
				catch (SmsException | IOException e)
				{
					transmission.setException(e);
					listener.itemError(e, transmission);
					this.logger.error(e, e);
				}

			}
		}
		catch (final IOException e)
		{
			this.logger.error(e, e);
			throw e;
		}

		listener.complete(sent);
		return this;
	}

	/**
	 * Sends a single SMS messaging using its own session.
	 *
	 * @param provider
	 * @param transmission
	 * @param listener
	 * @throws SmsException
	 * @throws IOException
	 */
	public void sendNoLogging(final SMSProvider provider, final SMSTransmission transmission,
			final ProgressListener<SMSTransmission> listener) throws SmsException, IOException
	{

		try (SMSSession session = new SMSSession(provider))
		{

			session.send(transmission, false);
			listener.progress(1, 1, transmission);
			listener.complete(1);
		}
		catch (final IOException e)
		{
			this.logger.error(e, e);
			throw e;
		}

	}

	/**
	 * Sends a single SMS messaging using its own session.
	 *
	 * @param provider
	 * @param transmission
	 * @param listener
	 * @throws SmsException
	 * @throws IOException
	 */
	public void send(final SMSProvider provider, final SMSTransmission transmission,
			final ProgressListener<SMSTransmission> listener) throws SmsException, IOException
	{

		try (SMSSession session = new SMSSession(provider))
		{

			session.send(transmission, true);
			listener.progress(1, 1, transmission);
			listener.complete(1);
		}
		catch (final IOException e)
		{
			this.logger.error(e, e);
			throw e;
		}

	}

	@Override
	public JPAContainer<SMSProvider> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

	@Override
	public void cancel()
	{
		this.cancel = true;

	}

}
