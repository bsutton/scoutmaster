package au.org.scoutmaster.application;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@Theme("mobiletheme")
@Widgetset("au.org.scoutmaster.AppWidgetSet")
@Title("Scoutmaster Mobile")
public class TouchNavigatorUI extends UI
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void init(final VaadinRequest request)
	{

		// Use it as the content root
		// setContent(new TouchView());
	}
}
