package au.org.scoutmaster.application;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

import au.org.scoutmaster.domain.access.User;

public enum SMSession
{
	INSTANCE;

	static private final String USER = "user";

	public User getLoggedInUser()
	{
		return (User) UI.getCurrent().getSession().getAttribute(SMSession.USER);
	}

	public void setLoggedInUser(final User user)
	{
		final VaadinSession session = UI.getCurrent().getSession();

		if (user != null)
		{
			session.setAttribute(SMSession.USER, user);
			// push the user into the servlet session as well
			// so we can write session history when the session collapses.
			session.getSession().setAttribute(SMSession.USER, user);
		}
		else
		{
			session.setAttribute(SMSession.USER, null);
			session.getSession().removeAttribute(SMSession.USER);
		}

	}

}
