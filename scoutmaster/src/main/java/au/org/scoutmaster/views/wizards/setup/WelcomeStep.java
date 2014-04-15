package au.org.scoutmaster.views.wizards.setup;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class WelcomeStep implements WizardStep
{

	public WelcomeStep(final GroupSetupWizardView setupWizardView)
	{
	}

	@Override
	public String getCaption()
	{
		return "Welcome to Scoutmaster";
	}

	@Override
	public Component getContent()
	{
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);

		final StringBuilder sb = new StringBuilder();
		sb.append("<h1>Welcome to Scoutmaster.</h1>");
		sb.append("<p>As the first user to login to Scoutmaster we need to take you through some first time configuration to get Scoutmaster up and running.</p>");
		sb.append("<p>Click the 'Next' button to begin</p>");

		final Label label = new Label(sb.toString());
		label.setContentMode(ContentMode.HTML);

		layout.addComponent(label);
		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		return true;
	}

	@Override
	public boolean onBack()
	{
		return false;
	}

}
