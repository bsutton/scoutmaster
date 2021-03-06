package au.org.scoutmaster.views.wizards.importer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.VerticalLayout;

import au.org.scoutmaster.util.SMNotification;

public class ImportSelectFile implements WizardStep
{

	protected File tempFile;
	protected Table table;

	private boolean uploadComplete = false;
	protected File selectedFilename;
	private Upload upload;
	private final ProgressBar progressBar;
	private final Label progress = new Label();
	protected boolean uploadStarted;
	private VerticalLayout content;
	// private ComboBox mapping;
	private Label completionMessage;

	public ImportSelectFile(final ImportWizardView importView)
	{
		this.progressBar = new ProgressBar();
		this.progressBar.setCaption("Progress");
		this.progressBar.setWidth("100%");
		this.progressBar.setHeight(100.0f, Unit.POINTS);

	}

	@Override
	public String getCaption()
	{
		return "Select CSV file";
	}

	@Override
	public Component getContent()
	{
		if (this.content == null)
		{
			this.content = new VerticalLayout();

			initUpload();

			this.upload.setImmediate(true);

			startListener();

			failedListener();

			progressListener();

			finishListener();

			this.content.addComponent(new Label("Click the 'Upload' button to select the file to import."));

			this.content.addComponent(this.upload);

			this.content.addComponent(this.progressBar);
			this.progressBar.setValue(0.0f);
			this.progressBar.setVisible(false);

			this.completionMessage = new Label("");
			this.content.addComponent(this.completionMessage);

			// JPAContainer<ImportUserMapping> userMappings =
			// JPAContainerFactory.make(ImportUserMapping.class,
			// "scoutmaster");
			// // Display the set of import mappings
			// HorizontalLayout row = new HorizontalLayout();
			// FormLayout fl = new FormLayout();
			// mapping = new ComboBox("Field Mappings", userMappings);
			// mapping.setInputPrompt("--Please Select--");
			// mapping.setNullSelectionItemId("--Please Select--");
			// fl.addComponent(mapping);
			// fl.addComponent(new
			// Label("If you have imported this type of file previously you can
			// select a saved field mapping."));
			// row.addComponent(fl);
			// //row.addComponent(new
			// Label("If you have imported this type of file previously you can
			// select a saved field mapping."));
			// content.addComponent(row);
			this.content.setMargin(true);
		}

		return this.content;
	}

	private void initUpload()
	{
		this.upload = new Upload("", new Upload.Receiver()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public OutputStream receiveUpload(final String filename, final String mimeType)
			{
				try
				{
					/*
					 * Here, we'll stored the uploaded file as a temporary file.
					 * No doubt there's a way to use a ByteArrayOutputStream, a
					 * reader around it, use ProgressListener (and a progress
					 * bar) and a separate reader thread to populate a container
					 * *during* the update.
					 *
					 * This is quick and easy example, though.
					 */
					ImportSelectFile.this.tempFile = File.createTempFile("temp", ".csv");
					return new FileOutputStream(ImportSelectFile.this.tempFile);
				}
				catch (final IOException e)
				{
					e.printStackTrace();
					return null;
				}
			}
		});
	}

	private void finishListener()
	{
		this.upload.addFinishedListener(new Upload.FinishedListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void uploadFinished(final Upload.FinishedEvent finishedEvent)
			{
				UI ui = UI.getCurrent();
				ui.access(() -> {
					ImportSelectFile.this.selectedFilename = new File(finishedEvent.getFilename());
					ImportSelectFile.this.uploadComplete = true;
					ImportSelectFile.this.progress.setVisible(false);
					ImportSelectFile.this.progressBar.setValue(1.0f);
					ImportSelectFile.this.progressBar.setVisible(true);

					ImportSelectFile.this.progressBar
							.setCaption("Completed uploading file: " + ImportSelectFile.this.selectedFilename.getName()
									+ " Total size: " + finishedEvent.getLength() + " bytes");
					ImportSelectFile.this.completionMessage.setValue(
							"The upload of File " + ImportSelectFile.this.selectedFilename + " has been completed.");

					SMNotification.show("The upload has completed. Click 'Next'", Type.TRAY_NOTIFICATION);
				});

			}
		});
	}

	private void progressListener()
	{
		this.upload.addProgressListener(new ProgressListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void updateProgress(final long readBytes, final long contentLength)
			{
				UI ui = UI.getCurrent();
				ui.access(() -> {
					ImportSelectFile.this.progressBar
							.setCaption("Uploaded: " + (float) readBytes / (float) contentLength * 100 + "%");
					ImportSelectFile.this.progressBar.setValue((float) readBytes / (float) contentLength);
					ImportSelectFile.this.progressBar.setVisible(true);
				});
			}
		});
	}

	private void failedListener()
	{
		this.upload.addFailedListener(new FailedListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void uploadFailed(final FailedEvent event)
			{
				UI ui = UI.getCurrent();
				ui.access(() -> {
					Notification
							.show("The upload failed. " + event.getReason() + " Please fix the problem and try again.");
					ImportSelectFile.this.uploadStarted = true;
					ImportSelectFile.this.uploadComplete = false;
				});
			}
		});
	}

	private void startListener()
	{
		this.upload.addStartedListener(new StartedListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void uploadStarted(final StartedEvent event)
			{
				ImportSelectFile.this.uploadStarted = true;
				ImportSelectFile.this.uploadComplete = false;
			}
		});
	}

	@Override
	public boolean onAdvance()
	{
		if (!this.uploadStarted)
		{
			Notification.show("Please select a file and then click 'Upload' to start the upload.");
		}
		else if (!this.uploadComplete)
		{
			Notification.show("Please wait for the upload to complete.");
		}

		return this.uploadStarted && this.uploadComplete;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	public File getTempFile()
	{
		return this.tempFile;
	}

	public File getSelectedFile()
	{
		return this.selectedFilename;
	}

	// public String getImportMapping()
	// {
	// return (String) mapping.getValue();
	// }

}
