package au.org.scoutmaster.views.wizards.bulkSMS;

import java.io.StringWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.access.User;

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
	
	public StringBuffer expandBody(User user, Contact contact)
	{
			VelocityEngine velocityEngine = new VelocityEngine();
			velocityEngine.init();
		
			StringWriter sw = new StringWriter();
			VelocityContext context = new VelocityContext();
			context.put("user", user);
			context.put("contact", contact);
			
			if (!velocityEngine.evaluate(context, sw, user.getFullname(), this.body))
				throw new RuntimeException("Error processing Velocity macro for SMS body. Check error log");
			
			return sw.getBuffer();
	}
}
