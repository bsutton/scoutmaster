package au.org.scoutmaster.views;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import au.com.vaadinutils.listener.ClickEventLogged;
import au.org.scoutmaster.help.HelpPageCache;
import au.org.scoutmaster.help.HelpPageIdentifier;
import au.org.scoutmaster.views.wizards.groupsetup.GroupSetupWizardView;

/**
 * This is the Home page of scoutmaster.
 *
 * The Home page is a public page which is used to provide some marketing
 * information on scoutmaster as well as a link to the 'login' and 'new account'
 * pages.
 *
 * @author bsutton
 *
 */
public class HomeView extends CustomComponent implements View, Button.ClickListener
{
	private static Logger logger = LogManager.getLogger();
	private static final long serialVersionUID = 1L;

	public static final String NAME = "Home";

	private Button signingButton;

	private Button freeAccount;
	private ArrayList<Label> labels = new ArrayList<>();

	public HomeView()
	{
		Panel panelForScrollbars = new Panel();
		panelForScrollbars.setImmediate(true);
		// panelForScrollbars.setSizeFull();

		UI.getCurrent().setResizeLazy(true);
		Page.getCurrent().addBrowserWindowResizeListener(event -> {
			setSizes(panelForScrollbars, event.getWidth(), event.getHeight());

		});
		// .panelForScrollbars.setWidthUndefined();
		// panelForScrollbars.setWidth("100%");
		// panelForScrollbars.setWidth("600px");
		// setSizeFull();

		// The view root layout
		final VerticalLayout viewLayout = new VerticalLayout();
		viewLayout.setMargin(new MarginInfo(true, true, true, true));
		viewLayout.setWidth("100%");
		// viewLayout.setWidthUndefined();

		addLogo(viewLayout);

		HorizontalLayout columns = new HorizontalLayout();

		addFirstColumn(columns);

		addSecondColumn(columns);

		viewLayout.addComponent(columns);

		panelForScrollbars.setContent(viewLayout);
		setCompositionRoot(panelForScrollbars);

		setSizes(panelForScrollbars, Page.getCurrent().getBrowserWindowWidth(),
				Page.getCurrent().getBrowserWindowHeight());
	}

	private void setSizes(Panel panelForScrollbars, int width, int height)
	{
		panelForScrollbars.setWidth("" + width);
		panelForScrollbars.setHeight("" + height);

		for (Label label : labels)
		{
			label.setWidth((width / 2) - 30, Unit.PIXELS);
		}
	}

	private void addSecondColumn(final HorizontalLayout viewLayout)
	{
		// 2nd row of information panes
		VerticalLayout secondColumn = new VerticalLayout();
		secondColumn.setWidth("100%");
		// secondRow.setWidthUndefined();
		// secondRow.setSizeFull();
		addFirstRightBlurb(secondColumn);
		addSecondRightBlurb(secondColumn);
		viewLayout.addComponent(secondColumn);
	}

	private void addFirstColumn(final HorizontalLayout viewLayout)
	{
		// First row of information panes
		final VerticalLayout firstColumn = new VerticalLayout();
		firstColumn.setWidth("100%");

		addFirstLeftBlurb(firstColumn);
		addSecondLeftBlurb(firstColumn);

		viewLayout.addComponent(firstColumn);
	}

	private void addLogo(final VerticalLayout viewLayout)
	{
		final VerticalLayout layout = new VerticalLayout();
		HorizontalLayout buttons = getLoginAccountButtons();
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth("100%");
		buttonLayout.addComponent(buttons);
		buttonLayout.setComponentAlignment(buttons, Alignment.TOP_RIGHT);

		layout.addComponent(buttonLayout);

		// So the logo
		final HorizontalLayout logoLayout = new HorizontalLayout();
		logoLayout.setWidth("100%");

		final String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		final FileResource resource = new FileResource(new File(basepath + "/images/scoutmaster-logo.png"));

		final Image image = new Image(null, resource);
		image.setAlternateText("Scoutmaster Logo");
		logoLayout.addComponent(image);

		layout.addComponent(logoLayout);
		layout.setComponentAlignment(logoLayout, Alignment.TOP_LEFT);

		viewLayout.addComponent(layout);

	}

	private void addSecondRightBlurb(VerticalLayout column)
	{
		loadBlurb(column, HelpPageIdentifier.HomeSecondRight);

	}

	private void addSecondLeftBlurb(VerticalLayout column)
	{
		loadBlurb(column, HelpPageIdentifier.HomeSecondLeft);

	}

	private void addFirstRightBlurb(VerticalLayout column)
	{
		loadBlurb(column, HelpPageIdentifier.HomeTopRight);

	}

	private HorizontalLayout getLoginAccountButtons()
	{
		// Buttons
		final HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);

		// Create login button
		this.signingButton = new Button("Login", new ClickEventLogged.ClickAdaptor(this));
		this.signingButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		buttons.addComponent(this.signingButton);
		buttons.setComponentAlignment(this.signingButton, Alignment.MIDDLE_LEFT);

		this.freeAccount = new Button("Free Account", new ClickEventLogged.ClickAdaptor(this));
		buttons.addComponent(this.freeAccount);
		buttons.setComponentAlignment(this.freeAccount, Alignment.MIDDLE_RIGHT);

		return buttons;
	}

	private void addFirstLeftBlurb(VerticalLayout column)
	{
		loadBlurb(column, HelpPageIdentifier.HomeTopLeft);

	}

	private void loadBlurb(VerticalLayout column, HelpPageIdentifier helpId)
	{
		Label content;
		try
		{
			VerticalLayout blurb = new VerticalLayout();
			content = new Label(HelpPageCache.lookupHelpPage(helpId));
			content.setContentMode(ContentMode.HTML);
			// content.setWidth("40%");
			this.labels.add(content);
			blurb.addComponent(content);
			column.addComponent(blurb);
			// row.setE
		}
		catch (ExecutionException e)
		{
			logger.error(e, e);
		}
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		// Use this to set focus on entry.
	}

	@Override
	public void buttonClick(final ClickEvent event)
	{

		if (event.getButton() == this.freeAccount)
		{
			UI.getCurrent().getNavigator().navigateTo(GroupSetupWizardView.NAME);
		}
		else
		{
			UI.getCurrent().getNavigator().navigateTo(LoginView.NAME);
		}
	}

}