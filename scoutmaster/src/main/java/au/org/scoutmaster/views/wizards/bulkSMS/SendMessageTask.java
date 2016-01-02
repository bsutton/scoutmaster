package au.org.scoutmaster.views.wizards.bulkSMS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marre.sms.SmsException;

import com.vaadin.ui.UI;

import au.com.vaadinutils.dao.CallableUI;
import au.com.vaadinutils.dao.EntityManagerThread;
import au.com.vaadinutils.listener.CancelListener;
import au.com.vaadinutils.listener.ProgressListener;
import au.com.vaadinutils.util.ProgressBarTask;
import au.com.vaadinutils.util.ProgressTaskListener;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMSProviderDao;
import au.org.scoutmaster.domain.SMSProvider;

public class SendMessageTask extends ProgressBarTask<SMSTransmission>
		implements ProgressListener<SMSTransmission>, CancelListener
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
	public void runUI(UI ui)
	{
		try
		{
			sendMessage(ui, this.provider, this.transmissions, this.message);
		}
		catch (final Exception e)
		{
			this.logger.error(e, e);
			super.taskException(e);
		}

		super.taskComplete(1);

	}

	private void sendMessage(UI ui, final SMSProvider provider, final List<SMSTransmission> targets,
			final Message message)
	{
		new EntityManagerThread<Void>(new CallableUI<Void>(ui)
		{

			@Override
			public Void call(UI ui)
			{
				final SMSProviderDao daoSMSProvider = new DaoFactory().getSMSProviderDao();
				try
				{
					SendMessageTask.this.listener = daoSMSProvider.send(provider, targets, message,
							SendMessageTask.this);
				}
				catch (SmsException | IOException e)
				{

					logger.error(e, e);
					SendMessageTask.super.taskException(e);
				}
				return null;

			}
		});
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
