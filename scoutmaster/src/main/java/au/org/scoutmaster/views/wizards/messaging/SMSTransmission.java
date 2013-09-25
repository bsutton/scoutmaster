package au.org.scoutmaster.views.wizards.messaging;

import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.PhoneType;

public class SMSTransmission
{
	private Contact contact;
	private Message message;
	private Phone recipient;
	// If an exception is thrown during transmission it is stored here.
	private Exception exception;

	public SMSTransmission(Contact contact, Message message, Phone recipient)
	{
		this.contact = contact;
		this.message = message;
		this.recipient = recipient;

		if (recipient.getPhoneType() != PhoneType.MOBILE)
			throw new IllegalArgumentException("The phone argument must be of type MOBILE");
	}

	public SMSTransmission(Contact contact, Message message, Exception exception)
	{
		this.contact = contact;
		this.message = message;
		this.exception = exception;
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
			return this.recipient.getPhoneNo();
		else
			return "";
	}

	public void setException(Exception e)
	{
		this.exception = e;

	}

	public String getResult()
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

}
