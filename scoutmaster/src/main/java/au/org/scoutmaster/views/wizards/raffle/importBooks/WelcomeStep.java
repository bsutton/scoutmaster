package au.org.scoutmaster.views.wizards.raffle.importBooks;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class WelcomeStep implements WizardStep
{


	public WelcomeStep(RaffleBookImportWizardView setupWizardView)
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
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		StringBuilder sb = new StringBuilder();
		sb.append("<h1>Import Raffle books.</h1>");
		sb.append("<p>This wizard is designed to allow you to import a set of Raffle books at the start of a Raffle.</p>");
		sb.append("<p>If you have sets of books which have a contiguous number range (there are no number gaps between books)</p>");
		sb.append("<p>then you can import all books in a single batch. If all of the books are not in a contiguous range then</p>");
		sb.append("<p>you need to import each set of books that fall in a contigous range as separate batches.</p>");
		sb.append("<p>Once you have imported the books you can then allocate them to members using the Book Allocation wizard.</p>");
		
		Label label = new Label(sb.toString());
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