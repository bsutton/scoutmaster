package au.org.scoutmaster.views.wizards.bulkEmail;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import org.vaadin.easyuploads.FileBuffer;
import org.vaadin.easyuploads.MultiFileUpload;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.fields.CKEditorEmailField;
import au.com.vaadinutils.listener.ClickEventLogged;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.fields.TagField;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class StepEnterDetails implements WizardStep
{
	private static final String TEMP_FILE_DIR = new File(System.getProperty("java.io.tmpdir")).getPath();

	private final TextField subject;

	private final TextField from;
	private final BulkEmailWizardView wizard;
	private final VerticalLayout layout;
	private final Label recipientCount;
	private final CKEditorEmailField ckEditorTextField;

	VerticalLayout attachedFiles;

	private final HashSet<AttachedFile> fileList = new HashSet<>();

	/**
	 * Used to tag the set of contacts that we successfully send the email to.
	 */
	private final TagField activityTag;

	public StepEnterDetails(final BulkEmailWizardView messagingWizardView)
	{
		this.wizard = messagingWizardView;

		this.layout = new VerticalLayout();
		// layout.setDescription("MessageDetailsContent");
		this.layout.setSizeFull();
		this.layout.setMargin(true);
		this.layout.setSpacing(true);

		this.layout.addComponent(new Label("Enter the subject and message and then click next."));
		this.recipientCount = new Label("Recipient Count");
		this.recipientCount.setContentMode(ContentMode.HTML);
		this.layout.addComponent(this.recipientCount);

		this.activityTag = new TagField("Activity Tag", false);
		this.activityTag.setWidth("100%");
		this.activityTag.setDescription("Enter a tag to associate with each Contact we successfully send to.");
		this.layout.addComponent(this.activityTag);

		this.from = new TextField("From Email Address");
		this.layout.addComponent(this.from);
		this.from.setWidth("100%");
		this.from.addValidator(new EmailValidator("'From Email address' must be supplied"));
		this.from
				.setDescription("Enter your email address so that all emails appear to come from you and recipients can directly reply to you.");

		final User user = (User) VaadinSession.getCurrent().getAttribute("user");
		this.from.setValue(user.getEmailAddress());

		this.subject = new TextField("Subject");
		this.subject.setWidth("100%");
		this.layout.addComponent(this.subject);
		this.subject.addValidator(new StringLengthValidator("'Subject' must be supplied", 1, 255, false));

		this.ckEditorTextField = new CKEditorEmailField(false);

		this.layout.addComponent(this.ckEditorTextField);
		this.layout.setExpandRatio(this.ckEditorTextField, 1.0f);
		if (user.getEmailSignature() != null)
		{
			this.ckEditorTextField.setValue("</br></br>" + user.getEmailSignature());
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

		this.layout.addComponent(uploadArea);

	}

	@Override
	public String getCaption()
	{
		return "Message Details";
	}

	@Override
	public Component getContent()
	{
		final int recipientCount = this.wizard.getRecipientStep().getRecipientCount();
		this.recipientCount
		.setValue("<p><b>" + recipientCount + pluralize(" recipient", recipientCount)
				+ pluralize(" has", " have", recipientCount)
				+ " been selected to recieve the following Email.</b></p>");

		return this.layout;
	}

	private String pluralize(final String singularForm, final String pluralForm, final int recipientCount)
	{
		return recipientCount == 1 ? singularForm : pluralForm;
	}

	private String pluralize(final String singularForm, final int recipientCount)
	{
		return recipientCount == 1 ? singularForm : singularForm + "s";
	}

	@Override
	public boolean onAdvance()
	{

		final boolean advance = notEmpty("Message", this.ckEditorTextField.getValue())
				&& notEmpty("From", this.from.getValue()) && notEmpty("Subject", this.subject.getValue());

		if (!advance)
		{
			Notification.show("Please enter your Email a Subject and a Message then click Next");
		}
		return advance;
	}

	private boolean notEmpty(final String label, final String value)
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
		return new Message(this.subject.getValue(), this.ckEditorTextField.getValue(), this.from.getValue());
	}

	public String getFrom()
	{
		return this.from.getValue();
	}

	public String getSubject()
	{
		return this.subject.getValue();
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
		multiFileUpload2.setRootDirectory(StepEnterDetails.TEMP_FILE_DIR);
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
		StepEnterDetails.this.attachedFiles.addComponent(line);

		final AttachedFile attachedFile = new AttachedFile(this.attachedFiles, file, line);
		this.fileList.add(attachedFile);
		removeButton.setData(attachedFile);

		removeButton.addClickListener(new ClickEventLogged.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void clicked(final ClickEvent event)
			{
				final AttachedFile file = (AttachedFile) event.getButton().getData();
				file.remove();
				StepEnterDetails.this.fileList.remove(file);

			}
		});

	}

	public HashSet<AttachedFile> getAttachedFiles()
	{
		return this.fileList;
	}

	public ArrayList<Tag> getActivityTags()
	{
		return this.activityTag.getTags();
	}

}
