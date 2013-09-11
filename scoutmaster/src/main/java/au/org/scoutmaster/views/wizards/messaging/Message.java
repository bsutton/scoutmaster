package au.org.scoutmaster.views.wizards.messaging;

import au.org.scoutmaster.domain.Phone;

public class Message
{
	private final String subject;
	private final String body;
	private final Phone sender;

	public Message(String subject, String body, Phone sender)
	{
		this.subject = subject;
		this.body = body;
		this.sender = sender;
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
