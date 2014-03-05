package au.org.scoutmaster.views.wizards.raffle.importBooks;

import org.vaadin.teemu.wizards.WizardStep;

import au.org.scoutmaster.views.ContactView;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class FinalStep implements WizardStep
{


	public FinalStep(RaffleBookImportWizardView setupWizardView)
	{
	}

	@Override
	public String getCaption()
	{
		return "Setup complete";
	}

	@Override
	public Component getContent()
	{
	VerticalLayout layout = new VerticalLayout();
		
		StringBuilder sb = new StringBuilder();
		sb.append("<h1>Import Complete</h1><p>If you have more books to import simply run the import wizard again.</p>");
		
		Label label = new Label(sb.toString());
		label.setContentMode(ContentMode.HTML);
		
		layout.addComponent(label);
		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		UI.getCurrent().getNavigator().navigateTo(ContactView.NAME);
		return false;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

}
