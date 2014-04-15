package au.org.scoutmaster.application;

import java.util.HashMap;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import au.com.vaadinutils.menu.MenuBuilder;
import au.com.vaadinutils.util.DeadlockFinder;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SectionTypeDao;
import au.org.scoutmaster.dao.access.UserDao;
import au.org.scoutmaster.domain.converter.ScoutmasterConverterFactory;
import au.org.scoutmaster.help.HelpWrappingViewProvider;
import au.org.scoutmaster.views.ForgottenPasswordView;
import au.org.scoutmaster.views.LoginView;
import au.org.scoutmaster.views.ResetPasswordView;
import au.org.scoutmaster.views.wizards.setup.GroupSetupWizardView;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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
public class NavigatorUI extends UI
{
	private static final long serialVersionUID = 1L;

	private MenuBar menubar;

	private VerticalLayout mainLayout;

	/*
	 * After UI class is created, init() is executed. You should build and wire
	 * up your user interface here.
	 */
	@Override
	protected void init(final VaadinRequest request)
	{

		DeadlockFinder.SINGLETON.start();

		VaadinSession.getCurrent().setConverterFactory(new ScoutmasterConverterFactory());
		styleConfirmDialog();

		final SectionTypeDao daoSectionType = new DaoFactory().getSectionTypeDao();
		daoSectionType.cacheSectionTypes();

		this.mainLayout = new VerticalLayout();
		this.mainLayout.setMargin(false);
		this.mainLayout.setSpacing(true);
		this.mainLayout.setSizeFull();

		final VerticalLayout viewContainer = new VerticalLayout();
		viewContainer.setHeight("100%");

		final Navigator navigator = new Navigator(this, viewContainer);
		navigator.addView("", ScoutmasterViewEnum.getDefaultView());

		// create our custom provider which will wrap all views in a
		// helpSplitPannel
		final HelpWrappingViewProvider provider = new HelpWrappingViewProvider();
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
				final boolean isForgottenPasswordView = event.getNewView() instanceof ForgottenPasswordView;
				final boolean isResetPasswordView = event.getNewView() instanceof ResetPasswordView;

				// TODO: should we cache this?
				// during dev its easier if we don't.
				final UserDao daoUser = new DaoFactory().getUserDao();
				final long userCount = daoUser.getCount();

				if (userCount == 0)
				{
					// Deal with recursion. When we navigateTo the setupwizard
					// it comes back through
					// beforeViewChange so we have to check that aren't aready
					// on our way to the
					// setupview.
					if (event.getNewView() instanceof GroupSetupWizardView)
					{
						return true;
					}
					else
					{
						// Must be a first time login so lets go and run the
						// setup wizard.
						getNavigator().navigateTo(GroupSetupWizardView.NAME);
						return false;
					}
				}

				if (!isLoggedIn && !isLoginView && !isForgottenPasswordView && !isResetPasswordView)
				{
					// Redirect to login view always if a user has not yet
					// logged in
					getNavigator().navigateTo(LoginView.NAME);
					return false;

				}
				else if (isForgottenPasswordView)
				{
					return true;
				}
				else if (isLoggedIn && isLoginView)
				{
					// If someone tries to access to login view while logged in,
					// then cancel
					return false;
				}
				else if (isLoggedIn)
				{
					// check if the menu bar has been added and if not then add
					// it.
					if (NavigatorUI.this.menubar == null)
					{
						NavigatorUI.this.menubar = new MenuBuilder(navigator, ScoutmasterViewEnum.getViewMap()).build();
						NavigatorUI.this.menubar.setWidth("100%");
						NavigatorUI.this.mainLayout.addComponentAsFirst(NavigatorUI.this.menubar);
					}
				}
				return true;
			}

			@Override
			public void afterViewChange(final ViewChangeEvent event)
			{
				// For some reason the page title is set to null after each
				// navigation transition.
				getPage().setTitle("Scoutmaster");
			}

		});

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

}
