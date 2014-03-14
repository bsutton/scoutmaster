package au.org.scoutmaster.application;

import au.org.scoutmaster.domain.access.User;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

public enum SMSession
{
	INSTANCE;

	static final String USER = "user";

	public User getLoggedInUser()
	{
		return (User) UI.getCurrent().getSession().getAttribute(USER);
	}

	public void setLoggedInUser(User user)
	{
		VaadinSession session = UI.getCurrent().getSession();

		if (user != null)
		{
			session.setAttribute(USER, user);
			// push the user into the servlet session as well
			// so we can write session history when the session collapses.
			session.getSession().setAttribute(USER, user);
		}
		else
		{
			session.setAttribute(USER, null);
			session.getSession().removeAttribute(USER);
		}

	}

}
