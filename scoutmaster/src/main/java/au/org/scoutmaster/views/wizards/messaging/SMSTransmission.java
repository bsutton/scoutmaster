package au.org.scoutmaster.views.wizards.messaging;

import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.PhoneType;

public class SMSTransmission
{
	Contact contact;
	Message message;
	Phone phone;
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
		return this.phone.getPhoneNo();
	}

	public void setException(Exception e)
	{
		this.exception = e;
		
	}
	public Exception getException()
	{
		return exception;
	}
	
	public String getContactName()
	{
		return this.contact.getFirstname() + " " + this.contact.getLastname();
	}

}
