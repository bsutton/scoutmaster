package au.org.scoutmaster.views.wizards.importer;

import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ImportComplete implements WizardStep
{

	@SuppressWarnings("unused")
	private final ImportWizardView importView;

	public ImportComplete(final ImportWizardView importView, final Wizard wizard)
	{
		this.importView = importView;
	}

	@Override
	public String getCaption()
	{
		return "Import Complete";
	}

	@Override
	public Component getContent()
	{
		final VerticalLayout layout = new VerticalLayout();
		layout.addComponent(new Label("Import complete."));
		layout.setMargin(true);
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
