package au.org.scoutmaster.help;

import java.util.Map;

import au.com.noojee.vaadin.conterters.ConverterFactory;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.ComponentContainer;
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
@Title("Noojee Admin")
public class NavigatorUI extends UI
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, String[]> params;

	/*
	 * After UI class is created, init() is executed. You should build and wire
	 * up your user interface here.
	 */
	protected void init(VaadinRequest request)
	{

		VaadinSession.getCurrent().setConverterFactory(new ConverterFactory());

		params = request.getParameterMap();

		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(false);
		mainLayout.setSpacing(true);
		mainLayout.setSizeFull();

		this.setContent(mainLayout);

		String target = "";
		String location = getPage().getLocation().toString();
		for (VaadinPageEnum page : VaadinPageEnum.values())
		{
			if (location.contains(page.getTitle()))
			{
				target = page.getTitle();
			}
		}

		// create our custom provider which will wrap all views in a helpSplitPannel
		HelpWrappingViewProvider provider = new HelpWrappingViewProvider();

		// create our customer Navigator, see comments in DemoNavigator as to why
		final Navigator navigator = new DemoNavigator(this, mainLayout, target);
		navigator.addProvider(provider);


		getNavigator().addViewChangeListener(new ViewChangeListener()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void afterViewChange(ViewChangeEvent event)
			{
				// For some reason the page title is set to null after each
				// navigation transition.
				getPage().setTitle("Noojee Admin");
			}

			@Override
			public boolean beforeViewChange(ViewChangeEvent event)
			{
				return true;
			}
		});


	}

	public class DemoNavigator extends Navigator
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * the purpose of this class is to prevent views being initialized twice
		 * @param ui
		 * @param container
		 * @param initialState
		 */
		public DemoNavigator(UI ui, ComponentContainer container, String initialState)
		{
			super(ui, container);
			getStateManager().setState(initialState);
			
			
		}
	}

	public Map<String, String[]> getParams()
	{
		return params;

	}

}
