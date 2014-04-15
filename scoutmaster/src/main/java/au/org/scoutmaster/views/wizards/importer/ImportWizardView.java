package au.org.scoutmaster.views.wizards.importer;

import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import au.com.vaadinutils.menu.Menu;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@Menu(display = "Import Contacts", path = "Members")
public class ImportWizardView extends VerticalLayout implements View, WizardProgressListener
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
		return this.type;
	}

	public ImportSelectFile getFile()
	{
		return this.file;
	}

	public ImportMatchFields getMatch()
	{
		return this.match;
	}

	public ImportShowSample getSample()
	{
		return this.sample;
	}

	public ImportShowProgress getProgress()
	{
		return this.progress;
	}

	public ImportComplete getComplete()
	{
		return this.complete;
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		this.type = new ImportSelectType(this);
		this.file = new ImportSelectFile(this);
		this.match = new ImportMatchFields(this);
		this.sample = new ImportShowSample(this);
		this.progress = new ImportShowProgress(this);
		this.complete = new ImportComplete(this, this.wizard);

		// create the Wizard component and add the steps
		this.wizard = new Wizard();
		this.wizard.setUriFragmentEnabled(true);
		this.wizard.setSizeFull();
		this.wizard.addListener(this);
		this.wizard.addStep(this.type, "type");
		this.wizard.addStep(this.file, "file");
		this.wizard.addStep(this.match, "match");
		this.wizard.addStep(this.sample, "sample");
		this.wizard.addStep(this.progress, "progress");
		this.wizard.addStep(this.complete, "complete");

		/* Main layout */
		this.setMargin(true);
		setSpacing(true);
		this.addComponent(this.wizard);
		setComponentAlignment(this.wizard, Alignment.TOP_CENTER);

		/* Main layout */
		setSizeFull();

	}

	@Override
	public void activeStepChanged(final WizardStepActivationEvent event)
	{
		// NOOP

	}

	@Override
	public void stepSetChanged(final WizardStepSetChangedEvent event)
	{
		Page.getCurrent().setTitle(event.getComponent().getCaption());

	}

	@Override
	public void wizardCompleted(final WizardCompletedEvent event)
	{
		endWizard("Import Completed!");

	}

	@Override
	public void wizardCancelled(final WizardCancelledEvent event)
	{
		endWizard("Import Cancelled!");

		if (this.file != null && this.file.getTempFile().exists())
		{
			this.file.getTempFile().delete();
		}

	}

	private void endWizard(final String message)
	{
		this.wizard.setVisible(false);
		Notification.show(message);
		Page.getCurrent().setTitle(message);
		Page.getCurrent().setLocation("");
	}

}
