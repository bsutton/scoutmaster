package au.org.scoutmaster.util;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

public class SMNotification extends Notification
{
	private static final long serialVersionUID = 1L;

	public SMNotification(final String caption)
	{
		super(caption);
	}

	public static void show(final String caption, final Type type)
	{
		UI ui = UI.getCurrent();
		ui.access(() -> {
			final Notification notification = new Notification(caption, type);
			if (type == Type.TRAY_NOTIFICATION)
			{
				notification.setPosition(Position.BOTTOM_LEFT);
			}
			notification.show(Page.getCurrent());
		});

	}

	public static void show(final String caption, final String description, final Type type)
	{
		UI ui = UI.getCurrent();
		ui.access(() -> {

			final Notification notification = new Notification(caption, description, type);
			if (type == Type.TRAY_NOTIFICATION)
			{
				notification.setPosition(Position.BOTTOM_LEFT);
			}
			notification.show(Page.getCurrent());
		});
	}

	public static void show(final Exception e, final Type type)
	{
		UI ui = UI.getCurrent();
		ui.access(() -> {

			// find root cause.
			Throwable rootCause = e;

			while (rootCause.getCause() != null)
			{
				rootCause = rootCause.getCause();
			}

			SMNotification.show(rootCause.getClass().getSimpleName() + ":" + rootCause.getMessage(), type);
		});
	}

	public static void show(final Throwable e, final Type type)
	{
		UI ui = UI.getCurrent();
		ui.access(() -> {

			// find root cause.
			Throwable rootCause = e;

			while (rootCause.getCause() != null)
			{
				rootCause = rootCause.getCause();
			}

			SMNotification.show(rootCause.getClass().getSimpleName() + ":" + rootCause.getMessage(), type);
		});
	}

}
