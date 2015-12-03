package au.org.scoutmaster.views.wizards.importer;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.addon.tableexport.CsvExport;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.fields.PoJoTable;
import au.com.vaadinutils.listener.ClickEventLogged;
import au.com.vaadinutils.util.ProgressBarWorker;
import au.com.vaadinutils.util.ProgressTaskListener;
import au.org.scoutmaster.domain.Importable;
import au.org.scoutmaster.util.SMNotification;

public class ImportShowProgress implements WizardStep, ProgressTaskListener<ImportItemStatus>
{

	@SuppressWarnings("unused")
	static private Logger logger = LogManager.getLogger(ImportShowProgress.class);
	private final ImportWizardView importView;
	private PoJoTable<ImportItemStatus> progressTable;

	private boolean importComplete = false;
	private ProgressBar indicator;
	private int count;
	private Label progressDescription;

	public ImportShowProgress(final ImportWizardView importView)
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

		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		this.progressDescription = new Label("Imported: " + this.count + " records");
		layout.addComponent(this.progressDescription);

		this.indicator = new ProgressBar(new Float(0.0));
		this.indicator.setWidth("100%");
		layout.addComponent(this.indicator);

		final Label errorLabel = new Label("Errors are displayed below.");
		layout.addComponent(errorLabel);

		this.progressTable = new PoJoTable<>(ImportItemStatus.class, new String[]
		{ "Row", "Exception" });
		this.progressTable.setColumnExpandRatio("Exception", 1);
		this.progressTable.setSizeFull();
		layout.addComponent(this.progressTable);
		layout.setExpandRatio(this.progressTable, 1);
		final Button errorExport = new Button("Export Errors");
		layout.addComponent(errorExport);
		layout.setComponentAlignment(errorExport, Alignment.BOTTOM_RIGHT);
		errorExport.addClickListener(new ClickEventLogged.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void clicked(final ClickEvent event)
			{
				CsvExport csvExport;
				csvExport = new CsvExport(ImportShowProgress.this.progressTable);
				csvExport.setDisplayTotals(false);
				csvExport.setDoubleDataFormat("0");
				csvExport.excludeCollapsedColumns();
				csvExport.setReportTitle("Document title");
				csvExport.setExportFileName("Nome_file_example.csv");
				csvExport.export();
			}
		});

		// Set polling frequency to 0.5 seconds.

		final File tempFile = this.importView.getFile().getTempFile();
		final Class<? extends Importable> clazz = this.importView.getType().getEntityClass();

		ImportTask importTask = new ImportTask(this, tempFile, this.importView.getFile().getSelectedFile(), clazz,
				this.importView.getMatch().getFieldMap());

		final ProgressBarWorker<ImportItemStatus> worker = new ProgressBarWorker<>(importTask);
		worker.start();

		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		return this.importComplete;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	@Override
	public void taskProgress(final int count, final int max, final ImportItemStatus status)
	{
		UI ui = UI.getCurrent();
		ui.access(() -> {
			ImportShowProgress.this.progressDescription.setValue("Imported: " + count + " records.");
			if (status != null)
			{
				ImportShowProgress.this.progressTable.addRow(status);
			}
			ImportShowProgress.this.indicator.setValue((float) count / (float) max);
		});
	}

	@Override
	public void taskComplete(final int sent)
	{

		UI ui = UI.getCurrent();
		ui.access(() -> {
			ImportShowProgress.this.indicator.setValue(1.0f);
			ImportShowProgress.this.indicator.setVisible(false);
			ImportShowProgress.this.indicator.setEnabled(false);
			SMNotification.show("Import has completed.", Type.TRAY_NOTIFICATION);
			ImportShowProgress.this.progressDescription.setValue("Imported " + sent + " records. Import complete.");
		});

		this.importComplete = true;
	}

	@Override
	public void taskItemError(final ImportItemStatus status)
	{
		UI ui = UI.getCurrent();
		ui.access(() -> ImportShowProgress.this.progressTable.addRow(status));

	}

	@Override
	public void taskException(final Exception e)
	{
		SMNotification.show(e.getMessage(), Type.ERROR_MESSAGE);
	}

}
