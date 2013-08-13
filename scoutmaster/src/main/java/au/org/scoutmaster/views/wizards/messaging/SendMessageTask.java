package au.org.scoutmaster.views.wizards.messaging;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.marre.sms.SmsException;

import au.org.scoutmaster.dao.SMSProviderDao;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.util.ProgressBarTask;
import au.org.scoutmaster.util.ProgressListener;
import au.org.scoutmaster.util.ProgressTaskListener;

public class SendMessageTask extends ProgressBarTask implements ProgressListener
{
	Logger logger = Logger.getLogger(SendMessageTask.class);
	private Message message;
	private List<Phone> targets;
	private SMSProvider provider;

	public SendMessageTask(ProgressTaskListener listener, SMSProvider provider, Message message, List<Phone> targets)
	{
		super(listener);
		this.message = message;
		this.targets = targets;
		this.provider = provider;
	}

	@Override
	public void run()
	{
		int count = 0;
		int max = targets.size();

		try
		{
			sendMessage(provider, targets, message);
			Thread.sleep(2000);
			super.taskProgress(count, max);
		}
		catch (InterruptedException | SmsException | IOException e)
		{
			logger.error(e,e);
		}

		super.taskComplete();

	}

	public void sendMessage(SMSProvider provider, List<Phone> targets, Message message) throws SmsException,
			IOException
	{
		SMSProviderDao daoSMSProvider = new SMSProviderDao();
		daoSMSProvider.send(provider, targets, message, this);

	}

	@Override
	public void progress(int count, int max)
	{
		super.taskProgress(count, max);
		
	}

	@Override
	public void complete()
	{
		super.taskComplete();
	}

}
