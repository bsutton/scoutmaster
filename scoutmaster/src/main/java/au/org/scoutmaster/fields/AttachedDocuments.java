package au.org.scoutmaster.fields;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.easyuploads.FileBuffer;
import org.vaadin.easyuploads.MultiFileUpload;

import au.com.vaadinutils.listener.ClickEventLogged;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.domain.Document;
import au.org.scoutmaster.util.SMNotification;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;

public class AttachedDocuments extends AbstractComponent
{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(AttachedDocuments.class);

	private static final String TEMP_FILE_DIR = new File(System.getProperty("java.io.tmpdir")).getPath();

	private final AbstractLayout uploadWidget;

	private final GridLayout documentLayout;

	private final ArrayList<AttachedDocument> attachedDocuments = new ArrayList<>();
	private List<Document> entitiesDocuments;

	public AttachedDocuments()
	{
		final HorizontalLayout layout = new HorizontalLayout();

		this.uploadWidget = buildUploadWidget();
		layout.addComponent(this.uploadWidget);
		layout.setComponentAlignment(this.uploadWidget, Alignment.MIDDLE_LEFT);
		this.documentLayout = buildDocumentList();
		layout.addComponent(this.documentLayout);
		layout.setComponentAlignment(this.documentLayout, Alignment.MIDDLE_RIGHT);
	}

	private GridLayout buildDocumentList()
	{
		final GridLayout layout = new GridLayout(1, 3);
		layout.setColumnExpandRatio(0, 1.0f);
		return layout;
	}

	public void setDocuments(final List<Document> documents)
	{
		this.entitiesDocuments = documents;
		for (final Document document : documents)
		{
			displayDocument(document, null);
		}
	}

	private void displayDocument(final Document document, final File file)
	{
		final int row = this.documentLayout.getRows() - 1;
		this.documentLayout.addComponent(new Label(document.getFilename()), row, 0);
		this.documentLayout.addComponent(new Label(document.getMimeType()), row, 0);
		final Button removeButton = new Button("x");
		removeButton.setStyleName("small");
		final AttachedDocument attached = new AttachedDocument(document, file, row);
		removeButton.setData(attached);

		removeButton.addClickListener(new ClickEventLogged.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void clicked(final ClickEvent event)
			{
				final AttachedDocument attachedDocument = (AttachedDocument) event.getButton().getData();
				attachedDocument.deleteFile();
				AttachedDocuments.this.attachedDocuments.remove(attachedDocument);
				final int row = attachedDocument.getRow();
				AttachedDocuments.this.documentLayout.removeRow(row);
				AttachedDocuments.this.entitiesDocuments.remove(attachedDocument.getDocument());
				// now we have to renumber all document rows
				for (final AttachedDocument document : AttachedDocuments.this.attachedDocuments)
				{
					if (document.getRow() > row)
					{
						document.setRow(document.getRow() - 1);
					}
				}

			}
		});

		this.documentLayout.addComponent(removeButton, this.documentLayout.getRows(), 1);
		this.attachedDocuments.add(attached);
	}

	private void attachDocument(final File file, final String mimeType) throws FileNotFoundException, IOException
	{

		final Document document = new Document();
		document.setFilename(file.getName());
		document.setMimeType(mimeType);
		document.setAddedBy(SMSession.INSTANCE.getLoggedInUser());

		// TODO: this could be very heavy weight.
		final byte[] data = IOUtils.toByteArray(new FileInputStream(file));
		document.setContent(data);
		this.entitiesDocuments.add(document);

		addDocument(document, file);
	}

	private void addDocument(final Document document, final File file)
	{
		displayDocument(document, file);
	}

	private AbstractLayout buildUploadWidget()
	{

		final MultiFileUpload multiFileUpload2 = new MultiFileUpload()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void handleFile(final File file, final String fileName, final String mimeType, final long length)
			{
				try
				{
					attachDocument(file, mimeType);
				}
				catch (final IOException e)
				{
					AttachedDocuments.logger.error(e, e);
					SMNotification.show(e, Type.ERROR_MESSAGE);
				}
			}

			@Override
			protected FileBuffer createReceiver()
			{
				final FileBuffer receiver = super.createReceiver();
				/*
				 * Make receiver not to delete files after they have been
				 * handled by #handleFile().
				 */
				receiver.setDeleteFiles(false);
				return receiver;
			}
		};
		multiFileUpload2.setCaption("Attach files");
		multiFileUpload2.setRootDirectory(AttachedDocuments.TEMP_FILE_DIR);
		return multiFileUpload2;
	}

	public class AttachedDocument
	{
		private final Document document;
		private final File file;
		private int row;

		public AttachedDocument(final Document document, final File file, final int row)
		{
			this.document = document;
			this.file = file;
			this.row = row;
		}

		public void setRow(final int row)
		{
			this.row = row;

		}

		public void deleteFile()
		{
			if (this.file != null)
			{
				this.file.delete();
			}
		}

		public Document getDocument()
		{
			return this.document;
		}

		public int getRow()
		{
			return this.row;
		}
	}

}
