package au.org.scoutmaster.util;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

public class SMNotification extends Notification
{
	private static final long serialVersionUID = 1L;

	public SMNotification(String caption)
	{
		super(caption);
	}

	public static void show(String caption, Type type)
	{

		Notification notification = new Notification(caption, type);
		if (type == Type.TRAY_NOTIFICATION)
			notification.setPosition(Position.BOTTOM_LEFT);
		notification.show(Page.getCurrent());

	}

	public static void show(String caption, String description, Type type)
	{
		Notification notification = new Notification(caption, description, type);
		if (type == Type.TRAY_NOTIFICATION)
			notification.setPosition(Position.BOTTOM_LEFT);
		notification.show(Page.getCurrent());
	}

	public static void show(Exception e, Type type)
	{
		// find root cause.
		Throwable rootCause = e;
		
		while (rootCause.getCause() != null)
			rootCause = rootCause.getCause();
		
		show(rootCause.getClass().getSimpleName() + ":" + rootCause.getMessage(), type);
	}
}
