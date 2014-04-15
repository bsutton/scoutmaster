package au.org.scoutmaster.views.wizards.bulkSMS;

import java.util.ArrayList;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.PhoneType;
import au.org.scoutmaster.domain.Tag;

public class SMSTransmission
{
	private final Contact contact;
	private final Message message;
	private Phone recipient;
	// If an exception is thrown during transmission it is stored here.
	private Exception exception;
	private final ArrayList<Tag> activityTags;

	public SMSTransmission(final ArrayList<Tag> activityTags, final Contact contact, final Message message,
			final Phone recipient)
	{
		this.contact = contact;
		this.message = message;
		this.recipient = recipient;
		this.activityTags = activityTags;

		if (recipient.getPhoneType() != PhoneType.MOBILE)
		{
			throw new IllegalArgumentException("The phone argument must be of type MOBILE");
		}

		// We are about to pass these to a new thread and em so we must detach
		// them.
		EntityManagerProvider.detach(contact);
		for (final Tag tag : this.activityTags)
		{
			EntityManagerProvider.detach(tag);
		}

	}

	public SMSTransmission(final Contact contact, final Message message, final Exception exception)
	{
		this.contact = contact;
		this.message = message;
		this.exception = exception;
		this.activityTags = new ArrayList<>();

		// We are about to pass these to a new thread and em so we must detach
		// them.
		EntityManagerProvider.detach(contact);

	}

	public Message getMessage()
	{
		return this.message;
	}

	public Phone getRecipient()
	{
		return this.recipient;
	}

	public String getRecipientPhoneNo()
	{
		if (this.recipient != null)
		{
			return this.recipient.getPhoneNo();
		}
		else
		{
			return "";
		}
	}

	public void setException(final Exception e)
	{
		this.exception = e;

	}

	public String getResult()
	{
		if (this.exception != null)
		{
			return this.exception.getClass().getSimpleName() + ": " + this.exception.getMessage();
		}
		else
		{
			return "Success";
		}
	}

	public String getContactName()
	{
		return this.contact.getFirstname() + " " + this.contact.getLastname();
	}

	public Contact getContact()
	{
		return this.contact;
	}

	public ArrayList<Tag> getActivityTags()
	{
		return this.activityTags;
	}

}
