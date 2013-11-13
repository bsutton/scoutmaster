package au.org.scoutmaster.views.wizards.bulkEmail;

import au.org.scoutmaster.domain.Phone;

public class Message
{
	private final String subject;
	private final String body;
	private final Phone senderPhone;
	private final String senderEmailAddress;

	public Message(String subject, String body, Phone senderPhone)
	{
		this.subject = subject;
		this.body = body;
		this.senderPhone = senderPhone;
		this.senderEmailAddress = null;
	}

	public Message(String subject, String body, String senderEmailAddress)
	{
		this.subject = subject;
		this.body = body;
		this.senderEmailAddress = senderEmailAddress;
		this.senderPhone = null;
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
		return this.senderPhone;
	}
	
	public String getSenderEmailAddress()
	{
		return this.senderEmailAddress;
	}

}
