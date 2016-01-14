package au.org.scoutmaster.forms;

import java.util.ArrayList;

import org.apache.commons.mail.EmailException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

import au.com.vaadinutils.dao.RunnableUI;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.dao.CommunicationLogDao;
import au.org.scoutmaster.dao.CommunicationTypeDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMTPSettingsDao;
import au.org.scoutmaster.domain.CommunicationLog;
import au.org.scoutmaster.domain.CommunicationType;
import au.org.scoutmaster.domain.SMTPServerSetting;
import au.org.scoutmaster.forms.EmailForm.TargetLine;
import au.org.scoutmaster.util.SMNotification;

public class EmailWorker extends RunnableUI
{

	private Logger logger = LogManager.getLogger();
	private EmailForm emailForm;

	public EmailWorker(UI ui, EmailForm form)
	{
		super(ui);
		this.emailForm = form;
	}

	@Override
	public void run(UI ui)
	{
		try
		{
			final SMTPSettingsDao daoSMTPSettings = new DaoFactory().getSMTPSettingsDao();
			final SMTPServerSetting settings = daoSMTPSettings.findSettings();

			final ArrayList<SMTPSettingsDao.EmailTarget> targets = new ArrayList<>();

			// First add in the primary address.
			if (!isEmpty(emailForm.getPrimaryTargetAddress().getValue()))
			{
				targets.add(
						new SMTPSettingsDao.EmailTarget((EmailAddressType) emailForm.getPrimaryTypeCombo().getValue(),
								emailForm.getPrimaryTargetAddress().getValue()));
			}

			for (final TargetLine line : emailForm.lines)
			{
				if (!isEmpty((String) line.targetAddress.getValue()))
				{
					targets.add(new SMTPSettingsDao.EmailTarget((EmailAddressType) line.targetTypeCombo.getValue(),
							(String) line.targetAddress.getValue()));
				}
			}

			assert targets.size() != 0 : "Empty list of email targets";
			daoSMTPSettings.sendEmail(settings, emailForm.getSender().getEmailAddress(),
					SMSession.INSTANCE.getLoggedInUser().getEmailAddress(), targets, emailForm.getSubject().getValue(),
					emailForm.getCkEditor().getValue(), emailForm.getAttachements());

			// em.detach(EmailForm.this.sender);
			// em.detach(EmailForm.this.contact);
			// Log the activity
			final CommunicationLogDao daoActivity = new DaoFactory().getCommunicationLogDao();
			final CommunicationTypeDao daoActivityType = new DaoFactory().getActivityTypeDao();
			final CommunicationType type = daoActivityType.findByName(CommunicationType.EMAIL);
			final CommunicationLog activity = new CommunicationLog();
			activity.setAddedBy(emailForm.getSender());
			activity.setWithContact(emailForm.getContact());
			activity.setSubject(emailForm.getSubject().getValue());
			activity.setDetails(emailForm.getCkEditor().getValue());
			activity.setType(type);

			daoActivity.persist(activity);
		}
		catch (final EmailException e)
		{
			logger.error(e, e);
			emailForm.getSend().setEnabled(true);
			SMNotification.show(e, Type.ERROR_MESSAGE);
		}

	}

	private boolean isEmpty(final String value)
	{
		return value == null || value.length() == 0;
	}

}
