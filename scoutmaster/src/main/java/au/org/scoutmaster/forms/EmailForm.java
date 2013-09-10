package au.org.scoutmaster.forms;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.vaadin.openesignforms.ckeditor.CKEditorConfig;
import org.vaadin.openesignforms.ckeditor.CKEditorTextField;

import au.com.vaadinutils.listener.ClickEventLogged;
import au.org.scoutmaster.dao.ActivityDao;
import au.org.scoutmaster.dao.ActivityTypeDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SMTPSettingsDao;
import au.org.scoutmaster.domain.Activity;
import au.org.scoutmaster.domain.ActivityType;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.SMTPServerSettings;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.util.SMNotification;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class EmailForm extends VerticalLayout implements com.vaadin.ui.Button.ClickListener
{
	private static Logger logger = Logger.getLogger(EmailForm.class);
	private static final long serialVersionUID = 1L;
	
	private User sender;
	private TextField toAddress;
	private TextField subject;
	private CKEditorTextField ckEditorTextField;
	private Window owner;
	private Button send;
	private TextField ccAddress;
	private Contact contact;

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
		
		GridLayout grid = new GridLayout(3, 2);
		grid.setWidth("100%");
		grid.setColumnExpandRatio(1, (float) 1.0);
		grid.setSpacing(true);

		List<String> targetTypes = getTargetTypes();
		ComboBox targetType = new ComboBox(null, targetTypes);
		targetType.setWidth("50");
		grid.addComponent(targetType);
		targetType.select(targetTypes.get(0));
		toAddress = new TextField();
		toAddress.setInputPrompt("Enter email address");
		toAddress.setValue(toEmailAddress);
		toAddress.setWidth("100%");
		grid.addComponent(toAddress);
		grid.newLine();
		
		// CC
		ComboBox targetTypeCC = new ComboBox(null, targetTypes);
		targetTypeCC.setWidth("50");
		targetTypeCC.select(targetTypes.get(1));
		grid.addComponent(targetTypeCC);
		
		ccAddress = new TextField();
		ccAddress.setInputPrompt("Enter email address");
		ccAddress.setWidth("100%");
		grid.addComponent(ccAddress);

		send = new Button("Send", new ClickEventLogged.ClickAdaptor(this));
		send.setImmediate(true);
		grid.addComponent(send);
		
		this.addComponent(grid);
		subject = new TextField("Subject");
		subject.setWidth("100%");
		
		this.addComponent(subject);
		CKEditorTextField ckEditor = getEditor();
		this.addComponent(ckEditor);
		this.setExpandRatio(ckEditor, 1.0f);
		
		subject.focus();

	}

	List<String> getTargetTypes()
	{
		ArrayList<String> targetTypes = new ArrayList<>();

		targetTypes.add("To");
		targetTypes.add("CC");
		targetTypes.add("BCC");
		return targetTypes;
	}

	CKEditorTextField getEditor()
	{

		CKEditorConfig config = new CKEditorConfig();
		config.useCompactTags();
		config.disableElementsPath();
		//config.setResizeDir(CKEditorConfig.RESIZE_DIR.HORIZONTAL);
		config.setResizeEnabled(false);
		config.setToolbarCanCollapse(false);
		config.disableResizeEditor();

		ckEditorTextField = new CKEditorTextField(config);
		ckEditorTextField.setWidth("100%");
		ckEditorTextField.setHeight("100%");

		ckEditorTextField.addValueChangeListener(new Property.ValueChangeListener()
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event)
			{
				// TODO Auto-generated method stub

			}
		});

		return ckEditorTextField;

	}

	@Override
	public void buttonClick(ClickEvent event)
	{
		this.send.setEnabled(false);
		
		SMTPSettingsDao daoSMTPSettings = new DaoFactory().getSMTPSettingsDao();
		SMTPServerSettings settings = daoSMTPSettings.findSettings();
		
		try
		{
			SMNotification.show("Sending...", Type.TRAY_NOTIFICATION);
			daoSMTPSettings.sendEmail(settings, this.sender.getEmailAddress(), this.toAddress.getValue(), this.ccAddress.getValue(), this.subject.getValue(), ckEditorTextField.getValue());
			
			// Log the activity
			ActivityDao daoActivity = new DaoFactory().getActivityDao();
			ActivityTypeDao daoActivityType = new DaoFactory().getActivityTypeDao();
			ActivityType type = daoActivityType.findByName(ActivityType.EMAIL);
			Activity activity = new Activity();
			activity.setAddedBy(this.sender);
			activity.setWithContact(this.contact);
			activity.setSubject(this.subject.getValue());
			activity.setDetails(this.ckEditorTextField.getValue());
			activity.setType(type);
			
			daoActivity.persist(activity);
			SMNotification.show("Message sent", Type.TRAY_NOTIFICATION);
			this.owner.close();
		}
		catch (EmailException e)
		{
			logger.error(e,e);
			this.send.setEnabled(true);
			SMNotification.show(e, Type.ERROR_MESSAGE);
		}
		
	}
}
