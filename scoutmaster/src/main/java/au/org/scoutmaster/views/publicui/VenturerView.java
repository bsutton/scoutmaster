package au.org.scoutmaster.views.publicui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomLayout;

import au.com.vaadinutils.menu.Menu;

/** A start view for navigating to the main view */
@Menu(display = "Venturer", path = "Public")
public class VenturerView extends CustomLayout implements View
{
	public static final String NAME = "Venturer";

	private static final long serialVersionUID = 1L;

	public VenturerView()
	{
		super("Venterer");
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		// TODO Auto-generated method stub

	}
}