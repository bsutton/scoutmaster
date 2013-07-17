package au.org.scoutmaster.views.messagingWizard;

public class Message
{
	private final String subject;
	private final String body;
	private final String sender;

	public Message(String subject, String body, String sender)
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

	public String getSender()
	{
		return sender;
	}

}
