package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class WelcomeStep implements WizardStep
{
	private VerticalLayout layout;

	public WelcomeStep(RaffleBookAllocationWizardView setupWizardView)
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
		if (layout == null)
		{
			layout = new VerticalLayout();
			layout.setMargin(true);

			StringBuilder sb = new StringBuilder();
			sb.append("<h1>Allocate Raffle books.</h1>");
			sb.append("<p>This wizard is designed to allow you to allocate books to Group Members (or any willing Contact).</p>");
			sb.append("<p>Before you attempt to Allocate Raffle books you must have first Imported the books using the Import Raffle Wizard</p>");
			sb.append("<p>The Allocation Wizard will find the first set of Unallocated books and allocate the desired count to the selected Contact.</p>");
			sb.append("<p>At the end of this wizard you will be able to print off an Allocation form for the member to sign, acknowledging the receipt of the books.</p>");
			Label label = new Label(sb.toString());
			label.setContentMode(ContentMode.HTML);
			layout.addComponent(label);

		}

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
