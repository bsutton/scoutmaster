package au.org.scoutmaster.views.publicui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomLayout;

import au.com.vaadinutils.menu.Menu;

/** A start view for navigating to the main view */
@Menu(display = "Public View", path = "Public")
public class HomePageView extends CustomLayout implements View
{
	public static final String NAME = "PublicCalendar";

	private static final long serialVersionUID = 1L;

	public HomePageView()
	{

		super("home");

		/*
		 * Button start = new Button("Start"); this.addComponent(start,
		 * "start");
		 *
		 * start.addClickListener(e -> {
		 * UI.getCurrent().getNavigator().navigateTo(CutOptimizerUI.VIEWS.
		 * WIZARD_VIEW.name()); });
		 */
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		// TODO Auto-generated method stub

	}
}