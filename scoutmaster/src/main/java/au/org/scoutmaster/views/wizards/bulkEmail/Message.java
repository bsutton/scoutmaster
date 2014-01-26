package au.org.scoutmaster.views.wizards.bulkEmail;

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

	public StringBuffer expandBody(User user, Contact contact)
	{
			VelocityEngine velocityEngine = new VelocityEngine();
			velocityEngine.init();
		
			StringWriter sw = new StringWriter();
			VelocityContext context = new VelocityContext();
			context.put("user", user);
			context.put("contact", contact);
			
			if (!velocityEngine.evaluate(context, sw, user.getFullname(), this.body))
				throw new RuntimeException("Error processing Velocity macro for email body. Check error log");
			
			return sw.getBuffer();
	}
	
	public StringBuffer expandSubject(User user, Contact contact)
	{
			VelocityEngine velocityEngine = new VelocityEngine();
			velocityEngine.init();
		
			StringWriter sw = new StringWriter();
			VelocityContext context = new VelocityContext();
			context.put("user", user);
			context.put("contact", contact);
			
			if (!velocityEngine.evaluate(context, sw, user.getFullname(), this.subject))
				throw new RuntimeException("Error processing Velocity macro for email body. Check error log");
			
			return sw.getBuffer();
	}

}
