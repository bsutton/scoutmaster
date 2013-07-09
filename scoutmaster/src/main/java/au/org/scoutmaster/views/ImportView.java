package au.org.scoutmaster.views;

import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import au.org.scoutmaster.views.importWizard.ImportComplete;
import au.org.scoutmaster.views.importWizard.ImportMatchFields;
import au.org.scoutmaster.views.importWizard.ImportSelectFile;
import au.org.scoutmaster.views.importWizard.ImportSelectType;
import au.org.scoutmaster.views.importWizard.ImportShowProgress;
import au.org.scoutmaster.views.importWizard.ImportShowSample;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@Menu(display="Import")
public class ImportView extends VerticalLayout implements View, WizardProgressListener
{
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "Import";

	private Wizard wizard;
	
	private ImportSelectType type;
	private ImportSelectFile file;
	private ImportMatchFields match;
	private ImportShowSample sample;
	private ImportShowProgress progress;
	private ImportComplete complete;



	public ImportSelectType getType()
	{
		return type;
	}


	public ImportSelectFile getFile()
	{
		return file;
	}


	public ImportMatchFields getMatch()
	{
		return match;
	}


	public ImportShowSample getSample()
	{
		return sample;
	}


	public ImportShowProgress getProgress()
	{
		return progress;
	}


	public ImportComplete getComplete()
	{
		return complete;
	}


	@Override
	public void enter(ViewChangeEvent event)
	{

		type = new ImportSelectType(this);
		file = new ImportSelectFile(this);
		match = new ImportMatchFields(this);
		sample = new ImportShowSample(this);
		progress = new ImportShowProgress(this);
		complete = new ImportComplete(this, wizard);


		// create the Wizard component and add the steps
		wizard = new Wizard();
		wizard.setUriFragmentEnabled(true);
		wizard.addListener(this);
		wizard.addStep(type, "type");
		wizard.addStep(file, "file");
		wizard.addStep(match, "match");
		wizard.addStep(sample, "sample");
		wizard.addStep(progress, "progress");
		wizard.addStep(complete, "complete");
		wizard.setHeight("600px");
		wizard.setWidth("800px");
		wizard.setUriFragmentEnabled(true);
		
		/* Main layout */
		this.setMargin(true);
		this.setSpacing(true);
		this.addComponent(wizard);
		this.setComponentAlignment(wizard, Alignment.TOP_CENTER);

	}

	
	@Override
	public void activeStepChanged(WizardStepActivationEvent event)
	{
		// NOOP

	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent event)
	{
		Page.getCurrent().setTitle(event.getComponent().getCaption());

	}

	@Override
	public void wizardCompleted(WizardCompletedEvent event)
	{
		this.endWizard("Import Completed!");

	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event)
	{
		this.endWizard("Import Cancelled!");
		
		if (this.file.getTempFile().exists())
			this.file.getTempFile().delete();

	}

	private void endWizard(String message)
	{
		wizard.setVisible(false);
		Notification.show(message);
		Page.getCurrent().setTitle(message);
		Page.getCurrent().setLocation("");
	}

}
