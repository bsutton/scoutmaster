package au.org.scoutmaster.views.wizards.bulkSMS;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMSProviderDao;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.fields.TagField;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class MessageDetailsStep implements WizardStep
{

	private final TextField subject;
	private final TextArea message;
	private final Label remaining;

	// SMFormHelper<SMSProvider> formHelper;
	private final TextField from;
	private final ComboBox providers;
	private final BulkSMSWizardView wizard;
	private final MultiColumnFormLayout<SMSProvider> formLayout;
	private final VerticalLayout layout;
	private final Label recipientCount;
	private final TagField tag;

	public MessageDetailsStep(final BulkSMSWizardView messagingWizardView)
	{
		this.wizard = messagingWizardView;

		this.layout = new VerticalLayout();
		this.layout.setDescription("MessageDetailsContent");

		this.layout.addComponent(new Label("Enter the subject and message and then click next."));
		this.formLayout = new MultiColumnFormLayout<>(1, null);
		this.formLayout.setColumnFieldWidth(0, 500);
		this.formLayout.setSizeFull();

		final SMSProviderDao daoSMSProvider = new DaoFactory().getSMSProviderDao();
		final List<SMSProvider> providerList = daoSMSProvider.findAll();
		if (providerList.size() == 0)
		{
			throw new IllegalStateException("You must first configure an SMS Provider");
		}
		final SMSProvider provider = providerList.get(0);

		this.providers = new ComboBox("Provider");
		this.providers.setContainerDataSource(daoSMSProvider.createVaadinContainer());
		this.providers.setConverter(SMSProvider.class);
		this.providers.select(provider.getId());

		// Only give the user the option to select a provider if there is more
		// than one of them.
		if (providerList.size() > 1)
		{
			this.layout.addComponent(this.providers);
		}

		this.recipientCount = new Label();
		this.recipientCount.setContentMode(ContentMode.HTML);
		this.layout.addComponent(this.recipientCount);

		this.tag = new TagField("Activity Tag", false);
		this.tag.setWidth("100%");
		this.tag.setDescription("Enter a tag to associate with each Contact we successfully send to.");
		this.layout.addComponent(this.tag);

		this.from = this.formLayout.bindTextField("From Mobile No.", "from");
		this.from.addValidator(new StringLengthValidator("'From Mobile' must be supplied", 1, 15, false));
		final User user = SMSession.INSTANCE.getLoggedInUser();
		String senderID = provider.getDefaultSenderID();
		if (user.getSenderMobile() != null && user.getSenderMobile().length() > 0)
		{
			senderID = user.getSenderMobile();
		}
		this.from.setValue(senderID);
		this.from
				.setDescription("Enter your mobile phone no. so that all messages appear to come from you and recipients can send a text directly back to your phone.");
		this.subject = this.formLayout.bindTextField("Subject", "subject");
		this.subject.addValidator(new StringLengthValidator("'Subject' must be supplied", 1, 255, false));
		this.message = this.formLayout.bindTextAreaField("Message", "message", 4);
		this.message.addValidator(new StringLengthValidator("'Message' must be supplied", 1, 160, false));
		this.remaining = this.formLayout.bindLabel("Characters remaining 160");
		this.remaining.setImmediate(true);

		this.layout.addComponent(this.formLayout);
		this.layout.setMargin(true);

		this.message.addTextChangeListener(new TextChangeListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void textChange(final TextChangeEvent event)
			{
				MessageDetailsStep.this.remaining.setValue("Characters remaining " + (160 - event.getText().length()));

			}
		});

	}

	@Override
	public String getCaption()
	{
		return "Message Details";
	}

	@Override
	public Component getContent()
	{
		this.recipientCount.setValue("<p><b>" + this.wizard.getRecipientStep().getRecipientCount()
				+ " recipients have been selected to recieve the following SMS.</b></p>");

		return this.layout;
	}

	@Override
	public boolean onAdvance()
	{

		final boolean advance = notEmpty("Message", this.message.getValue()) && notEmpty("From", this.from.getValue())
				&& notEmpty("Subject", this.subject.getValue());

		if (!advance)
		{
			Notification.show("Please enter your Mobile, Subject and a Message then click Next");
		}
		return advance;
	}

	private boolean notEmpty(final String label, final String value)
	{
		return value != null && value.length() > 0;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	public Message getMessage()
	{
		return new Message(this.subject.getValue(), this.message.getValue(), new Phone(this.from.getValue()));
	}

	public SMSProvider getProvider()
	{
		final Long providerId = (Long) this.providers.getValue();

		final SMSProviderDao dao = new DaoFactory().getSMSProviderDao();
		return dao.findById(providerId);
	}

	public String getFrom()
	{
		return this.from.getValue();
	}

	public String getSubject()
	{
		return this.subject.getValue();
	}

	public ArrayList<Tag> getActivityTags()
	{
		return this.tag.getTags();
	}

}
