package au.org.scoutmaster.views.wizards.messaging;

import au.org.scoutmaster.domain.Phone;

public class Message
{
	private final String subject;
	private final String body;
	private final Phone sender;

	public Message(String subject, String body, Phone phone)
	{
		this.subject = subject;
		this.body = body;
		this.sender = phone;
	}

	public String getSubject()
	{
		return subject;
	}

	public String getBody()
	{
		return body;
	}

	public Phone getSender()
	{
		return sender;
	}

}
