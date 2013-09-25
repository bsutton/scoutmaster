package au.org.scoutmaster.views.wizards.messaging;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.fields.PoJoTable;
import au.com.vaadinutils.ui.UIUpdater;
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

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ShowProgressStep implements WizardStep, ProgressTaskListener<SMSTransmission>
{
	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(ShowProgressStep.class);
	JPAContainer<? extends Importable> entities;
	private MessagingWizardView messagingWizardView;
	private boolean sendComplete = false;
	private ProgressBar indicator;
	private Label progressDescription;
	private PoJoTable<SMSTransmission> progressTable;
	private MutableInteger queued = new MutableInteger(0);
	private MutableInteger rejected = new MutableInteger(0);
	private WorkingDialog workDialog;

	public ShowProgressStep(MessagingWizardView messagingWizardView)
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

		progressTable = new PoJoTable<>(SMSTransmission.class, new String[]
		{ "ContactName", "RecipientPhoneNo", "Result" });
		progressTable.setColumnWidth("RecipientPhoneNo", 80);
		progressTable.setColumnExpandRatio("Result", 1);
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

		MessageDetailsStep enter = messagingWizardView.getDetails();
		ArrayList<SMSTransmission> transmissions = new ArrayList<>();

		HashSet<String> dedupList = new HashSet<>();
		PhoneDao daoPhone = new DaoFactory().getPhoneDao();

		for (Contact contact : recipients)
		{

			// Find if the contact has a mobile.
			// Check the primary field first.
			Phone primaryPhone = contact.getPrimaryPhone();
			if (primaryPhone != null && primaryPhone.getPhoneType() == PhoneType.MOBILE && !daoPhone.isEmpty(primaryPhone))
			{
				queueTransmission(enter, transmissions, dedupList, contact, primaryPhone);
				continue;
			}

			if (contact.getPhone1() != null && contact.getPhone1().getPhoneType() == PhoneType.MOBILE && !daoPhone.isEmpty(contact.getPhone1()))
			{
				queueTransmission(enter, transmissions, dedupList, contact, contact.getPhone1());
				continue;
			}

			if (contact.getPhone2() != null && contact.getPhone2().getPhoneType() == PhoneType.MOBILE && !daoPhone.isEmpty(contact.getPhone2()))
			{
				queueTransmission(enter, transmissions, dedupList, contact, contact.getPhone2());
				continue;
			}

			if (contact.getPhone3() != null && contact.getPhone3().getPhoneType() == PhoneType.MOBILE && !daoPhone.isEmpty(contact.getPhone3()))
			{
				queueTransmission(enter, transmissions, dedupList, contact, contact.getPhone3());
				continue;
			}

			// No mobile found
			SMSTransmission transmission = new SMSTransmission(contact, enter.getMessage(), new RecipientException(
					"No mobile no.", contact));
			ShowProgressStep.this.progressTable.addRow(transmission);
			rejected.setValue(rejected.intValue() + 1);
		}

		if (transmissions.size() == 0)
		{
			Notification.show("None of the selected contacts have a mobile phone no.", Type.ERROR_MESSAGE);
		}
		else
		{
			queued.setValue(transmissions.size());
			progressDescription.setValue(queued.intValue() + " messages queued. Initialising SMS Provider, ");

			SMSProvider provider = messagingWizardView.getDetails().getProvider();

			

			SendMessageTask task = new SendMessageTask(
					this, provider, enter.getMessage(), transmissions);
			workDialog = new WorkingDialog("Sending SMS messages", "Sending...", task);
			ProgressBarWorker<SMSTransmission> worker = new ProgressBarWorker<SMSTransmission>(task);
			worker.start();
			

			UI.getCurrent().addWindow(workDialog);

		}

		return layout;
	}

	private void queueTransmission(MessageDetailsStep enter, ArrayList<SMSTransmission> transmissions,
			HashSet<String> dedupList, Contact contact, Phone primaryPhone)
	{
		SMSTransmission transmission = new SMSTransmission(contact, enter.getMessage(), primaryPhone);
		String phone = primaryPhone.getPhoneNo();
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
		return sendComplete;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	public final void taskProgress(final int count, final int max, final SMSTransmission status)
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

				ShowProgressStep.this.progressTable.addRow(status);
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

				if (ShowProgressStep.this.rejected.intValue() == 0 && queued.intValue() == sent)
					progressDescription.setValue("All SMS Messages have been sent successfully.");

				else
					progressDescription
							.setValue(sent
									+ " SMS Message " + (sent == 1 ? "has" : "s have") + " been sent successfully. Check the list below for the reason why some of the messages failed.");
				workDialog.complete(sent);

			}
		});
	}

	@Override
	public void taskItemError(SMSTransmission transmission)
	{
		this.progressTable.addRow(transmission);

	}

	@Override
	public void taskException(Exception e)
	{
		Notification.show("Error occurred sending Message.", e.getMessage(), Type.ERROR_MESSAGE);

	}
}
