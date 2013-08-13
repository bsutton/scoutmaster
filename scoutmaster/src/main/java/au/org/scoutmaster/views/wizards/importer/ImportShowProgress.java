package au.org.scoutmaster.views.wizards.importer;

import java.io.File;

import org.apache.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.org.scoutmaster.domain.Importable;
import au.org.scoutmaster.util.ProgressBarWorker;
import au.org.scoutmaster.util.ProgressTaskListener;
import au.org.scoutmaster.views.ImportView;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ImportShowProgress implements WizardStep, ProgressTaskListener
{
	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(ImportShowProgress.class);
	private ImportView importView;
	private boolean importComplete = false;
	private ProgressBar indicator;
	private int count;
	private Label progressDescription;

	public ImportShowProgress(ImportView importView)
	{
		this.importView = importView;
	}

	@Override
	public String getCaption()
	{
		return "Importing data";
	}

	@Override
	public Component getContent()
	{
		this.count = 0;

		VerticalLayout layout = new VerticalLayout();

		progressDescription = new Label("Imported: " + this.count + " records");
		layout.addComponent(progressDescription);
		indicator = new ProgressBar(new Float(0.0));
		indicator.setIndeterminate(true);
		layout.addComponent(indicator);

		// Set polling frequency to 0.5 seconds.

		File tempFile = this.importView.getFile().getTempFile();
		Class<? extends Importable> clazz = importView.getType().getEntityClass();

		ProgressBarWorker worker = new ProgressBarWorker(new ImportTask(this, tempFile, clazz, this.importView.getMatch()
						.getFieldMap()));
		worker.start();

		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		return importComplete;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	@Override
	public void taskProgress(final int count, final int max)
	{
		UI.getCurrent().access(new Runnable()
		{
			@Override
			public void run()
			{
				progressDescription.setValue("Imported: " + count + " records.");
			}
		});

	}

	@Override
	public void taskComplete()
	{
		indicator.setValue(1.0f);
		indicator.setVisible(false);
		indicator.setEnabled(false);
		importComplete = true;
	}

}
