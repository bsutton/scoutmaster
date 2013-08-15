package au.org.scoutmaster.views.wizards.messaging;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.PhoneDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Importable;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.util.ProgressBarWorker;
import au.org.scoutmaster.util.ProgressTaskListener;
import au.org.scoutmaster.views.MessagingWizardView;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class SendMessageStep implements WizardStep, ProgressTaskListener
{
	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(SendMessageStep.class);
	JPAContainer<? extends Importable> entities;
	private MessagingWizardView messagingWizardView;
	private boolean sendComplete = false;
	private ProgressBar indicator;
	private Label progressDescription;

	public SendMessageStep(MessagingWizardView messagingWizardView)
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

		progressDescription = new Label();
		layout.addComponent(progressDescription);
		layout.setMargin(true);
		indicator = new ProgressBar(new Float(0.0));
		indicator.setHeight("30px");
		indicator.setIndeterminate(false);
		indicator.setImmediate(true);
		indicator.setSizeFull();
		layout.addComponent(indicator);

		ContactDao daoContact = new ContactDao();
		List<Contact> contacts = daoContact.findAll();
		ArrayList<Phone> phones = new ArrayList<>();

		for (Contact contact : contacts)
		{
			PhoneDao daoPhone = new PhoneDao();

			if (!daoPhone.isEmpty(contact.getPhone3()))
				phones.add(contact.getPhone3());
		}

		if (phones.size() == 0)
		{
			Notification.show("None of the selected contacts have a mobile phone no.", Type.ERROR_MESSAGE);
		}
		else
		{
			progressDescription.setValue("Sent: 0 of " + contacts.size() + " messages.");

			MessageDetailsStep enter = messagingWizardView.getDetails();

			SMSProvider provider = messagingWizardView.getDetails().getProvider();

			ProgressBarWorker worker = new ProgressBarWorker(new SendMessageTask(this, provider, enter.getMessage(),
					phones));
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

	public final void taskProgress(final int count, final int max)
	{
		UI.getCurrent().access(new Runnable()
		{
			@Override
			public void run()
			{
				progressDescription.setValue("Sent: " + count + " of " + max + " messages.");
				indicator.setValue((float) count / max);
			}
		});
	}

	public final void taskComplete()
	{
		indicator.setValue(1.0f);
		progressDescription.setValue("SMS Messages have been sent.");
		sendComplete = true;
	}
}
