package au.org.scoutmaster.views.wizards.bulkEmail;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.fields.PoJoTable;
import au.com.vaadinutils.ui.WorkingDialog;
import au.com.vaadinutils.util.MutableInteger;
import au.com.vaadinutils.util.ProgressBarWorker;
import au.com.vaadinutils.util.ProgressTaskListener;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Importable;
import au.org.scoutmaster.domain.security.User;
import au.org.scoutmaster.util.SMNotification;

public class StepShowProgress implements WizardStep, ProgressTaskListener<EmailTransmission>
{
	@SuppressWarnings("unused")
	static private Logger logger = LogManager.getLogger(StepShowProgress.class);
	JPAContainer<? extends Importable> entities;
	private final BulkEmailWizardView messagingWizardView;
	private boolean sendComplete = false;
	private ProgressBar indicator;
	private Label progressDescription;
	private PoJoTable<EmailTransmission> progressTable;
	private final MutableInteger queued = new MutableInteger(0);
	private final MutableInteger rejected = new MutableInteger(0);
	private WorkingDialog workDialog;

	public StepShowProgress(final BulkEmailWizardView messagingWizardView)
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
		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		this.progressTable = new PoJoTable<>(EmailTransmission.class, new String[]
		{ "ContactName", "Recipient", "Exception" });
		this.progressTable.setColumnWidth("Recipient", 80);
		this.progressTable.setColumnExpandRatio("Exception", 1);
		this.progressTable.setSizeFull();
		this.progressDescription = new Label();
		layout.addComponent(this.progressDescription);
		layout.setMargin(true);
		this.indicator = new ProgressBar(new Float(0.0));
		this.indicator.setHeight("30px");
		this.indicator.setIndeterminate(false);
		this.indicator.setImmediate(true);
		this.indicator.setSizeFull();
		layout.addComponent(this.indicator);
		layout.addComponent(this.progressTable);
		layout.setExpandRatio(this.progressTable, 1);

		final ArrayList<Contact> recipients = this.messagingWizardView.getRecipientStep().getRecipients();

		final StepEnterDetails details = this.messagingWizardView.getDetails();
		final ArrayList<EmailTransmission> transmissions = new ArrayList<>();

		final HashSet<String> dedupList = new HashSet<>();

		for (final Contact contact : recipients)
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
			final EmailTransmission transmission = new EmailTransmission(contact, details.getMessage(),
					new RecipientException("No email address on contact.", contact));
			StepShowProgress.this.progressTable.addRow(transmission);
			this.rejected.setValue(this.rejected.intValue() + 1);
		}

		if (transmissions.size() == 0)
		{
			Notification.show("None of the selected contacts have an Email address", Type.ERROR_MESSAGE);
		}
		else
		{
			this.queued.setValue(transmissions.size());
			this.progressDescription.setValue(this.queued.intValue() + " messages queued.");

			final User user = SMSession.INSTANCE.getLoggedInUser();
			final SendEmailTask task = new SendEmailTask(this, user, details.getMessage(), transmissions,
					this.messagingWizardView.getDetails().getAttachedFiles());

			this.workDialog = new WorkingDialog("Sending Emails", "Sending...", task);

			final ProgressBarWorker<EmailTransmission> worker = new ProgressBarWorker<EmailTransmission>(task);
			worker.start();

			UI.getCurrent().addWindow(this.workDialog);

		}

		return layout;
	}

	private void queueTransmission(final StepEnterDetails details, final ArrayList<EmailTransmission> transmissions,
			final HashSet<String> dedupList, final Contact contact, final String toEmailAddress)
	{
		final EmailTransmission transmission = new EmailTransmission(details.getActivityTags(), contact,
				details.getMessage(), toEmailAddress);
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
		return this.sendComplete;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	@Override
	public final void taskProgress(final int count, final int max, final EmailTransmission status)
	{
		UI ui = UI.getCurrent();
		ui.access(() -> {
			final String message = "Sending: " + count + " of " + max + " messages.";
			StepShowProgress.this.progressDescription.setValue(message);
			StepShowProgress.this.indicator.setValue((float) count / max);
			StepShowProgress.this.workDialog.progress(count, max, message);
			StepShowProgress.this.progressTable.addRow(status);
		});
	}

	@Override
	public final void taskComplete(final int sent)
	{
		UI ui = UI.getCurrent();
		ui.access(() -> {
			StepShowProgress.this.sendComplete = true;
			StepShowProgress.this.indicator.setValue(1.0f);

			if (StepShowProgress.this.rejected.intValue() == 0 && StepShowProgress.this.queued.intValue() == sent)
			{
				StepShowProgress.this.progressDescription.setValue("All Email Messages have been sent successfully.");
			}
			else
			{
				StepShowProgress.this.progressDescription.setValue(sent + " Email Message "
						+ (sent == 1 ? "has" : "s have")
						+ " been sent successfully. Check the list below for the reason why some of the messages failed.");
			}
			SMNotification.show("Email batch send complete", Type.TRAY_NOTIFICATION);
			StepShowProgress.this.workDialog.complete(sent);
		});
	}

	@Override
	public void taskItemError(final EmailTransmission transmission)
	{
		this.progressTable.addRow(transmission);

	}

	@Override
	public void taskException(final Exception e)
	{
		Notification.show("Error occurred sending Message.", e.getMessage(), Type.ERROR_MESSAGE);

	}
}
