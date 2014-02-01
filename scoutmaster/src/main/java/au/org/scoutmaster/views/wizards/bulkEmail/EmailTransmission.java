package au.org.scoutmaster.views.wizards.bulkEmail;

import java.util.ArrayList;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Tag;

public class EmailTransmission
{
	private Contact contact;
	private Message message;
	private String recipientEmailAddress;
	// If an exception is thrown during transmission it is stored here.
	private Exception exception;
	private ArrayList<Tag> activityTags;

	public EmailTransmission(ArrayList<Tag> activityTags, Contact contact, Message message, String recipient)
	{
		this.contact = contact;
		this.message = message;
		this.recipientEmailAddress = recipient;
		this.activityTags = activityTags;
		
		// We are about to pass these to a new thread and em so we must detach them.
		EntityManagerProvider.detach(contact);
		for (Tag tag: this.activityTags)
			EntityManagerProvider.detach(tag);
	}

	public EmailTransmission(Contact contact, Message message, Exception exception)
	{
		this.contact = contact;
		this.message = message;
		this.exception = exception;
	}

	public Message getMessage()
	{
		return this.message;
	}

	public String getRecipient()
	{
		return this.recipientEmailAddress;
	}

	public String getRecipientPhoneNo()
	{
		return this.recipientEmailAddress;
	}

	public void setException(Exception e)
	{
		this.exception = e;

	}

	public String getException()
	{
		if (exception != null)
			return exception.getClass().getSimpleName() + ": " + exception.getMessage();
		else
			return "Success";
	}

	public String getContactName()
	{
		return this.contact.getFirstname() + " " + this.contact.getLastname();
	}

	public Contact getContact()
	{
		return contact;
	}

	public ArrayList<Tag> getActivityTags()
	{
		return activityTags;
	}

}
