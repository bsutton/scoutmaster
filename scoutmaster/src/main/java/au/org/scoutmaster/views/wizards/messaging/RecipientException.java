package au.org.scoutmaster.views.wizards.messaging;

import au.org.scoutmaster.domain.Contact;

public class RecipientException extends Exception
{
	private static final long serialVersionUID = 1L;
	private Contact contact;
	private String cause;

	RecipientException(String cause, Contact contact)
	{
		this.contact = contact;
		this.cause = cause;
	}
	
	public String getMessage()
	{
		return this.contact.getFirstname() + " " + this.contact.getLastname()
				+ " rejected due to : " + cause;
	}
	
}
