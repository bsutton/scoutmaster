package au.org.scoutmaster.views;

import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import com.vaadin.navigator.View;
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

import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.fields.DependantComboBox;
import au.com.vaadinutils.fields.EntityComboBox;
import au.com.vaadinutils.jasper.filter.ReportFilterUIBuilder;
import au.com.vaadinutils.jasper.parameter.ReportParameterConstant;
import au.com.vaadinutils.menu.Menu;
import au.com.vaadinutils.wizards.bulkJasperEmail.JasperProxy;
import au.com.vaadinutils.wizards.bulkJasperEmail.Recipient;
import au.com.vaadinutils.wizards.bulkJasperEmail.WizardView;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.application.ScoutmasterViewEnum;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.SectionType;
import au.org.scoutmaster.domain.SectionType_;
import au.org.scoutmaster.jasper.JasperEmailSettingsImpl;
import au.org.scoutmaster.jasper.SMJasperReportProperties;
import au.org.scoutmaster.util.SMNotification;
import au.org.scoutmaster.views.SectionBulkEmailWizard.ContactRecipient;
import net.sf.jasperreports.engine.JRException;

@Menu(display = "Section", path = "Test")
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
		super(new DaoFactory().getSectionTypeDao().createVaadinContainer(),
				new DaoFactory().getContactDao().createVaadinContainer());
	}

	@Override
	public ContactRecipient getRecipient(final Long recipientId)
	{
		return new ContactRecipient(recipientId);
	}

	class ContactRecipient implements Recipient
	{

		private final Contact contact;

		public ContactRecipient(final Long recipientId)
		{
			final ContactDao daoContact = new DaoFactory().getContactDao();
			this.contact = daoContact.findById(recipientId);
		}

		@Override
		public String getEmailAddress()
		{
			// String email = contact.getHomeEmail();
			// if (email == null || email.trim().length() == 0)
			// email = contact.getWorkEmail();
			final String email = "bsutton@noojee.com.au";
			return email;
		}

		@Override
		public String getDescription()
		{
			return this.contact.getFullname();
		}

	}

	public String getJasperReport()
	{
		return "MemberReport.jasper";
	}

	@Override
	public HeadingPropertySet<Contact> getVisibleSelectColumns()
	{
		final Builder<Contact> builder = new HeadingPropertySet.Builder<Contact>();
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
		final SMJasperReportProperties properties = new SMJasperReportProperties(ScoutmasterViewEnum.Member)
		{
			@Override
			public String getReportTitle()
			{
				return "Member Report";
			}

			@Override
			public String getReportFileName()
			{
				return getJasperReport();
			}

			@Override
			public ReportFilterUIBuilder getFilterBuilder()
			{
				ReportFilterUIBuilder builder = new ReportFilterUIBuilder();

				ReportParameterConstant<String> param = new ReportParameterConstant<String>("group_id",
						"" + SMSession.INSTANCE.getGroup().getId());
				builder.getReportParameters().add(param);

				return builder;
			}
		};
		return new JasperProxy(this.subjectField.getValue(), "support@noojee.com.au", new JasperEmailSettingsImpl(),
				properties);

	}

	@Override
	protected AbstractLayout buildFilter()
	{
		if (this.filterLayout == null)
		{
			this.filterLayout = new VerticalLayout();
			this.filterLayout.addComponent(new Label("Select the list you are looking email to. Then click 'Next'"));
			final FormLayout formLayout = new FormLayout();

			this.dateField = new DateField("Unpublished Date");
			formLayout.addComponent(this.dateField);

			this.sectionField = new EntityComboBox<SectionType>("Section", getParentContainer(),
					getParentDisplayProperty());
			this.sectionField.setWidth("300");
			formLayout.addComponent(this.sectionField);

			this.contactField = new DependantComboBox<SectionType, Contact>("Contact", this.sectionField,
					getChildContainer(), getChildForeignAttribute(), getChildDisplayProperty());
			this.contactField.setWidth("300");
			formLayout.addComponent(this.contactField);

			this.subjectField = new TextField("Subject");
			this.subjectField.setWidth("300");
			formLayout.addComponent(this.subjectField);

			this.letterField = new ComboBox("Letter");
			final List<Parameter<String>> parameterList = getParameters();
			for (final Parameter<String> parameter : parameterList)
			{
				this.letterField.addItem(parameter);
			}
			this.letterField.setWidth("300");
			formLayout.addComponent(this.letterField);

			this.sendNowField = new CheckBox("Send Now");
			formLayout.addComponent(this.sendNowField);

			this.filterLayout.addComponent(formLayout);
			this.filterLayout.setMargin(true);

			this.filterLayout.setReadOnly(false);
		}

		return this.filterLayout;
	}

	@Override
	protected AbstractLayout buildConfirm()
	{
		if (this.confirmLayout == null)
		{
			this.confirmLayout = new VerticalLayout();
			this.confirmSectionField = new TextField("Section");
			this.confirmLayout.addComponent(this.confirmSectionField);
			this.confirmSectionField.setReadOnly(true);
			this.confirmSectionField.setWidth("100%");

			this.confirmContactField = new TextField("Contacts");
			this.confirmLayout.addComponent(this.confirmContactField);
			this.confirmContactField.setReadOnly(true);
			this.confirmContactField.setWidth("100%");

			this.confirmRecipientCountField = new Label("Recipients");
			this.confirmRecipientCountField.setContentMode(ContentMode.HTML);
			this.confirmRecipientCountField.setReadOnly(true);
			this.confirmLayout.addComponent(this.confirmRecipientCountField);

			this.confirmSubjectField = new TextField("Subject");
			this.confirmSubjectField.setWidth("100%");
			this.confirmSubjectField.setReadOnly(true);
			this.confirmLayout.addComponent(this.confirmSubjectField);

			this.confirmLetterField = new TextField("Letter");
			this.confirmLetterField.setWidth("100%");
			this.confirmLetterField.setReadOnly(true);
			this.confirmLayout.addComponent(this.confirmLetterField);

			this.confirmSendNowField = new CheckBox("Send Now");
			this.confirmSendNowField.setWidth("100%");
			this.confirmSendNowField.setReadOnly(true);
			this.confirmLayout.addComponent(this.confirmSendNowField);

			this.confirmLayout.setMargin(true);
		}

		/**
		 * Set the values each time as they may change between invocations.
		 */
		this.confirmRecipientCountField.setReadOnly(false);
		this.confirmRecipientCountField.setValue("<p><b>" + getRecipientStep().getRecipientCount()
				+ " recipients have been selected to recieve the transmission.</b></p>");
		this.confirmRecipientCountField.setReadOnly(true);

		if (this.sectionField.getValue() != null)
		{
			this.confirmSectionField.setReadOnly(false);
			this.confirmSectionField.setValue(this.sectionField.getConvertedValue().toString());
			this.confirmSectionField.setReadOnly(true);
		}

		if (this.contactField.getValue() != null)
		{
			this.confirmContactField.setReadOnly(false);
			this.confirmContactField.setValue(this.contactField.getConvertedValue().toString());
			this.confirmContactField.setReadOnly(true);
		}

		if (this.letterField.getValue() != null)
		{
			this.confirmLetterField.setReadOnly(false);
			this.confirmLetterField.setValue(this.letterField.getValue().toString());
			this.confirmLetterField.setReadOnly(true);
		}

		this.confirmSubjectField.setReadOnly(false);
		this.confirmSubjectField.setValue(this.subjectField.getValue());
		this.confirmSubjectField.setReadOnly(true);

		this.confirmSendNowField.setReadOnly(false);
		this.confirmSendNowField.setValue(this.sendNowField.getValue());
		this.confirmSendNowField.setReadOnly(true);

		return this.confirmLayout;
	}

	@Override
	public boolean validateFilter()
	{
		boolean valid = true;

		if (this.sectionField.getValue() == null)
		{
			SMNotification.show("Please select a Section Type", Type.WARNING_MESSAGE);
			valid = false;
		}

		else if (this.contactField.getValue() == null)
		{
			SMNotification.show("Please select a Contact", Type.WARNING_MESSAGE);
			valid = false;
		}

		else if (this.subjectField.getValue() == null || this.subjectField.getValue().trim().length() == 0)
		{
			SMNotification.show("Please enter a Subject", Type.WARNING_MESSAGE);
			valid = false;
		}

		else if (this.letterField.getValue() == null)
		{
			SMNotification.show("Please select a Letter", Type.WARNING_MESSAGE);
			valid = false;
		}

		return valid;
	}
}
