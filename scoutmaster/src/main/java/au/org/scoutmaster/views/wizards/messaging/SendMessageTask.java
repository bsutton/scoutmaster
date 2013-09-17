package au.org.scoutmaster.views.wizards.messaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.marre.sms.SmsException;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.impl.LocalEntityManagerFactory;
import au.com.vaadinutils.listener.CancelListener;
import au.com.vaadinutils.listener.ProgressListener;
import au.org.scoutmaster.application.Transaction;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMSProviderDao;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.vaadinutil.util.ProgressBarTask;
import au.org.vaadinutil.util.ProgressTaskListener;

public class SendMessageTask extends ProgressBarTask<SMSTransmission> implements ProgressListener<SMSTransmission>, CancelListener
{
	Logger logger = Logger.getLogger(SendMessageTask.class);
	private Message message;
	private List<SMSTransmission> transmissions;
	private SMSProvider provider;
	private CancelListener listener;

	public SendMessageTask(ProgressTaskListener<SMSTransmission> listener, SMSProvider provider, Message message,
			ArrayList<SMSTransmission> transmissions)
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
			sendMessage(provider, transmissions, message);
		}
		catch (Exception e)
		{
			logger.error(e,e);
			super.taskException(e);
		}

		super.taskComplete(1);

	}

	private void sendMessage(SMSProvider provider, List<SMSTransmission> targets, Message message) throws SmsException, IOException
	{

		EntityManager em = LocalEntityManagerFactory.createEntityManager();

		try (Transaction t = new Transaction(em))
		{
			// We are in a background thread so we have to get our own entity manager.
			EntityManagerProvider.INSTANCE.setCurrentEntityManager(em);

			SMSProviderDao daoSMSProvider = new DaoFactory().getSMSProviderDao();
			listener = daoSMSProvider.send(provider, targets, message, this);

			t.commit();
		}
		finally
		{
			// Reset the entity manager
			EntityManagerProvider.INSTANCE.setCurrentEntityManager(null);
		}
	}

	@Override
	public void progress(int count, int max, SMSTransmission transmission)
	{
		super.taskProgress(count, max, transmission);

	}

	@Override
	public void complete(int sent)
	{
		super.taskComplete(sent);
	}

	@Override
	public void exception(Exception e)
	{
		super.taskException(e);
		
	}

	@Override
	public void itemError(Exception e, SMSTransmission status)
	{
		super.taskItemError(status);
		
	}

	@Override
	public void cancel()
	{
		listener.cancel();
	}
}
