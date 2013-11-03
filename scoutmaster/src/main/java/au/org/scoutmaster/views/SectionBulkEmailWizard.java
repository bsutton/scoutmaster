package au.org.scoutmaster.views;

import java.io.File;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import net.sf.jasperreports.engine.JRException;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.fields.DependantComboBox;
import au.com.vaadinutils.fields.EntityComboBox;
import au.com.vaadinutils.menu.Menu;
import au.com.vaadinutils.wizards.bulkJasperEmail.JasperProxy;
import au.com.vaadinutils.wizards.bulkJasperEmail.Recipient;
import au.com.vaadinutils.wizards.bulkJasperEmail.WizardView;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.SectionType;
import au.org.scoutmaster.domain.SectionType_;
import au.org.scoutmaster.util.SMNotification;
import au.org.scoutmaster.views.SectionBulkEmailWizard.ContactRecipient;

import com.vaadin.navigator.View;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@Menu(display="Section", path="Test")
public class SectionBulkEmailWizard extends WizardView<SectionType, Contact, ContactRecipient> implements View
{
	private static final long serialVersionUID = 1L;
	public static final String NAME = "Section";

	/** Filter fields **/
	private VerticalLayout filterLayout;
	private TextField subjectField;
	private ComboBox letterField;
	private CheckBox sendNowField;
	private ComboBox sectionField;
	private ComboBox contactField;
	private DateField dateField;

	/**
	 * Confirm fields
	 */
	private VerticalLayout confirmLayout;
	private TextField confirmSubjectField;
	private TextField confirmSectionField;
	private Label confirmRecipientCountField;
	private TextField confirmContactField;
	private TextField confirmLetterField;
	private CheckBox confirmSendNowField;

	public SectionBulkEmailWizard()
	{
		super(new DaoFactory().getSectionTypeDao().createVaadinContainer(), new DaoFactory().getContactDao()
				.createVaadinContainer());
	}

	@Override
	public ContactRecipient getRecipient(Long recipientId)
	{
		return new ContactRecipient(recipientId);
	}

	class ContactRecipient implements Recipient
	{

		private Contact contact;

		public ContactRecipient(Long recipientId)
		{
			ContactDao daoContact = new DaoFactory().getContactDao();
			contact = daoContact.findById(recipientId);
		}

		@Override
		public String getEmailAddress()
		{
			// String email = contact.getHomeEmail();
			// if (email == null || email.trim().length() == 0)
			// email = contact.getWorkEmail();
			String email = "bsutton@noojee.com.au";
			return email;
		}

		@Override
		public String getDescription()
		{
			return contact.getFullname();
		}

	}

	public File getJasperReport()
	{
		File baseDirectory = VaadinService.getCurrent()
                .getBaseDirectory();
		return new File(new File(baseDirectory, "jasperreports"), "MemberReport.jasper");
	}

	@Override
	public HeadingPropertySet<Contact> getVisibleSelectColumns()
	{
		Builder<Contact> builder = new HeadingPropertySet.Builder<Contact>();
		builder.addColumn("Firstname", Contact_.firstname).addColumn("Lastname", Contact_.lastname)
				.addColumn("Birth Date", Contact_.birthDate);

		return builder.build();
	}

	@Override
	public SingularAttribute<SectionType, String> getParentDisplayProperty()
	{
		return SectionType_.name;
	}

	@Override
	public SingularAttribute<Contact, SectionType> getChildForeignAttribute()
	{
		return Contact_.section;
	}

	@Override
	public String getChildDisplayProperty()
	{

		// MetamodelImpl metaModel = new
		// MetamodelImpl(EntityManagerProvider.INSTANCE.getEntityManager());
		// EntityType entityModel = metaModel.entity(Contact.class);
		// Attribute attribute = entityModel.getAttribute("fullname");
		//
		// return (SingularAttribute<Contact, String>) attribute;

		return "fullname";
	}

	@Override
	public JasperProxy getJasperProxy() throws JRException
	{
		return new JasperProxy(getJasperReport(), subjectField.getValue(), "support@noojee.com.au");
	}

	@Override
	protected AbstractLayout buildFilter()
	{
		if (filterLayout == null)
		{
			filterLayout = new VerticalLayout();
			filterLayout.addComponent(new Label("Select the list you are looking email to. Then click 'Next'"));
			FormLayout formLayout = new FormLayout();
			
			dateField = new DateField("Unpublished Date");
			formLayout.addComponent(dateField);

			sectionField = new EntityComboBox<SectionType>("Section", getParentContainer(), getParentDisplayProperty());
			sectionField.setWidth("300");
			formLayout.addComponent(sectionField);

			contactField = new DependantComboBox<SectionType, Contact>("Contact", sectionField, getChildContainer(),
					getChildForeignAttribute(), getChildDisplayProperty());
			contactField.setWidth("300");
			formLayout.addComponent(contactField);

			subjectField = new TextField("Subject");
			subjectField.setWidth("300");
			formLayout.addComponent(subjectField);

			letterField = new ComboBox("Letter");
			List<Parameter<String>> parameterList = getParameters();
			for (Parameter<String> parameter : parameterList)
			{
				letterField.addItem(parameter);
			}
			letterField.setWidth("300");
			formLayout.addComponent(letterField);

			sendNowField = new CheckBox("Send Now");
			formLayout.addComponent(sendNowField);

			filterLayout.addComponent(formLayout);
			filterLayout.setMargin(true);

			filterLayout.setReadOnly(false);
		}

		return filterLayout;
	}

	@Override
	protected AbstractLayout buildConfirm()
	{
		if (confirmLayout == null)
		{
			confirmLayout = new VerticalLayout();
			confirmSectionField = new TextField("Section");
			confirmLayout.addComponent(confirmSectionField);
			confirmSectionField.setReadOnly(true);
			confirmSectionField.setWidth("100%");

			confirmContactField = new TextField("Contacts");
			confirmLayout.addComponent(confirmContactField);
			confirmContactField.setReadOnly(true);
			confirmContactField.setWidth("100%");

			confirmRecipientCountField = new Label("Recipients");
			confirmRecipientCountField.setContentMode(ContentMode.HTML);
			confirmRecipientCountField.setReadOnly(true);
			confirmLayout.addComponent(confirmRecipientCountField);

			confirmSubjectField = new TextField("Subject");
			confirmSubjectField.setWidth("100%");
			confirmSubjectField.setReadOnly(true);
			confirmLayout.addComponent(confirmSubjectField);

			confirmLetterField = new TextField("Letter");
			confirmLetterField.setWidth("100%");
			confirmLetterField.setReadOnly(true);
			confirmLayout.addComponent(confirmLetterField);

			confirmSendNowField = new CheckBox("Send Now");
			confirmSendNowField.setWidth("100%");
			confirmSendNowField.setReadOnly(true);
			confirmLayout.addComponent(confirmSendNowField);

			confirmLayout.setMargin(true);
		}

		/**
		 * Set the values each time as they may change between invocations.
		 */
		confirmRecipientCountField.setReadOnly(false);
		confirmRecipientCountField.setValue("<p><b>" + getRecipientStep().getRecipientCount()
				+ " recipients have been selected to recieve the transmission.</b></p>");
		confirmRecipientCountField.setReadOnly(true);

		if (sectionField.getValue() != null)
		{
			confirmSectionField.setReadOnly(false);
			confirmSectionField.setValue(sectionField.getConvertedValue().toString());
			confirmSectionField.setReadOnly(true);
		}

		if (contactField.getValue() != null)
		{
			confirmContactField.setReadOnly(false);
			confirmContactField.setValue(contactField.getConvertedValue().toString());
			confirmContactField.setReadOnly(true);
		}

		if (letterField.getValue() != null)
		{
			confirmLetterField.setReadOnly(false);
			confirmLetterField.setValue(letterField.getValue().toString());
			confirmLetterField.setReadOnly(true);
		}

		confirmSubjectField.setReadOnly(false);
		confirmSubjectField.setValue(subjectField.getValue());
		confirmSubjectField.setReadOnly(true);

		confirmSendNowField.setReadOnly(false);
		confirmSendNowField.setValue(sendNowField.getValue());
		confirmSendNowField.setReadOnly(true);

		return confirmLayout;
	}

	@Override
	public boolean validateFilter()
	{
		boolean valid = true;

		if (sectionField.getValue() == null)
		{
			SMNotification.show("Please select a Section Type", Type.WARNING_MESSAGE);
			valid = false;
		}

		else if (contactField.getValue() == null)
		{
			SMNotification.show("Please select a Contact", Type.WARNING_MESSAGE);
			valid = false;
		}

		else if (subjectField.getValue() == null || subjectField.getValue().trim().length() == 0)
		{
			SMNotification.show("Please enter a Subject", Type.WARNING_MESSAGE);
			valid = false;
		}
		
		else if (letterField.getValue() == null)
		{
			SMNotification.show("Please select a Letter", Type.WARNING_MESSAGE);
			valid = false;
		}


		return valid;
	}
}
