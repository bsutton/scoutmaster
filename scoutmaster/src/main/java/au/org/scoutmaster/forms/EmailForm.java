package au.org.scoutmaster.forms;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.mail.EmailException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.easyuploads.FileBuffer;
import org.vaadin.easyuploads.MultiFileUpload;

import rx.Subscription;
import rx.util.functions.Action1;
import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.fields.CKEditorEmailField;
import au.com.vaadinutils.listener.ClickEventLogged;
import au.com.vaadinutils.listener.CompleteListener;
import au.com.vaadinutils.ui.WorkingDialog;
import au.com.vaadinutils.validator.EmailValidator;
import au.org.scoutmaster.dao.ActivityDao;
import au.org.scoutmaster.dao.ActivityTypeDao;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMTPSettingsDao;
import au.org.scoutmaster.dao.Transaction;
import au.org.scoutmaster.domain.Activity;
import au.org.scoutmaster.domain.ActivityType;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.SMTPServerSettings;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.util.ButtonEventSource;
import au.org.scoutmaster.util.SMNotification;
import au.org.scoutmaster.views.wizards.bulkEmail.AttachedFile;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractSelect.NewItemHandler;
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

public class EmailForm extends VerticalLayout
{
	private static Logger logger = LogManager.getLogger(EmailForm.class);
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
		ComboBox targetAddress;
		Button minusButton;
		private Subscription buttonSubscription;
		public int row;
	}

	ArrayList<TargetLine> lines = new ArrayList<>();
	private ComboBox primaryTypeCombo;
	private TextField primaryTargetAddress;
	private Button primaryPlusButton;

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

		List<EmailAddressType> targetTypes = getTargetTypes();

		primaryTypeCombo = new ComboBox(null, targetTypes);
		primaryTypeCombo.setWidth("60");
		primaryTypeCombo.select(targetTypes.get(0));
		primaryTypeCombo.select(EmailAddressType.To);
		grid.addComponent(primaryTypeCombo);

		primaryTargetAddress = new TextField(null, toEmailAddress);
		primaryTargetAddress.setWidth("100%");
		//primaryTargetAddress.setReadOnly(true);
		primaryTargetAddress.addValidator(new EmailValidator("Please enter a valid email address."));
		primaryTargetAddress.setImmediate(true);
		grid.addComponent(primaryTargetAddress);

		primaryPlusButton = new Button("+");
		primaryPlusButton.setDescription("Click to add another email address line.");
		primaryPlusButton.setStyleName(Reindeer.BUTTON_SMALL);
		grid.addComponent(primaryPlusButton);
		Action1<ClickEvent> plusClickAction = new PlusClickAction();
		ButtonEventSource.fromActionOf(primaryPlusButton).subscribe(plusClickAction);

		send = new Button("Send");
		send.setDescription("Click to send this email.");
		Action1<ClickEvent> sendClickAction = new SendClickAction();
		ButtonEventSource.fromActionOf(send).subscribe(sendClickAction);

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

	private boolean checkValidAddresses()
	{
		boolean valid = true;
		for (TargetLine line : lines)
		{
			if (!line.targetAddress.isValid())
			{
				valid = false;
				break;
			}
		}
		return valid;
	}

	private boolean isEmpty(String value)
	{
		return value == null || value.length() == 0;
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

	private TargetLine insertTargetLine(int row)
	{
		List<EmailAddressType> targetTypes = getTargetTypes();

		EmailForm.this.grid.insertRow(row);
		grid.setCursorY(row);
		grid.setCursorX(0);

		final TargetLine line = new TargetLine();
		line.row = row;

		line.targetTypeCombo = new ComboBox(null, targetTypes);
		line.targetTypeCombo.setWidth("60");
		line.targetTypeCombo.select(targetTypes.get(0));
		grid.addComponent(line.targetTypeCombo);

		line.targetAddress = new ComboBox(null);
		grid.addComponent(line.targetAddress);
		line.targetAddress.setImmediate(true);
		line.targetAddress.setTextInputAllowed(true);
		line.targetAddress.setInputPrompt("Enter Contact Name or email address");
		line.targetAddress.setWidth("100%");
		line.targetAddress.addValidator(new EmailValidator("Please enter a valid email address."));

		line.targetAddress.setContainerDataSource(getValidEmailContacts());
		line.targetAddress.setItemCaptionPropertyId("namedemail");
		line.targetAddress.setNewItemsAllowed(true);

		line.targetAddress.setNewItemHandler(new NewItemHandler()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void addNewItem(String newItemCaption)
			{
				IndexedContainer container = (IndexedContainer) line.targetAddress.getContainerDataSource();

				Item item = addItem(container, "", newItemCaption);
				if (item != null)
				{
					line.targetAddress.addItem(item.getItemProperty("id").getValue());
					line.targetAddress.setValue(item.getItemProperty("id").getValue());
				}
			}
		});

		line.minusButton = new Button("-");
		line.minusButton.setDescription("Click to remove this email address line.");
		line.minusButton.setData(line);
		line.minusButton.setStyleName(Reindeer.BUTTON_SMALL);
		grid.addComponent(line.minusButton);
		Action1<ClickEvent> minusClickAction = new MinusClickAction();

		line.buttonSubscription = ButtonEventSource.fromActionOf(line.minusButton).subscribe(minusClickAction);

		lines.add(line);

		return line;
	}

	private IndexedContainer getValidEmailContacts()
	{
		IndexedContainer container = new IndexedContainer();

		ContactDao daoContact = new DaoFactory().getContactDao();
		List<Contact> list = daoContact.findByHasEmail();

		container.addContainerProperty("id", String.class, null);
		container.addContainerProperty("email", String.class, null);
		container.addContainerProperty("namedemail", String.class, null);

		for (Contact contact : list)
		{
			String named = contact.getFirstname() + " " + contact.getLastname();
			if (contact.getHomeEmail().trim().length() != 0)
			{
				addItem(container, named, contact.getHomeEmail());
			}
			if (contact.getWorkEmail().trim().length() != 0)
			{
				addItem(container, named, contact.getWorkEmail());
			}
		}
		return container;
	}

	@SuppressWarnings("unchecked")
	private Item addItem(IndexedContainer container, String named, String email)
	{
		// When we are editing an email (as second time) we can end up with
		// double brackets so we strip them off here.
		if (email.startsWith("<"))
			email = email.substring(1);
		if (email.endsWith(">"))
			email = email.substring(0, email.length() - 1);

		Item item = container.addItem(email);
		if (item != null)
		{
			item.getItemProperty("id").setValue(email);
			item.getItemProperty("email").setValue(email);
			String namedEmail;
			if (named != null && named.trim().length() > 0)
				namedEmail = named + " <" + email + ">";
			else
				namedEmail = "<" + email + ">";
			item.getItemProperty("namedemail").setValue(namedEmail);
		}
		return item;
	}

	class PlusClickAction implements Action1<ClickEvent>
	{
		@Override
		public void call(ClickEvent event)
		{
			TargetLine newLine = insertTargetLine(EmailForm.this.lines.size() + 1);
			newLine.targetAddress.focus();
		}
	}

	class MinusClickAction implements Action1<ClickEvent>
	{
		@Override
		public void call(ClickEvent event)
		{
			Button button = event.getButton();
			TargetLine line = (TargetLine) button.getData();
			EmailForm.this.grid.removeRow(line.row);
			line.buttonSubscription.unsubscribe();
			lines.remove(line.row - 1);

			// recalculate rows
			int row = 1;
			for (TargetLine aLine : lines)
			{
				aLine.row = row++;
			}
		}
	}

	class SendClickAction implements Action1<ClickEvent>, CompleteListener
	{
		@Override
		public void call(ClickEvent t1)
		{
			send.setEnabled(false);
			if (isEmpty(EmailForm.this.subject.getValue()))
				SMNotification.show("The subject may not be blank", Type.WARNING_MESSAGE);
			else if (!checkValidAddresses())
				SMNotification.show("All Email adddresses must be valid", Type.WARNING_MESSAGE);
			else if (isEmpty(EmailForm.this.ckEditor.getValue()))
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
						try (Transaction t = new Transaction(em))
						{
							SMTPSettingsDao daoSMTPSettings = new DaoFactory(em).getSMTPSettingsDao();
							SMTPServerSettings settings = daoSMTPSettings.findSettings();

							ArrayList<SMTPSettingsDao.EmailTarget> targets = new ArrayList<>();

							
							// First add in the primary address.
							if (!isEmpty((String) primaryTargetAddress.getValue()))
								targets.add(new SMTPSettingsDao.EmailTarget((EmailAddressType) primaryTypeCombo
										.getValue(), (String) primaryTargetAddress.getValue()));

							for (TargetLine line : lines)
							{
								if (!isEmpty((String) line.targetAddress.getValue()))
									targets.add(new SMTPSettingsDao.EmailTarget((EmailAddressType) line.targetTypeCombo
											.getValue(), (String) line.targetAddress.getValue()));
							}

							assert targets.size() != 0 : "Empty list of email targets";
							daoSMTPSettings.sendEmail(settings, EmailForm.this.sender.getEmailAddress(), targets,
									EmailForm.this.subject.getValue(), ckEditor.getValue(), fileList);

//							em.detach(EmailForm.this.sender);
//							em.detach(EmailForm.this.contact);
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
			EmailForm.this.send.setEnabled(true);

		}

		@Override
		public void complete()
		{
			SMNotification.show("Message sent", Type.TRAY_NOTIFICATION);
			EmailForm.this.owner.close();
		}

	}

}
