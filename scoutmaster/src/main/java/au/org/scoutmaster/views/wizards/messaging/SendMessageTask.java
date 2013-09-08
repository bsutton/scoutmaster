package au.org.scoutmaster.views.wizards.messaging;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.marre.sms.SmsException;

import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMSProviderDao;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.util.ProgressBarTask;
import au.org.scoutmaster.util.ProgressListener;
import au.org.scoutmaster.util.ProgressTaskListener;

public class SendMessageTask extends ProgressBarTask<SMSTransmission> implements ProgressListener<SMSTransmission>
{
	Logger logger = Logger.getLogger(SendMessageTask.class);
	private Message message;
	private List<SMSTransmission> transmissions;
	private SMSProvider provider;

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

	public void sendMessage(SMSProvider provider, List<SMSTransmission> targets, Message message) throws SmsException
	{

		SMSProviderDao daoSMSProvider = new DaoFactory().getSMSProviderDao();
		daoSMSProvider.send(provider, targets, message, this);

	}

	@Override
	public void progress(int count, int max, SMSTransmission transmission)
	{
		super.taskProgress(count, max, transmission);

	}

	@Override
	public void complete()
	{
		super.taskComplete(1);
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
}
