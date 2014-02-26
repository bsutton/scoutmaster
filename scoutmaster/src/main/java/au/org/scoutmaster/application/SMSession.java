package au.org.scoutmaster.application;

import au.org.scoutmaster.domain.access.User;

import com.vaadin.ui.UI;

public enum SMSession
{
	INSTANCE;

	private static final String USER = "user";

	public User getLoggedInUser()
	{
		return (User)UI.getCurrent().getSession().getAttribute(USER);
	}

	public void setLoggedInUser(User user)
	{
		UI.getCurrent().getSession().setAttribute(USER, user);
		
	}
	
	

}
