package au.org.scoutmaster.application;

import java.util.ArrayList;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.menu.ViewMapping;
import au.org.scoutmaster.views.calendar.PublicCalendarView;

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
@Theme("scoutmaster")
@Push // (transport = Transport.LONG_POLLING)
@Widgetset(value = "au.org.scoutmaster.AppWidgetSet")
public class PublicUI extends UI
{
	private static final long serialVersionUID = 1L;

	private final ArrayList<ViewMapping> viewMap = new ArrayList<>();

	private VerticalLayout mainLayout;

	/*
	 * After UI class is created, init() is executed. You should build and wire
	 * up your user interface here.
	 */
	@Override
	protected void init(final VaadinRequest request)
	{
		// VaadinSession.getCurrent().setConverterFactory(new
		// ScoutmasterConverterFactory());
		// styleConfirmDialog();
		//
		// SectionTypeDao daoSectionType = new DaoFactory().getSectionTypeDao();
		// daoSectionType.cacheSectionTypes();

		this.viewMap.add(new ViewMapping("", PublicCalendarView.class));
		this.viewMap.add(new ViewMapping(PublicCalendarView.NAME, PublicCalendarView.class));

		this.mainLayout = new VerticalLayout();
		this.mainLayout.setMargin(false);
		this.mainLayout.setSpacing(true);
		this.mainLayout.setSizeFull();

		final VerticalLayout viewContainer = new VerticalLayout();
		viewContainer.setHeight("100%");

		final Navigator navigator = new Navigator(this, viewContainer);

		// Wire up the navigation
		for (final ViewMapping viewmap : this.viewMap)
		{
			navigator.addView(viewmap.getViewName(), viewmap.getView());
		}

		this.mainLayout.addComponent(viewContainer);
		this.mainLayout.setExpandRatio(viewContainer, 1.0f);

		setContent(this.mainLayout);
	}
}
