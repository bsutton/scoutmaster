package au.org.scoutmaster.views.wizards.importer;

import java.io.File;

import org.apache.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.fields.PoJoTable;
import au.com.vaadinutils.listener.ClickEventLogged;
import au.com.vaadinutils.ui.UIUpdater;
import au.org.scoutmaster.domain.Importable;
import au.org.scoutmaster.util.SMNotification;
import au.org.vaadinutil.util.ProgressBarWorker;
import au.org.vaadinutil.util.ProgressTaskListener;

import com.vaadin.addon.tableexport.CsvExport;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;

public class ImportShowProgress implements WizardStep, ProgressTaskListener<ImportItemStatus>
{

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(ImportShowProgress.class);
	private ImportWizardView importView;
	private PoJoTable<ImportItemStatus> progressTable;

	private boolean importComplete = false;
	private ProgressBar indicator;
	private int count;
	private Label progressDescription;

	public ImportShowProgress(ImportWizardView importView)
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
		layout.setSizeFull();

		progressDescription = new Label("Imported: " + this.count + " records");
		layout.addComponent(progressDescription);
		
		indicator = new ProgressBar(new Float(0.0));
		indicator.setWidth("100%");
		layout.addComponent(indicator);


		Label errorLabel = new Label("Errors are displayed below.");
		layout.addComponent(errorLabel);

		progressTable = new PoJoTable<>(ImportItemStatus.class, new String[]
		{ "Row", "Exception" });
		progressTable.setColumnExpandRatio("Exception", 1);
		progressTable.setSizeFull();
		layout.addComponent(this.progressTable);
		layout.setExpandRatio(progressTable, 1);
		Button errorExport = new Button("Export Errors");
		layout.addComponent(errorExport);
		layout.setComponentAlignment(errorExport, Alignment.BOTTOM_RIGHT);
		errorExport.addClickListener(new ClickEventLogged.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void clicked(ClickEvent event)
			{
				CsvExport csvExport;
		        csvExport = new CsvExport(progressTable);
		        csvExport.setDisplayTotals(false);
		        csvExport.setDoubleDataFormat("0");
		        csvExport.excludeCollapsedColumns();
		        csvExport.setReportTitle("Document title");
		        csvExport.setExportFileName("Nome_file_example.csv");
		        csvExport.export();
			}
		});
		


		// Set polling frequency to 0.5 seconds.

		File tempFile = this.importView.getFile().getTempFile();
		Class<? extends Importable> clazz = importView.getType().getEntityClass();

		ProgressBarWorker<ImportItemStatus> worker = new ProgressBarWorker<>(new ImportTask(this, tempFile, clazz,
				this.importView.getMatch().getFieldMap()));
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
	public void taskProgress(final int count, final int max, final ImportItemStatus status)
	{
		new UIUpdater(new Runnable()
		{
			@Override
			public void run()
			{
				progressDescription.setValue("Imported: " + count + " records.");
				if (status != null)
					ImportShowProgress.this.progressTable.addRow(status);
				indicator.setValue(((float)count)/((float)max));
			}

		});
	}

	@Override
	public void taskComplete(final int sent)
	{

		new UIUpdater(new Runnable()
		{
			@Override
			public void run()
			{
				indicator.setValue(1.0f);
				indicator.setVisible(false);
				indicator.setEnabled(false);
				SMNotification.show("Import has completed.", Type.TRAY_NOTIFICATION);
				progressDescription.setValue("Imported " + sent + " records. Import complete.");
			}
		});

		importComplete = true;
	}

	@Override
	public void taskItemError(final ImportItemStatus status)
	{
		new UIUpdater(new Runnable()
		{
			@Override
			public void run()
			{

				ImportShowProgress.this.progressTable.addRow(status);
			}
		});

	}

	@Override
	public void taskException(final Exception e)
	{
		SMNotification.show(e.getMessage(), Type.ERROR_MESSAGE);
	}

}
