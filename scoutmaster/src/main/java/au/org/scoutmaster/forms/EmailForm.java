package au.org.scoutmaster.forms;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.vaadin.easyuploads.FileBuffer;
import org.vaadin.easyuploads.MultiFileUpload;

import rx.Subscription;
import rx.util.functions.Action1;
import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.fields.CKEditorEmailField;
import au.com.vaadinutils.listener.ClickEventLogged;
import au.com.vaadinutils.listener.CompleteListener;
import au.com.vaadinutils.ui.WorkingDialog;
import au.org.scoutmaster.dao.ActivityDao;
import au.org.scoutmaster.dao.ActivityTypeDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMTPSettingsDao;
import au.org.scoutmaster.dao.SMTransaction;
import au.org.scoutmaster.domain.Activity;
import au.org.scoutmaster.domain.ActivityType;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.SMTPServerSettings;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.util.ButtonEventSource;
import au.org.scoutmaster.util.SMNotification;
import au.org.scoutmaster.views.wizards.bulkEmail.AttachedFile;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class EmailForm extends VerticalLayout implements com.vaadin.ui.Button.ClickListener, CompleteListener
{
	private static Logger logger = Logger.getLogger(EmailForm.class);
	private static final long serialVersionUID = 1L;
	private static final String TEMP_FILE_DIR = new File(System.getProperty("java.io.tmpdir")).getPath();

	private User sender;
	private TextField subject;
	private CKEditorEmailField ckEditor;
	private Window owner;
	private Button send;
	private Contact contact;
	private VerticalLayout attachedFiles;
	private HashSet<AttachedFile> fileList = new HashSet<>();

	private GridLayout grid;

	class TargetLine
	{
		ComboBox targetTypeCombo;
		TextField targetAddress;
		Button plusButton;
		private Subscription buttonSubscription;
		public int row;
	}

	ArrayList<TargetLine> lines = new ArrayList<>();

	/**
	 * 
	 * @param sender
	 *            the user who is sending this email.
	 */
	public EmailForm(Window owner, User sender, Contact contact, String toEmailAddress)
	{
		this.owner = owner;
		this.sender = sender;
		this.contact = contact;

		this.setSpacing(true);
		this.setMargin(true);
		this.setSizeFull();

		grid = new GridLayout(4, 2);
		grid.setWidth("100%");
		grid.setColumnExpandRatio(1, (float) 1.0);
		grid.setSpacing(true);

		TargetLine line = insertTargetLine(0);

		line.targetAddress.setValue(toEmailAddress);
		line.targetTypeCombo.select(EmailAddressType.To);

		send = new Button("Send", new ClickEventLogged.ClickAdaptor(this));
		send.setImmediate(true);
		
		grid.newLine();
		grid.addComponent(send);

		this.addComponent(grid);
		subject = new TextField("Subject");
		subject.setWidth("100%");

		this.addComponent(subject);
		ckEditor = new CKEditorEmailField(false);
		this.addComponent(ckEditor);
		this.setExpandRatio(ckEditor, 1.0f);

		if (sender.getEmailSignature() != null)
		{
			ckEditor.setValue("</br></br>" + sender.getEmailSignature());
		}

		HorizontalLayout uploadArea = new HorizontalLayout();
		AbstractLayout uploadWidget = addUploadWidget();
		uploadArea.addComponent(uploadWidget);
		attachedFiles = new VerticalLayout();
		Label attachedLabel = new Label("<b>Attached Files</b>");
		attachedLabel.setContentMode(ContentMode.HTML);
		attachedFiles.addComponent(attachedLabel);
		uploadArea.addComponent(attachedFiles);
		uploadArea.setWidth("100%");
		uploadArea.setComponentAlignment(uploadWidget, Alignment.TOP_LEFT);
		uploadArea.setComponentAlignment(attachedFiles, Alignment.TOP_RIGHT);

		this.addComponent(uploadArea);

		subject.focus();

	}

	List<EmailAddressType> getTargetTypes()
	{
		ArrayList<EmailAddressType> targetTypes = new ArrayList<>();

		targetTypes.add(EmailAddressType.To);
		targetTypes.add(EmailAddressType.CC);
		targetTypes.add(EmailAddressType.BCC);
		return targetTypes;
	}

	/**
	 * User click send button
	 */
	@Override
	public void buttonClick(ClickEvent event)
	{

		send.setEnabled(false);
		if (isEmpty(this.subject.getValue()))
			SMNotification.show("The subject may not be blank", Type.WARNING_MESSAGE);
		else if (!oneValidAddress())
			SMNotification.show("You must provide at least one email address", Type.WARNING_MESSAGE);
		else if (isEmpty(this.ckEditor.getValue()))
			SMNotification.show("The body of the email may not be blank", Type.WARNING_MESSAGE);
		else

		{
			WorkingDialog working = new WorkingDialog("Sending Email", "Sending...");
			UI.getCurrent().addWindow(working);
			working.setWorker(new Runnable()
			{

				@Override
				public void run()
				{
					EntityManager em = EntityManagerProvider.createEntityManager();
					try (SMTransaction t = new SMTransaction(em))
					{

						SMTPSettingsDao daoSMTPSettings = new DaoFactory(em).getSMTPSettingsDao();
						SMTPServerSettings settings = daoSMTPSettings.findSettings();

						ArrayList<SMTPSettingsDao.EmailTarget> targets = new ArrayList<>();
						
						for (TargetLine line : lines)
						{
							if (!isEmpty(line.targetAddress.getValue()))
								targets.add(new SMTPSettingsDao.EmailTarget((EmailAddressType) line.targetTypeCombo.getValue(), line.targetAddress.getValue()));
						}
						
						assert targets.size() != 0 : "Empty list of email targets"; 
						daoSMTPSettings.sendEmail(settings, EmailForm.this.sender.getEmailAddress(), targets,
								EmailForm.this.subject.getValue(), ckEditor.getValue(), fileList);

						// Log the activity
						ActivityDao daoActivity = new DaoFactory(em).getActivityDao();
						ActivityTypeDao daoActivityType = new DaoFactory(em).getActivityTypeDao();
						ActivityType type = daoActivityType.findByName(ActivityType.EMAIL);
						Activity activity = new Activity();
						activity.setAddedBy(EmailForm.this.sender);
						activity.setWithContact(EmailForm.this.contact);
						activity.setSubject(EmailForm.this.subject.getValue());
						activity.setDetails(EmailForm.this.ckEditor.getValue());
						activity.setType(type);

						daoActivity.persist(activity);
						t.commit();

					}
					catch (EmailException e)
					{
						logger.error(e, e);
						EmailForm.this.send.setEnabled(true);
						SMNotification.show(e, Type.ERROR_MESSAGE);
					}

				}
			}, this);

		}
		this.send.setEnabled(true);

	}

	private boolean oneValidAddress()
	{
		boolean found = false;
		for (TargetLine line : lines)
		{
			if (!isEmpty(line.targetAddress.getValue()))
			{
				found = true;
				break;
			}
		}
		return found;
	}

	private boolean isEmpty(String value)
	{
		return value == null || value.length() == 0;
	}

	@Override
	public void complete()
	{
		SMNotification.show("Message sent", Type.TRAY_NOTIFICATION);
		// working.close();
		this.owner.close();

	}

	private AbstractLayout addUploadWidget()
	{

		MultiFileUpload multiFileUpload2 = new MultiFileUpload()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void handleFile(File file, String fileName, String mimeType, long length)
			{
				attachFile(file);
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

	private void attachFile(File file)
	{
		HorizontalLayout line = new HorizontalLayout();
		line.setSpacing(true);
		Button removeButton = new Button("x");

		removeButton.setStyleName("small");

		line.addComponent(removeButton);
		line.addComponent(new Label(file.getName()));
		EmailForm.this.attachedFiles.addComponent(line);

		AttachedFile attachedFile = new AttachedFile(attachedFiles, file, line);
		this.fileList.add(attachedFile);
		removeButton.setData(attachedFile);

		removeButton.addClickListener(new ClickEventLogged.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void clicked(ClickEvent event)
			{
				AttachedFile file = (AttachedFile) event.getButton().getData();
				file.remove();
				EmailForm.this.fileList.remove(file);

			}
		});

	}

	class PlusAction implements Action1<ClickEvent>
	{
		@Override
		public void call(ClickEvent event)
		{
			Button button = event.getButton();
			TargetLine line = (TargetLine) button.getData();
			insertTargetLine(line.row + 1);
			// switch the button to a minus action as only the last line can be
			// used to add rows.
			button.setCaption("-");

			line.buttonSubscription.unsubscribe();

			Action1<ClickEvent> minusClickAction = new MinusAction();

			line.buttonSubscription = ButtonEventSource.fromActionOf(line.plusButton).subscribe(minusClickAction);
		}
	}

	class MinusAction implements Action1<ClickEvent>
	{
		@Override
		public void call(ClickEvent event)
		{
			Button button = event.getButton();
			TargetLine line = (TargetLine) button.getData();
			EmailForm.this.grid.removeRow(line.row);
			line.buttonSubscription.unsubscribe();
			lines.remove(line.row);
			
			// recalculate rows
			int row = 0;
			for (TargetLine aLine : lines)
			{
				aLine.row = row++;
			}
		}
	}

	private TargetLine insertTargetLine(int row)
	{
		List<EmailAddressType> targetTypes = getTargetTypes();

		EmailForm.this.grid.insertRow(row);
		grid.setCursorY(row);
		grid.setCursorX(0);

		TargetLine line = new TargetLine();
		line.row = row;
		line.targetTypeCombo = new ComboBox(null, targetTypes);
		line.targetTypeCombo.setWidth("60");
		line.targetTypeCombo.select(targetTypes.get(0));
		grid.addComponent(line.targetTypeCombo);
		line.targetAddress = new TextField();
		line.targetAddress.setInputPrompt("Enter email address");
		line.targetAddress.setWidth("100%");
		grid.addComponent(line.targetAddress);
		line.plusButton = new Button("+");
		line.plusButton.setData(line);
		line.plusButton.setStyleName(Reindeer.BUTTON_SMALL);
		grid.addComponent(line.plusButton);
		Action1<ClickEvent> plusClickAction = new PlusAction();

		line.buttonSubscription = ButtonEventSource.fromActionOf(line.plusButton).subscribe(plusClickAction);
		
		lines.add(line);

		return line;
	}

}
