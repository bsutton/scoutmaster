package au.org.scoutmaster.help;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;

import au.org.scoutmaster.application.ScoutmasterViewEnum;

/**
 * View Provider, to replace the default one that Vaadin uses.
 *
 * The list of views is automatically retrieved from the VaadinPageEnum enum
 *
 * This one wraps the views in a HelpSplitPanel, so all our vaadin pages have a
 * help panel.
 *
 * @author rsutton
 *
 */
public class HelpWrappingViewProvider implements ViewProvider
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1300354248455962125L;
	private final Map<String, Class<? extends View>> views = new HashMap<String, Class<? extends View>>();

	private final Map<String, Class<? extends View>> viewsWithoutHelp = new HashMap<String, Class<? extends View>>();

	ScoutmasterViewEnum defaultView = null;

	Logger logger = LogManager.getLogger();

	/**
	 * The list of views is automatically retrieved from the VaadinPageEnum enum
	 */
	public HelpWrappingViewProvider()
	{
		for (final ScoutmasterViewEnum page : ScoutmasterViewEnum.values())
		{
			addView(page);
		}

	}

	@Override
	public String getViewName(String viewAndParameters)
	{
		this.logger.info(viewAndParameters);

		if (viewAndParameters.contains("&"))
		{
			viewAndParameters = viewAndParameters.substring(0, viewAndParameters.indexOf("&"));
		}

		if (viewAndParameters.contains("/"))
		{
			viewAndParameters = viewAndParameters.substring(0, viewAndParameters.indexOf("/"));
		}

		if (viewAndParameters.length() == 0)
		{
			viewAndParameters = this.defaultView.getTitle();
		}

		if (!this.views.containsKey(viewAndParameters) && !this.viewsWithoutHelp.containsKey(viewAndParameters))
		{
			this.logger.error("Couldn't match view " + viewAndParameters);
			return null;
		}
		return viewAndParameters;
	}

	@Override
	public View getView(final String viewName)
	{
		boolean noHelp = false;
		View helpPanel = null;
		try
		{
			Class<? extends View> viewClass = this.views.get(viewName);
			if (viewClass == null)
			{
				noHelp = true;
				viewClass = this.viewsWithoutHelp.get(viewName);
			}
			Preconditions.checkNotNull(viewClass, "Couldn't find the view " + viewName);

			helpPanel = viewClass.newInstance();
			if (!noHelp)
			{
				// wrap the view in a help panel
				helpPanel = new HelpSplitPanel(helpPanel);
			}

		}
		catch (final InstantiationException e)
		{
			this.logger.error(e, e);
		}
		catch (final IllegalAccessException e)
		{
			this.logger.error(e, e);
		}
		return helpPanel;
	}

	private void addView(final ScoutmasterViewEnum view)
	{
		if (view.noHelp())
		{
			this.viewsWithoutHelp.put(view.getTitle(), view.getViewClass());
		}
		else
		{
			this.views.put(view.getTitle(), view.getViewClass());
		}

		if (view.isDefaultView())
		{
			this.defaultView = view;
		}
	}

}
