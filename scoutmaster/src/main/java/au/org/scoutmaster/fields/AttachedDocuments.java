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

	private AbstractLayout uploadWidget;

	private GridLayout documentLayout;

	private ArrayList<AttachedDocument> attachedDocuments = new ArrayList<>();
	private List<Document> entitiesDocuments;
	
	public AttachedDocuments()
	{
		HorizontalLayout layout = new HorizontalLayout();
		
		uploadWidget = buildUploadWidget();
		layout.addComponent(uploadWidget);
		layout.setComponentAlignment(uploadWidget, Alignment.MIDDLE_LEFT);
		documentLayout = buildDocumentList();
		layout.addComponent(documentLayout);
		layout.setComponentAlignment(documentLayout, Alignment.MIDDLE_RIGHT);
	}
	
	
	private GridLayout buildDocumentList()
	{
		GridLayout layout = new GridLayout(1,3);
		layout.setColumnExpandRatio(0, 1.0f);
		return layout;
	}
	
	public void setDocuments(List<Document> documents)
	{
		this.entitiesDocuments = documents;
		for (Document document : documents)
		{
			displayDocument(document, null);
		}
	}


	private void displayDocument(Document document, File file)
	{
		int row = documentLayout.getRows() - 1;
		documentLayout.addComponent(new Label(document.getFilename()), row, 0);
		documentLayout.addComponent(new Label(document.getMimeType()), row, 0);
		Button removeButton = new Button("x");
		removeButton.setStyleName("small");
		AttachedDocument attached = new AttachedDocument(document, file, row);
		removeButton.setData(attached);

		removeButton.addClickListener(new ClickEventLogged.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void clicked(ClickEvent event)
			{
				AttachedDocument attachedDocument = (AttachedDocument) event.getButton().getData();
				attachedDocument.deleteFile();
				attachedDocuments.remove(attachedDocument.getDocument());
				int row = attachedDocument.getRow();
				documentLayout.removeRow(row);
				entitiesDocuments.remove(attachedDocument.getDocument());
				// now we have to renumber all document rows
				for (AttachedDocument document: attachedDocuments)
				{
					if (document.getRow() > row)
						document.setRow(document.getRow() - 1);
				}
				
			}
		});

		documentLayout.addComponent(removeButton, documentLayout.getRows(), 1);
		attachedDocuments.add(attached);
	}


	private void attachDocument(File file, String mimeType) throws FileNotFoundException, IOException
	{
		
		Document document = new Document();
		document.setFilename(file.getName());
		document.setMimeType(mimeType);
		document.setAddedBy(SMSession.INSTANCE.getLoggedInUser());
		
		// TODO: this could be very heavy weight.
		final byte[] data = IOUtils.toByteArray(new FileInputStream(file));
		document.setContent(data);
		entitiesDocuments.add(document);
		
		addDocument(document, file);
	}
	
	private void addDocument(Document document, File file)
	{
		displayDocument(document, file);
	}


	private AbstractLayout buildUploadWidget()
	{

		MultiFileUpload multiFileUpload2 = new MultiFileUpload()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void handleFile(File file, String fileName, String mimeType, long length)
			{
				try
				{
					attachDocument(file, mimeType);
				}
				catch (IOException  e)
				{
					logger.error(e,e);
					SMNotification.show(e, Type.ERROR_MESSAGE);
				}
			}

			@Override
			protected FileBuffer createReceiver()
			{
				FileBuffer receiver = super.createReceiver();
				/*
				 * Make receiver not to delete files after they have been
				 * handled by #handleFile().
				 */
				receiver.setDeleteFiles(false);
				return receiver;
			}
		};
		multiFileUpload2.setCaption("Attach files");
		multiFileUpload2.setRootDirectory(TEMP_FILE_DIR);
		return multiFileUpload2;
	}

	

	public class AttachedDocument
	{
		private Document document;
		private File file;
		private int row;

		public AttachedDocument(Document document, File file, int row)
		{
			this.document = document;
			this.file = file;
			this.row = row;
		}

		public void setRow(int row)
		{
			this.row = row;
			
		}

		public void deleteFile()
		{
			if (file != null)
				file.delete();
		}

		public Document getDocument()
		{
			return document;
		}
		
		public int getRow()
		{
			return row;
		}
	}

}
