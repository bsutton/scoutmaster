package au.org.scoutmaster.website;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class HomePage extends VerticalLayout implements View
{

	private static final long serialVersionUID = 1L;

	@Override
	public void enter(ViewChangeEvent event)
	{

	}

	void buildLayout()
	{
		buildTitle();

	}

	private void buildTitle()
	{
		HorizontalLayout titleBand = new HorizontalLayout();

		titleBand.setHeight("120px");

		Resource res = new ThemeResource("images/scoutlogo.gif");
		Image image = new Image("Heidelberg Scouts", res);
		titleBand.addComponent(image);

		titleBand.addComponent(new Label("Heidelberg Scouts"));

		titleBand.addStyleName("title-banner");

	}
}
