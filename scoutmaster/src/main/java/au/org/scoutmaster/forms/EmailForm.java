package au.org.scoutmaster.forms;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.activation.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.easyuploads.FileBuffer;
import org.vaadin.easyuploads.MultiFileUpload;

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

import au.com.vaadinutils.dao.EntityManagerRunnable;
import au.com.vaadinutils.fields.CKEditorEmailField;
import au.com.vaadinutils.listener.ClickEventLogged;
import au.com.vaadinutils.listener.CompleteListener;
import au.com.vaadinutils.ui.WorkingDialog;
import au.com.vaadinutils.validator.EmailValidator;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.util.ButtonEventSource;
import au.org.scoutmaster.util.SMNotification;
import au.org.scoutmaster.views.wizards.bulkEmail.AttachedFileLayout;
import rx.Subscription;
import rx.util.functions.Action1;

public class EmailForm extends VerticalLayout
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(EmailForm.class);
	private static final long serialVersionUID = 1L;
	private static final String TEMP_FILE_DIR = new File(System.getProperty("java.io.tmpdir")).getPath();

	private final User sender;
	private final TextField subject;
	private final CKEditorEmailField ckEditor;
	private final Window owner;
	private final Button send;
	private final Contact contact;
	private final VerticalLayout attachedFiles;
	private final HashSet<AttachedFileLayout> fileList = new HashSet<>();

	private final GridLayout grid;

	class TargetLine
	{
		ComboBox targetTypeCombo;
		ComboBox targetAddress;
		Button minusButton;
		private Subscription buttonSubscription;
		public int row;
	}

	ArrayList<TargetLine> lines = new ArrayList<>();
	private final ComboBox primaryTypeCombo;
	private final TextField primaryTargetAddress;
	private final Button primaryPlusButton;

	/**
	 *
	 * @param sender
	 *            the user who is sending this email.
	 */
	public EmailForm(final Window owner, final User sender, final Contact contact, final String toEmailAddress)
	{
		this.owner = owner;
		this.sender = sender;
		this.contact = contact;

		setSpacing(true);
		this.setMargin(true);
		setSizeFull();

		this.grid = new GridLayout(4, 2);
		this.grid.setWidth("100%");
		this.grid.setColumnExpandRatio(1, (float) 1.0);
		this.grid.setSpacing(true);

		final List<EmailAddressType> targetTypes = getTargetTypes();

		this.primaryTypeCombo = new ComboBox(null, targetTypes);
		this.getPrimaryTypeCombo().setWidth("100");
		this.getPrimaryTypeCombo().select(targetTypes.get(0));
		this.getPrimaryTypeCombo().select(EmailAddressType.To);
		this.grid.addComponent(this.getPrimaryTypeCombo());

		this.primaryTargetAddress = new TextField(null, toEmailAddress);
		this.getPrimaryTargetAddress().setWidth("100%");
		// primaryTargetAddress.setReadOnly(true);
		this.getPrimaryTargetAddress().addValidator(new EmailValidator("Please enter a valid email address."));
		this.getPrimaryTargetAddress().setImmediate(true);
		this.grid.addComponent(this.getPrimaryTargetAddress());

		this.primaryPlusButton = new Button("+");
		this.primaryPlusButton.setDescription("Click to add another email address line.");
		this.primaryPlusButton.setStyleName(Reindeer.BUTTON_SMALL);
		this.grid.addComponent(this.primaryPlusButton);
		final Action1<ClickEvent> plusClickAction = new PlusClickAction();
		ButtonEventSource.fromActionOf(this.primaryPlusButton).subscribe(plusClickAction);

		this.send = new Button("Send");
		this.getSend().setDescription("Click to send this email.");
		final Action1<ClickEvent> sendClickAction = new SendClickAction();
		ButtonEventSource.fromActionOf(this.getSend()).subscribe(sendClickAction);

		this.getSend().setImmediate(true);

		this.grid.newLine();
		this.grid.addComponent(this.getSend());

		this.addComponent(this.grid);
		this.subject = new TextField("Subject");
		this.getSubject().setWidth("100%");

		this.addComponent(this.getSubject());
		this.ckEditor = new CKEditorEmailField(false);
		this.addComponent(this.getCkEditor());
		setExpandRatio(this.getCkEditor(), 1.0f);

		if (sender.getEmailSignature() != null)
		{
			this.getCkEditor().setValue("</br></br>" + sender.getEmailSignature());
		}

		final HorizontalLayout uploadArea = new HorizontalLayout();
		final AbstractLayout uploadWidget = addUploadWidget();
		uploadArea.addComponent(uploadWidget);
		this.attachedFiles = new VerticalLayout();
		final Label attachedLabel = new Label("<b>Attached Files</b>");
		attachedLabel.setContentMode(ContentMode.HTML);
		this.attachedFiles.addComponent(attachedLabel);
		uploadArea.addComponent(this.attachedFiles);
		uploadArea.setWidth("100%");
		uploadArea.setComponentAlignment(uploadWidget, Alignment.TOP_LEFT);
		uploadArea.setComponentAlignment(this.attachedFiles, Alignment.TOP_RIGHT);

		this.addComponent(uploadArea);

		this.getSubject().focus();

	}

	List<EmailAddressType> getTargetTypes()
	{
		final ArrayList<EmailAddressType> targetTypes = new ArrayList<>();

		targetTypes.add(EmailAddressType.To);
		targetTypes.add(EmailAddressType.CC);
		targetTypes.add(EmailAddressType.BCC);
		return targetTypes;
	}

	private boolean checkValidAddresses()
	{
		boolean valid = true;
		for (final TargetLine line : this.lines)
		{
			if (!line.targetAddress.isValid())
			{
				valid = false;
				break;
			}
		}
		return valid;
	}

	private boolean isEmpty(final String value)
	{
		return value == null || value.length() == 0;
	}

	private AbstractLayout addUploadWidget()
	{

		final MultiFileUpload multiFileUpload2 = new MultiFileUpload()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void handleFile(final File file, final String fileName, final String mimeType, final long length)
			{
				attachFile(file);
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
		multiFileUpload2.setRootDirectory(EmailForm.TEMP_FILE_DIR);
		return multiFileUpload2;
	}

	private void attachFile(final File file)
	{
		final HorizontalLayout line = new HorizontalLayout();
		line.setSpacing(true);
		final Button removeButton = new Button("x");

		removeButton.setStyleName("small");

		line.addComponent(removeButton);
		line.addComponent(new Label(file.getName()));
		EmailForm.this.attachedFiles.addComponent(line);

		final AttachedFileLayout attachedFile = new AttachedFileLayout(this.attachedFiles, file, line);
		this.fileList.add(attachedFile);
		removeButton.setData(attachedFile);

		removeButton.addClickListener(new ClickEventLogged.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void clicked(final ClickEvent event)
			{
				final AttachedFileLayout file = (AttachedFileLayout) event.getButton().getData();
				file.remove();
				EmailForm.this.fileList.remove(file);

			}
		});

	}

	private TargetLine insertTargetLine(final int row)
	{
		final List<EmailAddressType> targetTypes = getTargetTypes();

		EmailForm.this.grid.insertRow(row);
		this.grid.setCursorY(row);
		this.grid.setCursorX(0);

		final TargetLine line = new TargetLine();
		line.row = row;

		line.targetTypeCombo = new ComboBox(null, targetTypes);
		line.targetTypeCombo.setWidth("100");
		line.targetTypeCombo.select(targetTypes.get(0));
		this.grid.addComponent(line.targetTypeCombo);

		line.targetAddress = new ComboBox(null);
		this.grid.addComponent(line.targetAddress);
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
			public void addNewItem(final String newItemCaption)
			{
				final IndexedContainer container = (IndexedContainer) line.targetAddress.getContainerDataSource();

				final Item item = addItem(container, "", newItemCaption);
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
		this.grid.addComponent(line.minusButton);
		final Action1<ClickEvent> minusClickAction = new MinusClickAction();

		line.buttonSubscription = ButtonEventSource.fromActionOf(line.minusButton).subscribe(minusClickAction);

		this.lines.add(line);

		return line;
	}

	private IndexedContainer getValidEmailContacts()
	{
		final IndexedContainer container = new IndexedContainer();

		final ContactDao daoContact = new DaoFactory().getContactDao();
		final List<Contact> list = daoContact.findByHasEmail();

		container.addContainerProperty("id", String.class, null);
		container.addContainerProperty("email", String.class, null);
		container.addContainerProperty("namedemail", String.class, null);

		for (final Contact contact : list)
		{
			final String named = contact.getFirstname() + " " + contact.getLastname();
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
	private Item addItem(final IndexedContainer container, final String named, String email)
	{
		// When we are editing an email (as second time) we can end up with
		// double brackets so we strip them off here.
		if (email.startsWith("<"))
		{
			email = email.substring(1);
		}
		if (email.endsWith(">"))
		{
			email = email.substring(0, email.length() - 1);
		}

		final Item item = container.addItem(email);
		if (item != null)
		{
			item.getItemProperty("id").setValue(email);
			item.getItemProperty("email").setValue(email);
			String namedEmail;
			if (named != null && named.trim().length() > 0)
			{
				namedEmail = named + " <" + email + ">";
			}
			else
			{
				namedEmail = "<" + email + ">";
			}
			item.getItemProperty("namedemail").setValue(namedEmail);
		}
		return item;
	}

	class PlusClickAction implements Action1<ClickEvent>
	{
		@Override
		public void call(final ClickEvent event)
		{
			final TargetLine newLine = insertTargetLine(EmailForm.this.lines.size() + 1);
			newLine.targetAddress.focus();
		}
	}

	class MinusClickAction implements Action1<ClickEvent>
	{
		@Override
		public void call(final ClickEvent event)
		{
			final Button button = event.getButton();
			final TargetLine line = (TargetLine) button.getData();
			EmailForm.this.grid.removeRow(line.row);
			line.buttonSubscription.unsubscribe();
			EmailForm.this.lines.remove(line.row - 1);

			// recalculate rows
			int row = 1;
			for (final TargetLine aLine : EmailForm.this.lines)
			{
				aLine.row = row++;
			}
		}
	}

	class SendClickAction implements Action1<ClickEvent>, CompleteListener
	{
		@Override
		public void call(final ClickEvent t1)
		{
			EmailForm.this.getSend().setEnabled(false);
			if (isEmpty(EmailForm.this.getSubject().getValue()))
			{
				SMNotification.show("The subject may not be blank", Type.WARNING_MESSAGE);
			}
			else if (!checkValidAddresses())
			{
				SMNotification.show("All Email adddresses must be valid", Type.WARNING_MESSAGE);
			}
			else if (isEmpty(EmailForm.this.getCkEditor().getValue()))
			{
				SMNotification.show("The body of the email may not be blank", Type.WARNING_MESSAGE);
			}
			else

			{
				final WorkingDialog working = new WorkingDialog("Sending Email", "Sending...");
				UI.getCurrent().addWindow(working);

				// working.setWorker(runnable, listener);
				working.setWorker(new EntityManagerRunnable(new EmailWorker(UI.getCurrent(), EmailForm.this)), this);

			}
			EmailForm.this.getSend().setEnabled(true);

		}

		@Override
		public void complete()
		{
			SMNotification.show("Message sent", Type.TRAY_NOTIFICATION);
			EmailForm.this.owner.close();
		}

	}

	public HashSet<? extends DataSource> getAttachements()
	{
		HashSet<DataSource> attachements = new HashSet<>();

		for (AttachedFileLayout f : fileList)
		{
			attachements.add(f.getDataSource());

		}
		return attachements;
	}

	public TextField getPrimaryTargetAddress()
	{
		return primaryTargetAddress;
	}

	public ComboBox getPrimaryTypeCombo()
	{
		return primaryTypeCombo;
	}

	public CKEditorEmailField getCkEditor()
	{
		return ckEditor;
	}

	public User getSender()
	{
		return sender;
	}

	public TextField getSubject()
	{
		return subject;
	}

	public Contact getContact()
	{
		return contact;
	}

	public Button getSend()
	{
		return send;
	}

}
