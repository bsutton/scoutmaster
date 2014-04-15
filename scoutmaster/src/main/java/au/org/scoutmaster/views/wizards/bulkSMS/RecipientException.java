package au.org.scoutmaster.views.wizards.bulkSMS;

import au.org.scoutmaster.domain.Contact;

public class RecipientException extends Exception
{
	private static final long serialVersionUID = 1L;
	private final Contact contact;
	private final String cause;

	RecipientException(final String cause, final Contact contact)
	{
		this.contact = contact;
		this.cause = cause;
	}

	@Override
	public String getMessage()
	{
		return this.contact.getFirstname() + " " + this.contact.getLastname() + " rejected due to : " + this.cause;
	}

}
