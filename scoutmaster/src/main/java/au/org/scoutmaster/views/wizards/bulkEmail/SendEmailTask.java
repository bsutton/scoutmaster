package au.org.scoutmaster.views.wizards.bulkEmail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.listener.CancelListener;
import au.com.vaadinutils.util.ProgressBarTask;
import au.com.vaadinutils.util.ProgressTaskListener;
import au.org.scoutmaster.application.Transaction;
import au.org.scoutmaster.dao.ActivityDao;
import au.org.scoutmaster.dao.ActivityTypeDao;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMTPSettingsDao;
import au.org.scoutmaster.dao.TagDao;
import au.org.scoutmaster.domain.Activity;
import au.org.scoutmaster.domain.ActivityType;
import au.org.scoutmaster.domain.SMTPServerSettings;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.forms.EmailAddressType;
import au.org.scoutmaster.util.SMNotification;
import au.org.scoutmaster.util.VelocityFormatException;

import com.vaadin.ui.Notification.Type;

public class SendEmailTask extends ProgressBarTask<EmailTransmission> implements CancelListener
{
	Logger logger = Logger.getLogger(SendEmailTask.class);
	private Message message;
	private List<EmailTransmission> transmissions;
	private User user;
	private HashSet<AttachedFile> attachedFiles;
	private boolean cancel = false;

	public SendEmailTask(ProgressTaskListener<EmailTransmission> listener, User user, Message message,
			ArrayList<EmailTransmission> transmissions, HashSet<AttachedFile> attachedFiles)
	{
		super(listener);
		this.message = message;
		this.transmissions = transmissions;
		this.user = user;
		this.attachedFiles = attachedFiles;

	}

	@Override
	public void run()
	{
		try
		{
			sendMessages(user, transmissions, message);
		}
		catch (Exception e)
		{
			logger.error(e, e);
			super.taskException(e);
		}

	}

	private void sendMessages(User user, List<EmailTransmission> targets, Message message) throws IOException
	{

		EntityManager em = EntityManagerProvider.createEntityManager();
		int sent = 0;

		try (Transaction t = new Transaction(em))
		{
			// We are in a background thread so we have to get our own entity
			// manager.
			EntityManagerProvider.setCurrentEntityManager(em);

			SMTPSettingsDao daoSMTPSettings = new DaoFactory().getSMTPSettingsDao();
			SMTPServerSettings settings = daoSMTPSettings.findSettings();

			for (EmailTransmission transmission : targets)
			{
				if (cancel == true)
					break;

				try
				{
					String expandedBody = message.expandBody(user, transmission.getContact());
					StringBuffer expandedSubject = message.expandSubject(user, transmission.getContact());
					daoSMTPSettings.sendEmail(settings, message.getSenderEmailAddress(), transmission.getRecipient(),
							EmailAddressType.To, null, null, expandedSubject.toString(), expandedBody.toString(),
							attachedFiles);

					// Log the activity
					ActivityDao daoActivity = new DaoFactory().getActivityDao();
					ActivityTypeDao daoActivityType = new DaoFactory().getActivityTypeDao();
					ActivityType type = daoActivityType.findByName(ActivityType.BULK_EMAIL);
					Activity activity = new Activity();
					activity.setAddedBy(user);
					activity.setWithContact(transmission.getContact());
					activity.setSubject(message.getSubject());
					activity.setDetails(message.getBody());
					activity.setType(type);
					daoActivity.persist(activity);

					// Tag the contact
					ContactDao daoContact = new DaoFactory().getContactDao();
					TagDao daoTag = new DaoFactory().getTagDao();
					for (Tag tag : transmission.getActivityTags())
					{
						tag = daoTag.merge(tag);
						daoContact.attachTag(transmission.getContact(), tag);
					}
					daoContact.merge(transmission.getContact());
					sent++;
					super.taskProgress(sent, targets.size(), transmission);
					SMNotification.show("Email sent to " + transmission.getContactName(), Type.TRAY_NOTIFICATION);

					// SMNotification.show("Message sent",
					// Type.TRAY_NOTIFICATION);
				}
				catch (EmailException e)
				{
					logger.error(e, e);
					transmission.setException(e);
					super.taskItemError(transmission);
					SMNotification.show(e, Type.ERROR_MESSAGE);
				}
				catch (VelocityFormatException e)
				{
					SMNotification.show(e, Type.ERROR_MESSAGE);
				}

			}

			t.commit();
		}
		finally
		{
			super.taskComplete(sent);

			// Reset the entity manager
			EntityManagerProvider.setCurrentEntityManager(null);
		}
	}

	@Override
	public void cancel()
	{
		this.cancel = true;

	}

}
