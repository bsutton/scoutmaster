package au.org.scoutmaster.views.wizards.groupsetup;

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
		return "Welcome";
	}

	@Override
	public Component getContent()
	{
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);

		final StringBuilder sb = new StringBuilder();
		sb.append("<h1>Welcome to Scoutmaster.</h1>");
		sb.append(
				"<p>Scoutmaster will now walk you through the process of setting up your Scout or Girl Guides group.</p>");
		sb.append("<p>Answer a few questions and we will have you up and going in just a few minutes.</p>");
		sb.append("<p>Click the 'Next' button to begin.</p>");

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
