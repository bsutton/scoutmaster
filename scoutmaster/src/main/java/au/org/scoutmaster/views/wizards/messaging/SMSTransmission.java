package au.org.scoutmaster.views.wizards.messaging;

import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.PhoneType;

public class SMSTransmission
{
	private Contact contact;
	private Message message;
	private Phone phone;
	// If an exception is thrown during transmission it is stored here.
	private Exception exception;

	public SMSTransmission(Contact contact, Message message, Phone phone)
	{
		this.contact = contact;
		this.message = message;
		this.phone = phone;

		if (phone.getPhoneType() != PhoneType.MOBILE)
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

	public Phone getPhone()
	{
		return this.phone;
	}

	public String getPhoneNo()
	{
		if (this.phone != null)
			return this.phone.getPhoneNo();
		else
			return "";
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
			return "";
	}

	public String getContactName()
	{
		return this.contact.getFirstname() + " " + this.contact.getLastname();
	}

}
