package au.org.scoutmaster.help;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;

import au.com.vaadinutils.crud.CrudSecurityManager;
import au.com.vaadinutils.crud.security.SecurityManagerFactoryProxy;
import au.com.vaadinutils.help.HelpSplitPanel;
import au.org.scoutmaster.application.AccessDeniedView;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.application.ScoutmasterViewEnum;
import au.org.scoutmaster.views.ContactView;
import au.org.scoutmaster.views.LoginView;

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
	private Map<String, Class<? extends View>> views = new HashMap<String, Class<? extends View>>();

	private Map<String, Class<? extends View>> viewsWithoutHelp = new HashMap<String, Class<? extends View>>();

	transient Logger logger = LogManager.getLogger();

	String lastViewName = null;
	private ScoutmasterViewEnum defaultView;

	/**
	 * The list of views is automatically retrieved from the VaadinPageEnum enum
	 */
	public HelpWrappingViewProvider(String defaultViewName)
	{

		for (ScoutmasterViewEnum page : ScoutmasterViewEnum.values())
		{
			addView(page);
			if (page.getTitle().equals(defaultViewName))
			{
				this.defaultView = page;
			}

		}

	}

	@Override
	public String getViewName(String viewAndParameters)
	{
		logger.info(viewAndParameters);

		viewAndParameters = viewAndParameters.split("&")[0];
		viewAndParameters = viewAndParameters.split("/")[0];

		if (!views.containsKey(viewAndParameters) && !viewsWithoutHelp.containsKey(viewAndParameters))
		{
			logger.warn("Couldn't match view '" + viewAndParameters + "'");
			return defaultView.getTitle();
		}
		lastViewName = viewAndParameters;
		return viewAndParameters;
	}

	@Override
	public View getView(String viewName)
	{
		boolean noHelp = false;
		View helpPanel = null;
		Class<? extends View> viewClass = views.get(viewName);
		if (viewClass == null)
		{
			noHelp = true;
			viewClass = viewsWithoutHelp.get(viewName);
		}
		Preconditions.checkNotNull(viewClass, "Couldn't find the view " + viewName);

		CrudSecurityManager model = SecurityManagerFactoryProxy.getSecurityManager(viewClass);
		if (!model.canUserView())
		{
			return new AccessDeniedView(model.getFeatureName());
		}
		try
		{
			helpPanel = viewClass.newInstance();
		}
		catch (Throwable e)
		{
			logger.error(e, e);
			// If things go pair shaped try to redirect to somewhere
			// sensible.
			// Could cause a nasty recursion if we can't get to Login or
			// Contact.
			noHelp = true;
			if (SMSession.INSTANCE.getLoggedInUser() == null)
				helpPanel = getView(LoginView.NAME);
			else
				helpPanel = getView(ContactView.NAME);
		}

		if (!noHelp)
		{
			// wrap the view in a help panel
			helpPanel = new HelpSplitPanel(helpPanel);
		}

		return helpPanel;
	}

	private void addView(ScoutmasterViewEnum page2)
	{
		if (page2.noHelp())
		{
			viewsWithoutHelp.put(page2.getTitle(), page2.getViewClass());
		}
		else
		{
			views.put(page2.getTitle(), page2.getViewClass());
		}

	}

}
