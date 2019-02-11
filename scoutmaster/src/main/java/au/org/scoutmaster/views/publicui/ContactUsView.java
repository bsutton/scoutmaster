package au.org.scoutmaster.views.publicui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomLayout;

import au.com.vaadinutils.menu.Menu;

/** A start view for navigating to the main view */
@Menu(display = "Scouts", path = "Public")
public class ContactUsView extends CustomLayout implements View
{
	public static final String NAME = "Scouts";

	private static final long serialVersionUID = 1L;

	public ContactUsView()
	{
		super("scouts");
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		// TODO Auto-generated method stub

	}
}