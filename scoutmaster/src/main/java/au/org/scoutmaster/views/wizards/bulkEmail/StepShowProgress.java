package au.org.scoutmaster.views.wizards.bulkEmail;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.fields.PoJoTable;
import au.com.vaadinutils.ui.UIUpdater;
import au.com.vaadinutils.ui.WorkingDialog;
import au.com.vaadinutils.util.MutableInteger;
import au.com.vaadinutils.util.ProgressBarWorker;
import au.com.vaadinutils.util.ProgressTaskListener;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Importable;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.util.SMNotification;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class StepShowProgress implements WizardStep, ProgressTaskListener<EmailTransmission>
{
	@SuppressWarnings("unused")
	static private Logger logger = LogManager.getLogger(StepShowProgress.class);
	JPAContainer<? extends Importable> entities;
	private WizardView messagingWizardView;
	private boolean sendComplete = false;
	private ProgressBar indicator;
	private Label progressDescription;
	private PoJoTable<EmailTransmission> progressTable;
	private MutableInteger queued = new MutableInteger(0);
	private MutableInteger rejected = new MutableInteger(0);
	private WorkingDialog workDialog;

	public StepShowProgress(WizardView messagingWizardView)
	{
		this.messagingWizardView = messagingWizardView;
	}

	@Override
	public String getCaption()
	{
		return "Send Messages";
	}

	@Override
	public Component getContent()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		progressTable = new PoJoTable<>(EmailTransmission.class, new String[]
		{ "ContactName", "Recipient", "Exception" });
		progressTable.setColumnWidth("Recipient", 80);
		progressTable.setColumnExpandRatio("Exception", 1);
		progressTable.setSizeFull();
		progressDescription = new Label();
		layout.addComponent(progressDescription);
		layout.setMargin(true);
		indicator = new ProgressBar(new Float(0.0));
		indicator.setHeight("30px");
		indicator.setIndeterminate(false);
		indicator.setImmediate(true);
		indicator.setSizeFull();
		layout.addComponent(indicator);
		layout.addComponent(this.progressTable);
		layout.setExpandRatio(progressTable, 1);

		ArrayList<Contact> recipients = messagingWizardView.getRecipientStep().getRecipients();

		StepEnterDetails details = messagingWizardView.getDetails();
		ArrayList<EmailTransmission> transmissions = new ArrayList<>();

		HashSet<String> dedupList = new HashSet<>();

		for (Contact contact : recipients)
		{
			// Find if the contact has a mobile.
			// Check the primary field first.
			String email = contact.getHomeEmail();
			if (email != null && email.length() > 0)
			{
				queueTransmission(details, transmissions, dedupList, contact, email);
				continue;
			}

			 email = contact.getWorkEmail();
			if (email != null && email.length() > 0)
			{
				queueTransmission(details, transmissions, dedupList, contact, email);
				continue;
			}

			// No email address found
			EmailTransmission transmission = new EmailTransmission(contact, details.getMessage(), new RecipientException(
					"No email address on contact.", contact));
			StepShowProgress.this.progressTable.addRow(transmission);
			rejected.setValue(rejected.intValue() + 1);
		}

		if (transmissions.size() == 0)
		{
			Notification.show("None of the selected contacts have an Email address", Type.ERROR_MESSAGE);
		}
		else
		{
			queued.setValue(transmissions.size());
			progressDescription.setValue(queued.intValue() + " messages queued.");

			User user = (User) SMSession.INSTANCE.getLoggedInUser();
			SendEmailTask task = new SendEmailTask(
					this, user, details.getMessage(), transmissions, this.messagingWizardView.getDetails().getAttachedFiles());
			
			workDialog = new WorkingDialog("Sending Emails", "Sending...", task);
			
			ProgressBarWorker<EmailTransmission> worker = new ProgressBarWorker<EmailTransmission>(task);
			worker.start();

			UI.getCurrent().addWindow(workDialog);
			
			
		}

		return layout;
	}

	private void queueTransmission(StepEnterDetails details, ArrayList<EmailTransmission> transmissions,
			HashSet<String> dedupList, Contact contact, String toEmailAddress)
	{
		EmailTransmission transmission = new EmailTransmission(details.getActivityTags(), contact, details.getMessage(), toEmailAddress);
		if (!dedupList.contains(toEmailAddress))
		{
			dedupList.add(toEmailAddress);
			transmissions.add(transmission);
		}
		else
		{
			transmission.setException(new RecipientException("Duplicate email address.", contact));
			StepShowProgress.this.progressTable.addRow(transmission);
		}
	}

	@Override
	public boolean onAdvance()
	{
		return sendComplete;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	public final void taskProgress(final int count, final int max, final EmailTransmission status)
	{
		new UIUpdater(new Runnable()
		{
			@Override
			public void run()
			{
				String message = "Sending: " + count + " of " + max + " messages.";
				progressDescription.setValue(message);
				indicator.setValue((float) count / max);
				workDialog.progress(count, max, message);
				StepShowProgress.this.progressTable.addRow(status);
			}
		});
	}

	public final void taskComplete(final int sent)
	{
		new UIUpdater(new Runnable()
		{

			@Override
			public void run()
			{
				sendComplete = true;
				indicator.setValue(1.0f);

				if (StepShowProgress.this.rejected.intValue() == 0 && queued.intValue() == sent)
					progressDescription.setValue("All Email Messages have been sent successfully.");

				else
					progressDescription
							.setValue(sent
									+ " Email Message " + (sent == 1 ? "has" : "s have") + " been sent successfully. Check the list below for the reason why some of the messages failed.");
				SMNotification.show("Email batch send complete", Type.TRAY_NOTIFICATION);
				workDialog.complete(sent);
			}
		});
	}

	@Override
	public void taskItemError(EmailTransmission transmission)
	{
		this.progressTable.addRow(transmission);

	}

	@Override
	public void taskException(Exception e)
	{
		Notification.show("Error occurred sending Message.", e.getMessage(), Type.ERROR_MESSAGE);

	}
}
