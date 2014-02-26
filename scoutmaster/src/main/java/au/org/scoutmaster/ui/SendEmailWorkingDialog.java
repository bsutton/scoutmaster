package au.org.scoutmaster.ui;

import java.util.ArrayList;

import javax.persistence.EntityManager;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.listener.CompleteListener;
import au.com.vaadinutils.ui.WorkingDialog;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMTPSettingsDao;
import au.org.scoutmaster.dao.Transaction;
import au.org.scoutmaster.domain.SMTPServerSettings;
import au.org.scoutmaster.util.SMNotification;

import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

/**
 * Designed to show a 'working' dialog whilst sending a single email.
 * 
 * Uses a builder pattern to construct the email.
 * @author bsutton
 *
 */
public class SendEmailWorkingDialog extends WorkingDialog implements CompleteListener
{
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(SendEmailWorkingDialog.class);

	private String subject;
	private ArrayList<String> toEmailAddresses = new ArrayList<>();
	private String message;
	private String fromEmailAddress;

	private CompleteListener completeListener;

	public SendEmailWorkingDialog(String caption, String message)
	{
		super(caption, message);
	}
	
	public SendEmailWorkingDialog setFrom(String fromEmailAddress)
	{
		this.fromEmailAddress = fromEmailAddress;
		return this;
	}

	
	public SendEmailWorkingDialog setSubject(String subject)
	{
		this.subject = subject;
		return this;
	}

	public SendEmailWorkingDialog addTo(String toEmailAddress)
	{
		this.toEmailAddresses .add(toEmailAddress);
		return this;
	}
	
	public SendEmailWorkingDialog setMessage(String message)
	{
		this.message = message;
		return this;
	}
	
	public void setCompleteListener(CompleteListener completeListener)
	{
		this.completeListener = completeListener;
	}



	public void send()
	{
		
		UI.getCurrent().addWindow(this);
		this.setWorker(new Runnable() //(String fromEmailAddress, ArrayList<String> toEmailAddresses, String subject, String message)
		{

			@Override
			public void run()
			{
				EntityManager em = EntityManagerProvider.createEntityManager();
				try (Transaction t = new Transaction(em))
				{

					SMTPSettingsDao settingsDao = new DaoFactory(em).getSMTPSettingsDao();
					SMTPServerSettings settings = settingsDao.findSettings();
			
					Email email = new SimpleEmail();
					email.setHostName(settings.getSmtpFQDN());
					email.setSmtpPort(settings.getSmtpPort());
					if (settings.isAuthRequired())
						email.setAuthenticator(new DefaultAuthenticator(settings.getUsername(), settings.getPassword()));
					email.setSSLOnConnect(true);
					try
					{
						email.setFrom(SendEmailWorkingDialog.this.fromEmailAddress);
						email.setSubject(SendEmailWorkingDialog.this.subject);
						email.setMsg(SendEmailWorkingDialog.this.message);
						email.addTo(SendEmailWorkingDialog.this.toEmailAddresses.toArray(new String[]{}));
						email.send();
					}
					catch (EmailException e)
					{
						logger.error(e, e);
						SMNotification.show(e, Type.ERROR_MESSAGE);
					}
					t.commit();
					
				}

			}
		}, this);

	}

	@Override
	public void complete()
	{
		this.setVisible(false);
		if (this.completeListener != null)
			this.completeListener.complete();
		
	}


}
