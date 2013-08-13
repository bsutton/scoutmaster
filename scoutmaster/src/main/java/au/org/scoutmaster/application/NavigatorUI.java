package au.org.scoutmaster.application;

import java.io.Serializable;
import java.util.ArrayList;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.views.AppointmentView;
import au.org.scoutmaster.views.ClickATellView;
import au.org.scoutmaster.views.ContactView;
import au.org.scoutmaster.views.ForgottenPasswordView;
import au.org.scoutmaster.views.ImportView;
import au.org.scoutmaster.views.LoginView;
import au.org.scoutmaster.views.MessagingWizardView;
import au.org.scoutmaster.views.ResetPasswordView;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.MenuBar;
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
//@Theme("scoutmaster")
@Push
@Widgetset(value="au.org.scoutmaster.AppWidgetSet")
public class NavigatorUI extends UI
{
	private static final long serialVersionUID = 1L;

	private MenuBar menubar;

	private ArrayList<NavigatorUI.ViewMap> viewMap = new ArrayList<>();

	private VerticalLayout mainLayout;

	/*
	 * After UI class is created, init() is executed. You should build and wire
	 * up your user interface here.
	 */
	protected void init(VaadinRequest request)
	{
		VaadinSession.getCurrent().setConverterFactory(new ScoutmasterConverterFactory());
		styleConfirmDialog();

		viewMap.add(new ViewMap("", ContactView.class));
		viewMap.add(new ViewMap(ContactView.NAME, ContactView.class));
		viewMap.add(new ViewMap(AppointmentView.NAME, AppointmentView.class));
		viewMap.add(new ViewMap(ImportView.NAME, ImportView.class));
		viewMap.add(new ViewMap(LoginView.NAME, LoginView.class));
		viewMap.add(new ViewMap(ForgottenPasswordView.NAME, ForgottenPasswordView.class));
		viewMap.add(new ViewMap(ResetPasswordView.NAME, ResetPasswordView.class));
		viewMap.add(new ViewMap(MessagingWizardView.NAME, MessagingWizardView.class));
		viewMap.add(new ViewMap(ClickATellView.NAME, ClickATellView.class));

		// HACK:
		// hack: create a default admin account - MUST BE REMOVED once we have
		// db sorted.
		// HACK:
		if (User.findUser("bsutton@noojee.com.au") == null)
			User.addUser("bsutton@noojee.com.au", "password");

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
			navigator.addView(viewmap.viewName, viewmap.view);
		}

		mainLayout.addComponent(viewContainer);
		mainLayout.setExpandRatio(viewContainer, 1.0f);

		this.setContent(mainLayout);

		//
		// We use a view change handler to ensure the user is always redirected
		// to the login view if the user is not logged in.
		//
		getNavigator().addViewChangeListener(new ViewChangeListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean beforeViewChange(ViewChangeEvent event)
			{

				// Check if a user has logged in
				boolean isLoggedIn = getSession().getAttribute("user") != null;
				boolean isLoginView = event.getNewView() instanceof LoginView;
				boolean isForgottenPasswordView = event.getNewView() instanceof ForgottenPasswordView;

				if (!isLoggedIn && !isLoginView && !isForgottenPasswordView)
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
						NavigatorUI.this.menubar = new MenuBuilder(navigator, viewMap).build();
						NavigatorUI.this.menubar.setWidth("100%");
						mainLayout.addComponentAsFirst(menubar);
					}
				}

				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event)
			{

			}
		});

	}

	static class ViewMap implements Serializable
	{
		private static final long serialVersionUID = 1L;
		String viewName;
		Class<? extends View> view;

		ViewMap(String viewName, Class<? extends View> class1)
		{
			this.viewName = viewName;
			this.view = class1;
		}
	}

	void styleConfirmDialog()
	{
		ConfirmDialog.Factory df = new DefaultConfirmDialogFactory()
		{
			private static final long serialVersionUID = 1L;

			// We change the default order of the buttons
			@Override
			public ConfirmDialog create(String caption, String message, String okCaption, String cancelCaption)
			{
				ConfirmDialog d = super.create(caption, message, okCaption, cancelCaption);
				d.setStyleName("black");
				d.setModal(true);


				return d;
			}

		};
		ConfirmDialog.setFactory(df);

	}

}
