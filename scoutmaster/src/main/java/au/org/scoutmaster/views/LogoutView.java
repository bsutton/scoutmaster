package au.org.scoutmaster.views;

import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.application.SMSession;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@Menu(display = "Logout")
public class LogoutView extends CustomComponent implements View
{
	private static final long serialVersionUID = 1L;

	public static final String NAME = "logout";

	public LogoutView()
	{
		setSizeFull();

		final VerticalLayout fields = new VerticalLayout();
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, true));
		fields.setSizeUndefined();

		// Add both to a panel
		Label label = new Label("<H1>Thankyou for using Scoutmaster.</H1>");
		label.setContentMode(ContentMode.HTML);

		fields.addComponent(label);

		label = new Label("Click <a href='#Login'>here to login again.</a>");
		label.setContentMode(ContentMode.HTML);
		fields.addComponent(label);

		// The view root layout
		final VerticalLayout viewLayout = new VerticalLayout(fields);
		viewLayout.setSizeFull();
		viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
		viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
		setCompositionRoot(viewLayout);
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		SMSession.INSTANCE.setLoggedInUser(null);
		getUI().getSession().close();
		getUI().getSession().getSession().invalidate();
		getUI().getPage().setLocation("/scoutmaster/");
	}
}