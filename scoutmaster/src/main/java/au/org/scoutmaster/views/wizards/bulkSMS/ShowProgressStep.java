package au.org.scoutmaster.views.wizards.bulkSMS;

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
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.PhoneDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Importable;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.PhoneType;
import au.org.scoutmaster.domain.SMSProvider;

public class ShowProgressStep implements WizardStep, ProgressTaskListener<SMSTransmission>
{
	@SuppressWarnings("unused")
	static private Logger logger = LogManager.getLogger(ShowProgressStep.class);
	JPAContainer<? extends Importable> entities;
	private final BulkSMSWizardView messagingWizardView;
	private boolean sendComplete = false;
	private ProgressBar indicator;
	private Label progressDescription;
	private PoJoTable<SMSTransmission> progressTable;
	private final MutableInteger queued = new MutableInteger(0);
	private final MutableInteger rejected = new MutableInteger(0);
	private WorkingDialog workDialog;

	public ShowProgressStep(final BulkSMSWizardView messagingWizardView)
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

		this.progressTable = new PoJoTable<>(SMSTransmission.class, new String[]
		{ "ContactName", "RecipientPhoneNo", "Result" });
		this.progressTable.setColumnWidth("RecipientPhoneNo", 80);
		this.progressTable.setColumnExpandRatio("Result", 1);
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

		final MessageDetailsStep detailsStep = this.messagingWizardView.getDetails();
		final ArrayList<SMSTransmission> transmissions = new ArrayList<>();

		final HashSet<String> dedupList = new HashSet<>();
		final PhoneDao daoPhone = new DaoFactory().getPhoneDao();

		for (final Contact contact : recipients)
		{

			// Find if the contact has a mobile.
			// Check the primary field first.
			final Phone primaryPhone = contact.getPrimaryPhone();
			if (primaryPhone != null && primaryPhone.getPhoneType() == PhoneType.MOBILE
					&& !daoPhone.isEmpty(primaryPhone))
			{
				queueTransmission(detailsStep, transmissions, dedupList, contact, primaryPhone);
				continue;
			}

			if (contact.getPhone1() != null && contact.getPhone1().getPhoneType() == PhoneType.MOBILE
					&& !daoPhone.isEmpty(contact.getPhone1()))
			{
				queueTransmission(detailsStep, transmissions, dedupList, contact, contact.getPhone1());
				continue;
			}

			if (contact.getPhone2() != null && contact.getPhone2().getPhoneType() == PhoneType.MOBILE
					&& !daoPhone.isEmpty(contact.getPhone2()))
			{
				queueTransmission(detailsStep, transmissions, dedupList, contact, contact.getPhone2());
				continue;
			}

			if (contact.getPhone3() != null && contact.getPhone3().getPhoneType() == PhoneType.MOBILE
					&& !daoPhone.isEmpty(contact.getPhone3()))
			{
				queueTransmission(detailsStep, transmissions, dedupList, contact, contact.getPhone3());
				continue;
			}

			// No mobile found
			final SMSTransmission transmission = new SMSTransmission(contact, detailsStep.getMessage(),
					new RecipientException("No mobile no.", contact));
			ShowProgressStep.this.progressTable.addRow(transmission);
			this.rejected.setValue(this.rejected.intValue() + 1);
		}

		if (transmissions.size() == 0)
		{
			Notification.show("None of the selected contacts have a mobile phone no.", Type.ERROR_MESSAGE);
		}
		else
		{
			this.queued.setValue(transmissions.size());
			this.progressDescription.setValue(this.queued.intValue() + " messages queued. Initialising SMS Provider, ");

			final SMSProvider provider = this.messagingWizardView.getDetails().getProvider();

			final SendMessageTask task = new SendMessageTask(this, provider, detailsStep.getMessage(), transmissions);
			this.workDialog = new WorkingDialog("Sending SMS messages", "Sending...", task);
			final ProgressBarWorker<SMSTransmission> worker = new ProgressBarWorker<SMSTransmission>(task);
			worker.start();

			UI.getCurrent().addWindow(this.workDialog);

		}

		return layout;
	}

	private void queueTransmission(final MessageDetailsStep detailsStep, final ArrayList<SMSTransmission> transmissions,
			final HashSet<String> dedupList, final Contact contact, final Phone primaryPhone)
	{
		final SMSTransmission transmission = new SMSTransmission(detailsStep.getActivityTags(), contact,
				detailsStep.getMessage(), primaryPhone);
		final String phone = primaryPhone.getPhoneNo();
		if (!dedupList.contains(phone))
		{
			dedupList.add(phone);
			transmissions.add(transmission);
		}
		else
		{
			transmission.setException(new RecipientException("Duplicate phone no.", contact));
			ShowProgressStep.this.progressTable.addRow(transmission);
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
	public final void taskProgress(final int count, final int max, final SMSTransmission status)
	{
		UI ui = UI.getCurrent();
		ui.access(() -> {
			final String message = "Sending: " + count + " of " + max + " messages.";
			ShowProgressStep.this.progressDescription.setValue(message);
			ShowProgressStep.this.indicator.setValue((float) count / max);
			ShowProgressStep.this.workDialog.progress(count, max, message);

			ShowProgressStep.this.progressTable.addRow(status);
		});
	}

	@Override
	public final void taskComplete(final int sent)
	{
		UI ui = UI.getCurrent();
		ui.access(() -> {
			ShowProgressStep.this.sendComplete = true;
			ShowProgressStep.this.indicator.setValue(1.0f);

			if (ShowProgressStep.this.rejected.intValue() == 0 && ShowProgressStep.this.queued.intValue() == sent)
			{
				ShowProgressStep.this.progressDescription.setValue("All SMS Messages have been sent successfully.");
			}
			else
			{
				ShowProgressStep.this.progressDescription.setValue(sent + " SMS Message "
						+ (sent == 1 ? "has" : "s have")
						+ " been sent successfully. Check the list below for the reason why some of the messages failed.");
			}
			ShowProgressStep.this.workDialog.complete(sent);

		});
	}

	@Override
	public void taskItemError(final SMSTransmission transmission)
	{
		this.progressTable.addRow(transmission);

	}

	@Override
	public void taskException(final Exception e)
	{
		Notification.show("Error occurred sending Message.", e.getMessage(), Type.ERROR_MESSAGE);

	}
}
