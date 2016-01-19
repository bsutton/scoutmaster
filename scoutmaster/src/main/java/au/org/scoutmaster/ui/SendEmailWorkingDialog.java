package au.org.scoutmaster.ui;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.ui.UI;

import au.com.vaadinutils.dao.EntityManagerRunnable;
import au.com.vaadinutils.listener.CompleteListener;
import au.com.vaadinutils.ui.WorkingDialog;

/**
 * Designed to show a 'working' dialog whilst sending a single email.
 *
 * Uses a builder pattern to construct the email.
 *
 * @author bsutton
 *
 */
public class SendEmailWorkingDialog extends WorkingDialog implements CompleteListener
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(SendEmailWorkingDialog.class);

	private String subject;

	private final ArrayList<String> toEmailAddresses = new ArrayList<>();
	private String message;
	private String fromEmailAddress;

	private CompleteListener completeListener;

	public SendEmailWorkingDialog(final String caption, final String message)
	{
		super(caption, message);
	}

	public SendEmailWorkingDialog setFrom(final String fromEmailAddress)
	{
		this.setFromEmailAddress(fromEmailAddress);
		return this;
	}

	public SendEmailWorkingDialog setSubject(final String subject)
	{
		this.subject = subject;
		return this;
	}

	public SendEmailWorkingDialog addTo(final String toEmailAddress)
	{
		this.getToEmailAddresses().add(toEmailAddress);
		return this;
	}

	public SendEmailWorkingDialog setMessage(final String message)
	{
		this.message = message;
		return this;
	}

	public void setCompleteListener(final CompleteListener completeListener)
	{
		this.completeListener = completeListener;
	}

	public void send()
	{

		UI.getCurrent().addWindow(this);
		setWorker(new EntityManagerRunnable(new EmailWorker(UI.getCurrent(), this)), this);

	}

	@Override
	public void complete()
	{
		setVisible(false);
		if (this.completeListener != null)
		{
			this.completeListener.complete();
		}

	}

	public String getFromEmailAddress()
	{
		return fromEmailAddress;
	}

	public void setFromEmailAddress(String fromEmailAddress)
	{
		this.fromEmailAddress = fromEmailAddress;
	}

	public String getSubject()
	{
		return subject;
	}

	public String getMessage()
	{
		return message;
	}

	public ArrayList<String> getToEmailAddresses()
	{
		return toEmailAddresses;
	}

}
