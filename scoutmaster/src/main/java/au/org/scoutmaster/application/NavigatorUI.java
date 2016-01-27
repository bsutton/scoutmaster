package au.org.scoutmaster.application;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.errorHandling.ErrorSettingsFactory;
import au.com.vaadinutils.errorHandling.ErrorWindow;
import au.com.vaadinutils.help.HelpIndexFactory;
import au.com.vaadinutils.menu.MenuBuilder;
import au.com.vaadinutils.util.DeadlockFinder;
import au.org.scoutmaster.domain.converter.ScoutmasterConverterFactory;
import au.org.scoutmaster.help.HelpIndexImpl;
import au.org.scoutmaster.help.HelpWrappingViewProvider;
import au.org.scoutmaster.views.ContactView;
import au.org.scoutmaster.views.ForgottenPasswordView;
import au.org.scoutmaster.views.HomeView;
import au.org.scoutmaster.views.LoginView;
import au.org.scoutmaster.views.ResetPasswordView;
import au.org.scoutmaster.views.wizards.groupsetup.GroupSetupWizardView;

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
public class NavigatorUI extends UI
{
	private Logger logger = LogManager.getLogger();

	private static final long serialVersionUID = 1L;

	private MenuBar menubar;

	private VerticalLayout mainLayout;

	private View currentView;

	private String initalFrag;

	private boolean configured = false;

	/*
	 * After UI class is created, init() is executed. You should build and wire
	 * up your user interface here.
	 */
	@Override
	protected void init(final VaadinRequest request)
	{
		storeUrlFragment();

		if (!configured)
		{
			DeadlockFinder.SINGLETON.start();

			// SecurityFactoryImpl.defaultSecurityManager = new
			// AllowAllSecurityManager();

			ErrorSettingsFactory.setErrorSettings(new ErrorString(this));

			HelpIndexFactory.registerHelpIndex(new HelpIndexImpl());

			this.getReconnectDialogConfiguration().setDialogText(
					"You may have a problem with your Internet connection. Server connection lost, trying to reconnect...");

			configured = true;
		}

		new ErrorWindow();

		VaadinSession.getCurrent().setConverterFactory(new ScoutmasterConverterFactory());
		styleConfirmDialog();

		this.mainLayout = new VerticalLayout();
		this.mainLayout.setMargin(false);
		this.mainLayout.setSpacing(true);
		this.mainLayout.setSizeFull();

		final VerticalLayout viewContainer = new VerticalLayout();
		viewContainer.setHeight("100%");

		// final Navigator navigator = new Navigator(this, viewContainer);

		final Navigator navigator = new CustomNavigator(this, viewContainer, "");

		// navigator.addView("", ScoutmasterViewEnum.getDefaultView());

		// create our custom provider which will wrap all views in a
		// helpSplitPannel
		final HelpWrappingViewProvider provider = new HelpWrappingViewProvider(ContactView.NAME);
		navigator.addProvider(provider);

		//
		// // Wire up the navigation
		// for (final ViewMapping viewmap : this.viewMap)
		// {
		// navigator.addView(viewmap.getViewName(), viewmap.getView());
		// }

		this.mainLayout.addComponent(viewContainer);
		this.mainLayout.setExpandRatio(viewContainer, 1.0f);

		setContent(this.mainLayout);

		//
		// We use a view change handler to ensure the user is always redirected
		// to the login view if the user is not logged in.
		//
		getNavigator().addViewChangeListener(new ViewChangeListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean beforeViewChange(final ViewChangeEvent event)
			{

				// Check if a user has logged in
				final boolean isLoggedIn = getSession().getAttribute("user") != null;
				final boolean isLoginView = event.getNewView() instanceof LoginView;
				final boolean isHomeView = event.getNewView() instanceof HomeView;
				final boolean isForgottenPasswordView = event.getNewView() instanceof ForgottenPasswordView;
				final boolean isResetPasswordView = event.getNewView() instanceof ResetPasswordView;
				final boolean isSetupWizard = event.getNewView() instanceof GroupSetupWizardView;

				// TODO: should we cache this?
				// during dev its easier if we don't.

				if (isLoggedIn)
				{
					if (isLoginView || isForgottenPasswordView || isResetPasswordView)
					{
						// If someone tries to access to login related pages
						// whilst logged in. Reject them.
						// then cancel
						return false;
					}
					else
					{
						// check if the menu bar has been added and if not then
						// add
						// it.
						if (NavigatorUI.this.menubar == null)
						{
							NavigatorUI.this.menubar = new MenuBuilder(navigator, ScoutmasterViewEnum.getViewMap())
									.build();
							NavigatorUI.this.menubar.setWidth("100%");
							NavigatorUI.this.mainLayout.addComponentAsFirst(NavigatorUI.this.menubar);
						}

						// Reset the sizing as HomeView may have made it
						// undefined.
						NavigatorUI.this.mainLayout.setSizeFull();
						return true;
					}
				}
				else
				{
					// We arn't currently logged in.
					if (isHomeView)
					{
						// HomeView needs scroll bars so set size undefined.
						NavigatorUI.this.mainLayout.setSizeUndefined();
						return true;
					}
					else if (isLoginView || isForgottenPasswordView || isResetPasswordView || isSetupWizard)
					{
						// Reset the sizing as HomeView may have made it
						// undefined.
						NavigatorUI.this.mainLayout.setSizeFull();
						return true;
					}
					else
					{
						// not logged in, don't know where they are going to so
						// send them home.
						getNavigator().navigateTo(HomeView.NAME);
						return false;
					}
				}

			}

			@Override
			public void afterViewChange(final ViewChangeEvent event)
			{
				// For some reason the page title is set to null after each
				// navigation transition.
				getPage().setTitle("Scoutmaster");
				currentView = event.getNewView();
			}

		});

	}

	public class CustomNavigator extends Navigator
	{
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * the purpose of this class is to prevent views being initialized twice
		 *
		 * @param ui
		 * @param container
		 * @param initialState
		 */
		public CustomNavigator(UI ui, ComponentContainer container, String initialState)
		{
			super(ui, container);

			// get the fragment when the page is first loaded!!!!!
			if (initalFrag != null)
			{

				getStateManager().setState(initalFrag);
				initalFrag = null;

			}
			else
			{
				// prevent views being initialized twice
				getStateManager().setState(initialState);
			}

		}
	}

	void styleConfirmDialog()
	{
		final ConfirmDialog.Factory df = new DefaultConfirmDialogFactory()
		{
			private static final long serialVersionUID = 1L;

			// We change the default order of the buttons
			@Override
			public ConfirmDialog create(final String caption, final String message, final String okCaption,
					final String cancelCaption)
			{
				final ConfirmDialog d = super.create(caption, message, okCaption, cancelCaption);
				d.setStyleName("black");
				d.setModal(true);

				return d;
			}

		};
		ConfirmDialog.setFactory(df);

	}

	public static HashMap<String, String> extractParameterMap(final ViewChangeEvent event)
	{
		final HashMap<String, String> paramMap = new HashMap<>();

		final String parameters = event.getParameters();
		if (parameters.trim().length() > 0)
		{
			// split at "/", and look for key value pairs.
			final String[] params = parameters.split("/");
			for (final String param : params)
			{
				final String[] pair = param.split("=");
				if (pair.length != 2)
				{
					throw new IllegalArgumentException("The URI contained an invalid parameter (" + param
							+ ") which did not confirm to the required pattern of 'key=value'");
				}

				paramMap.put(pair[0], pair[1]);
			}
		}
		return paramMap;
	}

	public View getCurrentView()
	{
		return currentView;

	}

	private void storeUrlFragment()
	{
		initalFrag = getPage().getUriFragment();
		if (initalFrag != null)
		{
			if (initalFrag.startsWith("#"))
			{
				initalFrag = initalFrag.substring(1);
			}
			if (initalFrag.startsWith("!"))
			{
				initalFrag = initalFrag.substring(1);
			}
			if (initalFrag.equalsIgnoreCase(ScoutmasterViewEnum.Login.getTitle()))
			{
				initalFrag = "";
			}
		}
		logger.info("Setting initial URL Fragment to {}", initalFrag);
		UI.getCurrent().getSession().setAttribute(LoginView.LOGIN_TARGET, initalFrag);

	}

}
