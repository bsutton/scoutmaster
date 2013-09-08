package au.org.scoutmaster.views;

import org.apache.log4j.Logger;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Gender;
import au.org.scoutmaster.domain.GroupRole;
import au.org.scoutmaster.domain.PhoneType;
import au.org.scoutmaster.domain.Phone_;
import au.org.scoutmaster.domain.PreferredCommunications;
import au.org.scoutmaster.domain.SectionType;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.fields.GoogleField;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

@Menu(display = "Contact")
public class ContactView extends BaseCrudView<Contact> implements View, Selected<Contact>
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ContactView.class);

	public static final String NAME = "Contact";

	private CheckBox primaryPhone1;
	private CheckBox primaryPhone2;
	private CheckBox primaryPhone3;
	private Tab youthTab;

	TabSheet tabs = new TabSheet();

	private DateField birthDate;

	private Label labelAge;

	private Label fieldSectionEligibity;

	@Override
	protected AbstractLayout buildEditor(ValidatingFieldGroup<Contact> fieldGroup2)
	{
		tabs.setSizeFull();

		VerticalLayout layout = new VerticalLayout();
		layout.setDescription("Editor");

		overviewTab();
		contactTab();
		youthTab();
		memberTab();
		medicalTab();
		backgroundTab();
		googleTab();

		// When a persons birth date changes recalculate their age.
		birthDate.addValueChangeListener(new Property.ValueChangeListener()
		{
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event)
			{
				DateField birthDate = (DateField) event.getProperty();
				ContactDao daoContact = new DaoFactory().getContactDao();
				labelAge.setValue(daoContact.getAge(birthDate.getValue()).toString());
				fieldSectionEligibity.setReadOnly(false);
				fieldSectionEligibity.setValue(daoContact.getSectionEligibilty(birthDate.getValue()).toString());
				fieldSectionEligibity.setReadOnly(true);
			}
		});

		layout.addComponent(tabs);

		return layout;
	}

	private DateField overviewTab()
	{
		// Overview tab

		SMMultiColumnFormLayout<Contact> overviewForm = new SMMultiColumnFormLayout<Contact>(2, this.fieldGroup);
		overviewForm.setColumnLabelWidth(1, 0);
		overviewForm.setColumnFieldWidth(1, 200);
		overviewForm.setSizeFull();

		// overviewForm.setMargin(true);
		tabs.addTab(overviewForm, "Overview");
		overviewForm.bindBooleanField("Active", "active");
		overviewForm.newLine();
		overviewForm.colspan(2);
		final ComboBox role = overviewForm.bindEnumField("Role", "role", GroupRole.class);
		overviewForm.newLine();
		overviewForm.colspan(2);
		overviewForm.bindTokenField(this, "Tags", "tags", Tag.class);
		overviewForm.colspan(2);
		overviewForm.bindTextField("Firstname", Contact.FIRSTNAME);
		overviewForm.newLine();
		overviewForm.colspan(2);
		overviewForm.bindTextField("Middle name", "middlename");
		overviewForm.newLine();

		overviewForm.colspan(2);
		overviewForm.bindTextField("Lastname", Contact.LASTNAME);

		overviewForm.newLine();
		birthDate = overviewForm.bindDateField("Birth Date", Contact.BIRTH_DATE, "yyyy-MM-dd", Resolution.DAY);
		labelAge = overviewForm.bindLabel("Age");
		overviewForm.bindEnumField("Gender", "gender", Gender.class);
		overviewForm.newLine();

		role.addValueChangeListener(new ChangeListener(role, labelAge));

		return birthDate;

	}

	private void contactTab()
	{
		// Contact tab
		SMMultiColumnFormLayout<Contact> contactForm = new SMMultiColumnFormLayout<Contact>(3, this.fieldGroup);
		contactForm.setColumnLabelWidth(0, 100);
		contactForm.setColumnLabelWidth(1, 0);
		contactForm.setColumnLabelWidth(2, 60);
		contactForm.setColumnFieldWidth(0, 100);
		contactForm.setColumnFieldWidth(1, 100);
		contactForm.setColumnFieldWidth(2, 20);

		contactForm.setSizeFull();
		tabs.addTab(contactForm, "Contact");
		contactForm.setMargin(true);

		contactForm.colspan(3);
		contactForm.bindEnumField("Preferred Communications", "preferredCommunications", PreferredCommunications.class);
		contactForm.newLine();
		contactForm.colspan(3);
		contactForm.bindTextField("Home Email", "homeEmail");
		contactForm.newLine();
		contactForm.colspan(3);
		contactForm.bindTextField("Work Email", "workEmail");
		contactForm.newLine();

		contactForm.bindTextField("Phone 1", "phone1.phoneNo");
		contactForm.bindEnumField(null, "phone1.phoneType", PhoneType.class);
		primaryPhone1 = contactForm.bindBooleanField("Primary",
				Contact_.phone1.getName() + "." + Phone_.primaryPhone.getName());
		primaryPhone1.addValueChangeListener(new PhoneChangeListener());

		contactForm.newLine();
		contactForm.bindTextField("Phone 2", "phone2.phoneNo");
		contactForm.bindEnumField(null, "phone2.phoneType", PhoneType.class);
		primaryPhone2 = contactForm.bindBooleanField("Primary", "phone2.primaryPhone");
		primaryPhone2.addValueChangeListener(new PhoneChangeListener());

		contactForm.newLine();
		contactForm.bindTextField("Phone 3", "phone3.phoneNo");
		contactForm.bindEnumField(null, "phone3.phoneType", PhoneType.class);
		primaryPhone3 = contactForm.bindBooleanField("Primary", "phone3.primaryPhone");
		primaryPhone3.addValueChangeListener(new PhoneChangeListener());

		contactForm.newLine();
		contactForm.colspan(3);
		contactForm.bindTextField("Street", "address.street");
		contactForm.newLine();
		contactForm.colspan(3);
		contactForm.bindTextField("City", "address.city");

		contactForm.newLine();
		contactForm.bindTextField("State", "address.state");
		contactForm.newLine();
		contactForm.bindTextField("Postcode", "address.postcode");
		contactForm.newLine();
	}

	private Label youthTab()
	{
		// Youth tab
		SMMultiColumnFormLayout<Contact> youthForm = new SMMultiColumnFormLayout<Contact>(1, this.fieldGroup);
		youthForm.setColumnLabelWidth(0, 120);
		youthForm.setColumnFieldWidth(0, 400);
		youthTab = tabs.addTab(youthForm, "Youth");
		youthForm.setSizeFull();
		youthForm.setMargin(true);
		// fieldSectionEligibity =
		// youthForm.bindTextField("Section Eligibility", (String)null);
		fieldSectionEligibity = youthForm.bindLabel("Section Eligibility");
		// fieldSectionEligibity.setReadOnly(true);
		youthForm.newLine();
		youthForm.bindTextField("School", "school");
		youthForm.bindBooleanField("Custody Order", Contact_.custodyOrder);
		youthForm.newLine();
		youthForm.bindTextAreaField("Custody Order Details", Contact_.custodyOrderDetails, 4);
		return fieldSectionEligibity;
	}

	private void memberTab()
	{
		// Member tab
		SMMultiColumnFormLayout<Contact> memberForm = new SMMultiColumnFormLayout<Contact>(2, this.fieldGroup);
		memberForm.setColumnLabelWidth(0, 120);
		tabs.addTab(memberForm, "Member");
		memberForm.setMargin(true);
		memberForm.setSizeFull();
		memberForm.bindBooleanField("Member", "isMember");
		memberForm.newLine();
		memberForm.colspan(2);
		memberForm.bindEntityField("Section ", "section", SectionType.class, "name");
		memberForm.newLine();
		memberForm.colspan(2);
		memberForm.bindTextField("Member No", "memberNo");
		memberForm.newLine();
		memberForm.colspan(2);
		memberForm.bindDateField("Member Since", "memberSince", "yyyy-MM-dd", Resolution.DAY);
	}

	private void medicalTab()
	{
		// Medical Tab
		SMMultiColumnFormLayout<Contact> medicalForm = new SMMultiColumnFormLayout<Contact>(2, this.fieldGroup);
		medicalForm.setColumnLabelWidth(0, 140);
		medicalForm.setColumnFieldWidth(0, 200);
		medicalForm.setColumnLabelWidth(1, 0);
		medicalForm.setColumnFieldWidth(1, 100);
		tabs.addTab(medicalForm, "Medical");
		medicalForm.setMargin(true);
		medicalForm.setSizeFull();
		medicalForm.colspan(2);
		medicalForm.bindTextAreaField("Allergies", "allergies", 4);
		medicalForm.bindBooleanField("Ambulance Subscriber", "ambulanceSubscriber");
		medicalForm.newLine();
		medicalForm.bindBooleanField("Private Medical Ins.", "privateMedicalInsurance");
		medicalForm.newLine();
		medicalForm.bindTextField("Private Medical Fund", "privateMedicalFundName");
	}

	private void googleTab()
	{
		// Medical Tab
		GoogleField googleField = new GoogleField();

		tabs.addTab(googleField, "Map");

	}

	private void backgroundTab()
	{
		// Background tab
		SMMultiColumnFormLayout<Contact> background = new SMMultiColumnFormLayout<Contact>(2, this.fieldGroup);
		background.setColumnLabelWidth(0, 120);
		tabs.addTab(background, "Background");
		background.setMargin(true);
		background.setSizeFull();

		background.colspan(2);
		background.bindTextAreaField("Hobbies", "hobbies", 4);
		background.newLine();
		background.colspan(2);
		background.bindDateField("Affiliated Since", "affiliatedSince", "yyyy-MM-dd", Resolution.DAY);
		background.newLine();
		background.colspan(2);
		background.bindTextField("Current Employer", "currentEmployer");
		background.newLine();
		background.colspan(2);
		background.bindTextField("Job Title", "jobTitle");
		background.newLine();
		// background.bindTextField("Assets", "assets");
		background.bindBooleanField("Has WWC", "hasWWC");
		background.bindDateField("WWC Expiry", "wwcExpiry", "yyyy-MM-dd", Resolution.DAY);
		background.bindTextField("WWC No.", "wwcNo");
		background.newLine();
		background.bindBooleanField("Has Police Check", "hasPoliceCheck");
		background.bindDateField("Police Check Expiry", "policeCheckExpiry", "yyyy-MM-dd", Resolution.DAY);
		background.newLine();
		background.bindBooleanField("Has Food Handling", "hasFoodHandlingCertificate");
		background.bindBooleanField("Has First Aid Certificate", "hasFirstAidCertificate");
	}

	private final class ChangeListener implements Property.ValueChangeListener
	{
		private final Label labelAge;
		private final ComboBox role;
		private static final long serialVersionUID = 1L;

		private ChangeListener(ComboBox role, Label labelAge)
		{
			this.labelAge = labelAge;
			this.role = role;
		}

		public void valueChange(ValueChangeEvent event)
		{
			switch ((GroupRole) this.role.getValue())
			{
				case YouthMember:
					showYouth(true);
					break;
				default:
					showYouth(false);
					break;
			}
			ContactDao daoContact = new DaoFactory().getContactDao();
			labelAge.setValue(daoContact.getAge(ContactView.super.getCurrent()).toString());
		}

		private void showYouth(boolean visible)
		{
			youthTab.setVisible(visible);
		}
	}

	public class PhoneChangeListener implements ValueChangeListener
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event)
		{
			CheckBox property = (CheckBox) event.getProperty();
			Boolean value = property.getValue();

			if (property == primaryPhone1 && value == true)
			{
				primaryPhone2.setValue(false);
				primaryPhone3.setValue(false);
			}
			else if (property == primaryPhone2 && value == true)
			{
				primaryPhone1.setValue(false);
				primaryPhone3.setValue(false);
			}
			else if (property == primaryPhone3 && value == true)
			{
				primaryPhone2.setValue(false);
				primaryPhone1.setValue(false);
			}
		}

	}

	@Override
	protected void interceptSaveValues(Contact entity)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		JPAContainer<Contact> container = new DaoFactory().getContactDao().makeJPAContainer();

		Builder<Contact> builder = new HeadingPropertySet.Builder<Contact>();
		builder.addColumn("Firstname", Contact_.firstname).addColumn("Lastname", Contact_.lastname)
				.addColumn("Section", Contact_.section).addColumn("Phone", Contact.PRIMARY_PHONE)
				.addColumn("Member", Contact_.isMember).addColumn("Role", Contact_.role);

		super.init(Contact.class, container, builder.build());

	}

	@Override
	protected Filter getContainerFilter(String filterString)
	{
//		return new Or(new Or(new SimpleStringFilter(Contact_.firstname.getName(), filterString, true, false),
//				new SimpleStringFilter(Contact_.lastname.getName(), filterString, true, false)),
//				new SimpleStringFilter(Contact_.section.getName() + "." + SectionType_.name.getName(), filterString, true, false));
		return new Or(new SimpleStringFilter(Contact_.firstname.getName(), filterString, true, false),
		new SimpleStringFilter(Contact_.lastname.getName(), filterString, true, false));

	}

}
