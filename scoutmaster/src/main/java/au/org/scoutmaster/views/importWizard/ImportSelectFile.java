package au.org.scoutmaster.views.importWizard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.vaadin.teemu.wizards.WizardStep;

import au.org.scoutmaster.domain.ImportUserMapping;
import au.org.scoutmaster.views.ImportView;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.VerticalLayout;

public class ImportSelectFile implements WizardStep
{

	protected File tempFile;
	protected Table table;

	private boolean uploadComplete = false;
	protected String selectedFilename;
	private Upload upload;
	private ProgressBar progressBar;
	protected boolean uploadStarted;
	private VerticalLayout content;
	private ComboBox mapping;

	public ImportSelectFile(ImportView importView)
	{
		this.progressBar = new ProgressBar();
		this.progressBar.setCaption("Progress");
		this.progressBar.setWidth("50%");
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
		if (content == null)
		{
			content = new VerticalLayout();

			this.upload = new Upload("", new Upload.Receiver()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public OutputStream receiveUpload(String filename, String mimeType)
				{
					try
					{
						/*
						 * Here, we'll stored the uploaded file as a temporary
						 * file. No doubt there's a way to use a
						 * ByteArrayOutputStream, a reader around it, use
						 * ProgressListener (and a progress bar) and a separate
						 * reader thread to populate a container *during* the
						 * update.
						 * 
						 * This is quick and easy example, though.
						 */
						tempFile = File.createTempFile("temp", ".csv");
						return new FileOutputStream(tempFile);
					}
					catch (IOException e)
					{
						e.printStackTrace();
						return null;
					}
				}
			});

			upload.addStartedListener(new StartedListener()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void uploadStarted(StartedEvent event)
				{
					ImportSelectFile.this.uploadStarted = true;
					ImportSelectFile.this.uploadComplete = false;
				}
			});

			upload.addFailedListener(new FailedListener()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void uploadFailed(FailedEvent event)
				{
					Notification.show("The upload failed. " + event.getReason()
							+ " Please fix the problem and try again.");
					ImportSelectFile.this.uploadStarted = true;
					ImportSelectFile.this.uploadComplete = false;
				}
			});

			upload.addProgressListener(new ProgressListener()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void updateProgress(long readBytes, long contentLength)
				{
					ImportSelectFile.this.progressBar.setValue(((float) (readBytes / contentLength)));
					ImportSelectFile.this.progressBar.setVisible(true);
				}
			});

			upload.addFinishedListener(new Upload.FinishedListener()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void uploadFinished(Upload.FinishedEvent finishedEvent)
				{
					ImportSelectFile.this.selectedFilename = finishedEvent.getFilename();
					ImportSelectFile.this.uploadComplete = true;
					ImportSelectFile.this.progressBar.setValue(1.0f);
					ImportSelectFile.this.progressBar.setVisible(true);
					ImportSelectFile.this.content.addComponent(new Label("File " + ImportSelectFile.this.selectedFilename + " has been uploaded."));

					Notification.show("The upload has completed. Click 'Next'");
				}
			});

			content.addComponent(new Label(
					"Click the 'Browse...' button to select the file to import and then click the 'Upload' button."));

			content.addComponent(upload);

			content.addComponent(this.progressBar);
			this.progressBar.setValue(0.0f);
			this.progressBar.setVisible(false);
			JPAContainer<ImportUserMapping> userMappings = JPAContainerFactory.make(ImportUserMapping.class, "scoutmaster");
			// Display the set of import mappings
			HorizontalLayout row = new HorizontalLayout();
			FormLayout fl = new FormLayout();
			mapping = new ComboBox("Field Mappings", userMappings);
			mapping.setInputPrompt("--Please Select--");
			mapping.setNullSelectionItemId("--Please Select--");
			fl.addComponent(mapping);
			fl.addComponent(new Label("If you have imported this type of file previously you can select a saved field mapping."));
			row.addComponent(fl);
			//row.addComponent(new Label("If you have imported this type of file previously you can select a saved field mapping."));
			content.addComponent(row);
			content.setMargin(true);
		}

		return content;
	}

	@Override
	public boolean onAdvance()
	{
		if (!this.uploadStarted)
			Notification.show("Please select a file and then click 'Upload' to start the upload.");
		else if (!uploadComplete)
			Notification.show("Please wait for the upload to complete.");

		return this.uploadStarted && this.uploadComplete;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	public File getTempFile()
	{
		return tempFile;
	}

	public String getSelectedFilename()
	{
		return selectedFilename;
	}

	public String getImportMapping()
	{
		return (String) mapping.getValue();
	}

}
