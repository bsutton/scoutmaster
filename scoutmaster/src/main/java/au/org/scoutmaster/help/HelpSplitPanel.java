package au.org.scoutmaster.help;

import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class HelpSplitPanel extends HorizontalSplitPanel implements View, HelpPageListener
{

	/**
	 *
	 */
	private static final long serialVersionUID = 5011140025383708388L;
	@AutoGenerated
	private HorizontalLayout componentPane;
	private Panel helpPane;

	Logger logger = Logger.getLogger(HelpSplitPanel.class);

	HelpPageCache page = new HelpPageCache();

	View component;
	private VerticalLayout helpContainer;

	public HelpSplitPanel(final View component)
	{
		super();
		this.component = component;
		if (component instanceof HelpPageListener)
		{
			((HelpPageListener) component).setHelpPageListener(this);
		}

		setSplitPosition(75, Unit.PERCENTAGE, false);

		setImmediate(true);

		buildMainLayout();
		setSizeFull();
		((Component) component).setSizeFull();
		this.componentPane.addComponent((Component) component);
		setHelpPageId(((HelpProvider) component).getHelpId());

	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		// allow the View we are wrapping to process the enter event in the
		// normal way
		this.component.enter(event);
	}

	public void setHelpPageId(final HelpPageIdentifier helpId)
	{
		setHelp(helpId);

	}

	private void buildMainLayout()
	{

		this.componentPane = new HorizontalLayout();
		this.componentPane.setImmediate(false);
		this.componentPane.setMargin(false);
		this.componentPane.setSizeFull();

		super.addComponent(this.componentPane);

		this.helpContainer = new VerticalLayout();

		super.addComponent(this.helpContainer);

	}

	@Override
	public void removeAllComponents()
	{
		throw new RuntimeException("Cant remove components this way");
	}

	@Override
	public void removeComponent(final Component component)
	{
		throw new RuntimeException("Cant remove components this way");
	}

	@Override
	public void addComponent(final Component component)
	{
		throw new RuntimeException("Cant add components this way");
	}

	@Override
	public void setHelp(final HelpPageIdentifier helpId)
	{
		final Thread loadPage = new Thread(
				() -> {
					String helpSource = null;
					try
					{
						helpSource = HelpSplitPanel.this.page.lookupHelpPage(helpId);
					}
					catch (final ExecutionException e)
					{
						HelpSplitPanel.this.logger.error(e, e);
					}

					final String pageSource = helpSource;
					UI.getCurrent()
							.access(() -> {
								if (pageSource != null)
								{
									HelpSplitPanel.this.helpContainer.removeAllComponents();

									HelpSplitPanel.this.helpContainer.addComponent(new Label("Help page is 'Help-"
											+ helpId + "'"));

									final Label help = new Label(pageSource, ContentMode.HTML);
									HelpSplitPanel.this.helpContainer.addComponent(help);
								}
								else
								{
									final VerticalLayout help = new VerticalLayout();
									help.setSizeFull();

									final Link link = new Link("To create a help page click here",
											new ExternalResource(
													"https://github.com/bsutton/scoutmaster/wiki/ContextHelpIndex"),
											"wiki", 0, 0, BorderStyle.DEFAULT);

									help.addComponent(new Label(
											"Please create a help page in the wiki. The hyper link will take you to the help page guide in the wiki. You should create new page for "
													+ helpId + " in the wiki."));
									help.addComponent(link);
									help.addComponent(new Label("Help id is " + helpId));
									HelpSplitPanel.this.helpPane.setContent(help);
								}

							});

				});
		loadPage.start();

	}

	@Override
	public void setHelpPageListener(final HelpPageListener helpSplitPanel)
	{
		throw new RuntimeException("This is the top level HelpPageListener, you cant set the HelpPageListener");

	}

}
