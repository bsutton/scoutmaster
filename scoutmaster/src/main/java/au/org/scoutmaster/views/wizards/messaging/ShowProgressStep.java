package au.org.scoutmaster.views.wizards.messaging;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.org.scoutmaster.dao.PhoneDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Importable;
import au.org.scoutmaster.domain.PhoneType;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.fields.PoJoTable;
import au.org.scoutmaster.util.ProgressBarWorker;
import au.org.scoutmaster.util.ProgressTaskListener;

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

	public ShowProgressStep(MessagingWizardView messagingWizardView)
	{
		this.messagingWizardView = messagingWizardView;
	}

	@Override
	public String getCaption()
	{
		return "Sending Message.";
	}

	@Override
	public Component getContent()
	{
		VerticalLayout layout = new VerticalLayout();

		progressTable = new PoJoTable<>(new String[]{"ContactName", "PhoneNo", "Exception"});
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
		this.progressTable.setSizeFull();

		ArrayList<Contact> recipients = messagingWizardView.getRecipientStep().getRecipients();
		
		MessageDetailsStep enter = messagingWizardView.getDetails();
		ArrayList<SMSTransmission> transmissions = new ArrayList<>();

		for (Contact contact : recipients)
		{
			PhoneDao daoPhone = new PhoneDao();

			// Find if the contact has a mobile.
			// Check the primary field first.
			if (contact.getPrimaryPhone().getPhoneType() == PhoneType.MOBILE
					&& !daoPhone.isEmpty(contact.getPrimaryPhone()))
			{
				transmissions.add(new SMSTransmission(contact, enter.getMessage(), contact.getPrimaryPhone()));
				continue;
			}

			if (contact.getPhone1().getPhoneType() == PhoneType.MOBILE && !daoPhone.isEmpty(contact.getPhone1()))
			{
				transmissions.add(new SMSTransmission(contact, enter.getMessage(), contact.getPhone1()));
				continue;
			}

			if (contact.getPhone2().getPhoneType() == PhoneType.MOBILE && !daoPhone.isEmpty(contact.getPhone2()))
			{
				transmissions.add(new SMSTransmission(contact, enter.getMessage(), contact.getPhone2()));
				continue;
			}

			if (contact.getPhone3().getPhoneType() == PhoneType.MOBILE && !daoPhone.isEmpty(contact.getPhone3()))
			{
				transmissions.add(new SMSTransmission(contact, enter.getMessage(), contact.getPhone3()));
				continue;
			}

		}

		if (transmissions.size() == 0)
		{
			Notification.show("None of the selected contacts have a mobile phone no.", Type.ERROR_MESSAGE);
		}
		else
		{
			progressDescription.setValue("Initialising SMS Provider");

			SMSProvider provider = messagingWizardView.getDetails().getProvider();

			ProgressBarWorker<SMSTransmission> worker = new ProgressBarWorker<SMSTransmission>(new SendMessageTask(
					this, provider, enter.getMessage(), transmissions));
			worker.start();
		}

		return layout;
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
		UI.getCurrent().access(new Runnable()
		{
			@Override
			public void run()
			{
				progressDescription.setValue("Sending: " + count + " of " + max + " messages.");
				indicator.setValue((float) count / max);
				ShowProgressStep.this.progressTable.addRow(status);
			}
		});
	}

	public final void taskComplete()
	{
		UI.getCurrent().access(new Runnable()
		{

			@Override
			public void run()
			{
				indicator.setValue(1.0f);
				progressDescription.setValue("SMS Messages have been sent. Check the list below for any failures.");
				sendComplete = true;
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
