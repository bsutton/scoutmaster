package au.com.noojee.scouts;

import java.util.ArrayList;

import au.com.noojee.scouts.views.ContactView;
import au.com.noojee.scouts.views.ImportView;
import au.com.noojee.scouts.views.AppointmentView;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


/**
 * Check this out.
 * http://www.aceevo.com/reusable-patterns-for-vaadin-custom-form-layout/
 * 
 * @author bsutton
 *
 */
/* 
 * UI class is the starting point for your app. You may deploy it with VaadinServlet
 * or VaadinPortlet by giving your UI class name a parameter. When you browse to your
 * app a web page showing your UI is automatically generated. Or you may choose to 
 * embed your UI to an existing web page. 
 */
@Title("Scouts")
public class NavigatorUI extends UI
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Navigator navigator;

	final MenuBar menubar = new MenuBar();

	class ViewMap
	{
		String menuItemName;
		Class<? extends View> view;

		ViewMap(String menuItemName, Class<? extends View> class1)
		{
			this.menuItemName = menuItemName;
			this.view = class1;
		}
	}

	ArrayList<ViewMap> viewMap = new ArrayList<ViewMap>();

	/*
	 * After UI class is created, init() is executed. You should build and wire
	 * up your user interface here.
	 */
	protected void init(VaadinRequest request)
	{
		viewMap.add(new ViewMap("", ContactView.class));
		viewMap.add(new ViewMap("Contact", ContactView.class));
		viewMap.add(new ViewMap("Start", AppointmentView.class));
		viewMap.add(new ViewMap("Import", ImportView.class));

		VerticalLayout mainLayout = new VerticalLayout();

		mainLayout.addComponent(menubar);
		mainLayout.setMargin(false);
		mainLayout.setSpacing(true);
		mainLayout.setSizeFull();
		menubar.setWidth("100%");

		VerticalLayout viewContainer = new VerticalLayout();
		viewContainer.setHeight("100%");
		mainLayout.addComponent(viewContainer);
		mainLayout.setExpandRatio(viewContainer, 1.0f);
		
		this.setContent(mainLayout);

		final Navigator navigator = new Navigator(this, viewContainer);

		// Wire up the navigation
		for (final ViewMap viewmap : this.viewMap)
		{
			navigator.addView(viewmap.menuItemName, viewmap.view);
			if (viewmap.menuItemName != "")
				menubar.addItem(viewmap.menuItemName, new MenuBar.Command()
				{
					private static final long serialVersionUID = 1L;

					public void menuSelected(MenuItem selectedItem)
					{
						navigator.navigateTo(viewmap.menuItemName);
					}
				});
		}
		menubar.addItem("Mailings", null, null);
		menubar.addItem("Events", null, null);
		menubar.addItem("Reports", null, null);

		// navigator.addListener(new ViewChangeListener()
		// {
		// public void navigatorViewChange(View previous, View current)
		// {
		// getMainWindow().showNotification("Navigated to " +
		// current.getClass().getName());
		//
		// }
		// });
		//
		// return w;

	}

}
