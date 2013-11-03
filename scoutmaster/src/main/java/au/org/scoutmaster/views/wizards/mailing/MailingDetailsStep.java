package au.org.scoutmaster.views.wizards.mailing;

import java.io.File;
import java.util.HashSet;

import org.vaadin.easyuploads.FileBuffer;
import org.vaadin.easyuploads.MultiFileUpload;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.fields.CKEditorEmailField;
import au.com.vaadinutils.listener.ClickEventLogged;
import au.org.scoutmaster.domain.SMSProvider;
import au.org.scoutmaster.domain.access.User;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class MailingDetailsStep implements WizardStep
{
	private static final String TEMP_FILE_DIR = new File(System.getProperty("java.io.tmpdir")).getPath();

	private TextField subject;

	private TextField from;
	private ComboBox providers;
	private BulkEmailWizardView wizard;
	private VerticalLayout layout;
	private Label recipientCount;
	private CKEditorEmailField ckEditorTextField;

	VerticalLayout attachedFiles;
	
	private HashSet<AttachedFile>fileList = new HashSet<>();


	public MailingDetailsStep(BulkEmailWizardView messagingWizardView)
	{
		wizard = messagingWizardView;

		layout = new VerticalLayout();
		// layout.setDescription("MessageDetailsContent");
		layout.setSizeFull();
		layout.setMargin(true);
		layout.setSpacing(true);

		layout.addComponent(new Label("Enter the subject and message and then click next."));
		recipientCount = new Label("Recipient Count");
		recipientCount.setContentMode(ContentMode.HTML);
		layout.addComponent(recipientCount);

		from = new TextField("From Email Address");
		layout.addComponent(from);
		from.setWidth("100%");
		from.addValidator(new EmailValidator("'From Email address' must be supplied"));
		from.setDescription("Enter your email address so that all emails appear to come from you and recipients can directly reply to you.");

		User user = (User) VaadinSession.getCurrent().getAttribute("user");
		from.setValue(user.getEmailAddress());

		subject = new TextField("Subject");
		subject.setWidth("100%");
		layout.addComponent(subject);
		subject.addValidator(new StringLengthValidator("'Subject' must be supplied", 1, 255, false));

		ckEditorTextField = new CKEditorEmailField(false);

		layout.addComponent(ckEditorTextField);
		layout.setExpandRatio(ckEditorTextField, 1.0f);

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

		layout.addComponent(uploadArea);

	}

	@Override
	public String getCaption()
	{
		return "Message Details";
	}

	@Override
	public Component getContent()
	{
		int recipientCount = wizard.getRecipientStep().getRecipientCount();
		this.recipientCount
				.setValue("<p><b>" + recipientCount + pluralize(" recipient", recipientCount)
						+ pluralize(" has", " have", recipientCount)
						+ " been selected to recieve the following Email.</b></p>");

		return layout;
	}

	private String pluralize(String singularForm, String pluralForm, int recipientCount)
	{
		return (recipientCount == 1 ? singularForm : pluralForm);
	}

	private String pluralize(String singularForm, int recipientCount)
	{
		return (recipientCount == 1 ? singularForm : singularForm + "s");
	}

	@Override
	public boolean onAdvance()
	{

		boolean advance = notEmpty("Message", ckEditorTextField.getValue()) && notEmpty("From", from.getValue())
				&& notEmpty("Subject", subject.getValue());

		if (!advance)
			Notification.show("Please enter your Email a Subject and a Message then click Next");
		return advance;
	}

	private boolean notEmpty(String label, String value)
	{
		return value != null && value.length() > 0;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	public Message getMessage()
	{
		return new Message(subject.getValue(), ckEditorTextField.getValue(), from.getValue());
	}

	public SMSProvider getProvider()
	{
		return (SMSProvider) providers.getConvertedValue();
	}

	public String getFrom()
	{
		return from.getValue();
	}

	public String getSubject()
	{
		return subject.getValue();
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
		MailingDetailsStep.this.attachedFiles.addComponent(line);
		
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
				MailingDetailsStep.this.fileList.remove(file);

			}
		});

	}
	
	public HashSet<AttachedFile> getAttachedFiles()
	{
		return this.fileList;
	}

}
