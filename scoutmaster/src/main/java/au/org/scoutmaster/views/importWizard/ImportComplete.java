package au.org.scoutmaster.views.importWizard;

import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;

import au.org.scoutmaster.views.ImportView;

import com.vaadin.ui.Component;

public class ImportComplete implements WizardStep
{

	@SuppressWarnings("unused")
	private ImportView importView;

	public ImportComplete(ImportView importView, Wizard wizard)
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onAdvance()
	{
		return false;
	}

	@Override
	public boolean onBack()
	{
		return false;
	}

}
