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

	public HelpSplitPanel(View component)
	{
		super();
		this.component = component;
		if (component instanceof HelpPageListener)
		{
			((HelpPageListener)component).setHelpPageListener(this);
		}

		setSplitPosition(75, Unit.PERCENTAGE, false);

		setImmediate(true);

		buildMainLayout();
		setSizeFull();
		((Component)component).setSizeFull();
		componentPane.addComponent((Component)component);
		setHelpPageId(((HelpProvider)component).getHelpId());
		


	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		// allow the View we are wrapping to process the enter event in the normal way
		component.enter(event);
	} 
	
	public void setHelpPageId(HelpPageIdentifier helpId)
	{
		setHelp(helpId);

	}

	private void buildMainLayout()
	{

		componentPane = new HorizontalLayout();
		componentPane.setImmediate(false);
		componentPane.setMargin(false);
		componentPane.setSizeFull();

		super.addComponent(componentPane);

		helpContainer = new VerticalLayout();

		super.addComponent(helpContainer);

	}

	@Override
	public void removeAllComponents()
	{
		throw new RuntimeException("Cant remove components this way");
	}

	@Override
	public void removeComponent(Component component)
	{
		throw new RuntimeException("Cant remove components this way");
	}

	@Override
	public void addComponent(Component component)
	{
		throw new RuntimeException("Cant add components this way");
	}

	public void setHelp(HelpPageIdentifier helpId)
	{
	//	helpPane.removeAllComponents();

		String pageSource = null;
		try
		{
			pageSource = page.lookupHelpPage(helpId);
		}
		catch (ExecutionException e)
		{
			logger.error(e,e);
		}

		if (pageSource != null)
		{
			helpContainer.removeAllComponents();
			
			helpContainer.addComponent(new Label("Help page is 'Help-" + helpId + "'"));
	
//			helpPane = new Panel();
//			helpPane.setImmediate(false);
//			helpPane.setSizeFull();
//
			Label help = new Label(pageSource, ContentMode.HTML);
//
//			helpPane.setContent(help);
			//helpPane.setExpandRatio(help, 1.0f);
			
				//helpPane.setContent(helpBody);
			helpContainer.addComponent(help);
		}
		else
		{
			VerticalLayout help = new VerticalLayout();
			help.setSizeFull();

			Link link = new Link("To create a help page click here", new ExternalResource(
					"https://github.com/bsutton/scoutmaster/wiki/ContextHelpIndex"), "wiki", 0, 0,
					BorderStyle.DEFAULT);

			help.addComponent(new Label(
					"Please create a help page in the wiki. The hyper link will take you to the help page guide in the wiki. You should create new page for "
							+ helpId + " in the wiki."));
			help.addComponent(link);
			help.addComponent(new Label("Help id is " + helpId));
			//helpPane.addComponent(help);
			helpPane.setContent(help);
		}
	}

	@Override
	public void setHelpPageListener(HelpPageListener helpSplitPanel)
	{
		throw new RuntimeException("This is the top level HelpPageListener, you cant set the HelpPageListener");
		
	}

	

}
