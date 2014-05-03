package au.org.scoutmaster.views.wizards.bulkSMS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marre.sms.SmsException;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.listener.CancelListener;
import au.com.vaadinutils.listener.ProgressListener;
import au.com.vaadinutils.util.ProgressBarTask;
import au.com.vaadinutils.util.ProgressTaskListener;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMSProviderDao;
import au.org.scoutmaster.dao.Transaction;
import au.org.scoutmaster.domain.SMSProvider;

public class SendMessageTask extends ProgressBarTask<SMSTransmission> implements ProgressListener<SMSTransmission>,
CancelListener
{
	Logger logger = LogManager.getLogger(SendMessageTask.class);
	private final Message message;
	private final List<SMSTransmission> transmissions;
	private final SMSProvider provider;
	private CancelListener listener;

	public SendMessageTask(final ProgressTaskListener<SMSTransmission> listener, final SMSProvider provider,
			final Message message, final ArrayList<SMSTransmission> transmissions)
	{
		super(listener);
		this.message = message;
		this.transmissions = transmissions;
		this.provider = provider;
	}

	@Override
	public void run()
	{
		try
		{
			sendMessage(this.provider, this.transmissions, this.message);
		}
		catch (final Exception e)
		{
			this.logger.error(e, e);
			super.taskException(e);
		}

		super.taskComplete(1);

	}

	private void sendMessage(final SMSProvider provider, final List<SMSTransmission> targets, final Message message)
			throws SmsException, IOException
	{
		final EntityManager em = EntityManagerProvider.createEntityManager();
		try (Transaction t = new Transaction(em))
		{
			// We are in a background thread so we have to get our own entity
			// manager.
			EntityManagerProvider.setCurrentEntityManager(em);

			final SMSProviderDao daoSMSProvider = new DaoFactory().getSMSProviderDao();
			this.listener = daoSMSProvider.send(provider, targets, message, this);

			t.commit();
		}
		finally
		{
		}
	}

	@Override
	public void progress(final int count, final int max, final SMSTransmission transmission)
	{
		super.taskProgress(count, max, transmission);

	}

	@Override
	public void complete(final int sent)
	{
		super.taskComplete(sent);
	}

	@Override
	public void exception(final Exception e)
	{
		super.taskException(e);

	}

	@Override
	public void itemError(final Exception e, final SMSTransmission status)
	{
		super.taskItemError(status);

	}

	@Override
	public void cancel()
	{
		this.listener.cancel();
	}
}
