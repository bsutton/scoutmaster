package au.org.scoutmaster.views.wizards.bulkEmail;

import java.io.StringWriter;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.util.VelocityFormatException;

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

	public String expandBody(User user, Contact contact) throws VelocityFormatException
	{
		String result= null;
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute" );
		velocityEngine.setProperty("runtime.log.logsystem.log4j.logger","velocity");
		velocityEngine.init();


		StringWriter sw = new StringWriter();
		VelocityContext context = new VelocityContext();
		context.put("user", user);
		context.put("contact", contact);

		try
		{
			// html escaping cause velocity problems so we need to unescape before we past the body to velocity.
			String unescapedBody = StringEscapeUtils.unescapeHtml(this.body);
			if (!velocityEngine.evaluate(context, sw, user.getFullname(), unescapedBody))
				throw new RuntimeException("Error processing Velocity macro for email body. Check error log");
			
			// now we need to re-escape the body.
			//result = StringEscapeUtils.escapeHtml(sw.toString());
			result = sw.toString();
		}
		catch (Throwable e)
		{
			throw new VelocityFormatException(e);

		}

		return result;
	}

	public StringBuffer expandSubject(User user, Contact contact) throws VelocityFormatException
	{
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute" );
		velocityEngine.setProperty("runtime.log.logsystem.log4j.logger","velocity");
		velocityEngine.init();

		StringWriter sw = new StringWriter();
		VelocityContext context = new VelocityContext();
		context.put("user", user);
		context.put("contact", contact);

		try
		{
			if (!velocityEngine.evaluate(context, sw, user.getFullname(), this.subject))
				throw new RuntimeException("Error processing Velocity macro for email body. Check error log");
		}
		catch (Throwable e)
		{
			throw new VelocityFormatException(e);

		}

		return sw.getBuffer();
	}

}
