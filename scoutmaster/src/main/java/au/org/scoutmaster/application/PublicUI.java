package au.org.scoutmaster.application;

import java.util.ArrayList;

import au.com.vaadinutils.menu.ViewMap;
import au.org.scoutmaster.views.calendar.PublicCalendarView;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
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
 * UI class is the starting point for your app. You may deploy it with
 * VaadinServlet or VaadinPortlet by giving your UI class name a parameter. When
 * you browse to your app a web page showing your UI is automatically generated.
 * Or you may choose to embed your UI to an existing web page.
 */
@Title("Scoutmaster")
// @PreserveOnRefresh
// @Theme("scoutmaster")
@Push
@Widgetset(value = "au.org.scoutmaster.AppWidgetSet")
public class PublicUI extends UI
{
	private static final long serialVersionUID = 1L;

	private ArrayList<ViewMap> viewMap = new ArrayList<>();

	private VerticalLayout mainLayout;

	/*
	 * After UI class is created, init() is executed. You should build and wire
	 * up your user interface here.
	 */
	protected void init(VaadinRequest request)
	{
//		VaadinSession.getCurrent().setConverterFactory(new ScoutmasterConverterFactory());
//		styleConfirmDialog();
//
//		SectionTypeDao daoSectionType = new DaoFactory().getSectionTypeDao();
//		daoSectionType.cacheSectionTypes();

		viewMap.add(new ViewMap("", PublicCalendarView.class));
		viewMap.add(new ViewMap(PublicCalendarView.NAME, PublicCalendarView.class));

		mainLayout = new VerticalLayout();
		mainLayout.setMargin(false);
		mainLayout.setSpacing(true);
		mainLayout.setSizeFull();

		VerticalLayout viewContainer = new VerticalLayout();
		viewContainer.setHeight("100%");

		final Navigator navigator = new Navigator(this, viewContainer);

		// Wire up the navigation
		for (final ViewMap viewmap : this.viewMap)
		{
			navigator.addView(viewmap.getViewName(), viewmap.getView());
		}

		mainLayout.addComponent(viewContainer);
		mainLayout.setExpandRatio(viewContainer, 1.0f);

		this.setContent(mainLayout);
	}
}
