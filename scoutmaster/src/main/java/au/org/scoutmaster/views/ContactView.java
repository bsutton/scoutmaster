package au.org.scoutmaster.views;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.listener.MouseEventLogged;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.GroupRoleDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Gender;
import au.org.scoutmaster.domain.GroupRole;
import au.org.scoutmaster.domain.GroupRole_;
import au.org.scoutmaster.domain.PhoneType;
import au.org.scoutmaster.domain.Phone_;
import au.org.scoutmaster.domain.PreferredCommunications;
import au.org.scoutmaster.domain.SectionType;
import au.org.scoutmaster.domain.SectionType_;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.fields.GoogleField;
import au.org.scoutmaster.fields.TagChangeListener;
import au.org.scoutmaster.fields.TagField;
import au.org.scoutmaster.forms.EmailForm;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@Menu(display = "Contacts")
public class ContactView extends BaseCrudView<Contact> implements View, Selected<Contact>, TagChangeListener
{

	@Override
	protected String getTitleText()
	{
		return "Contacts";
	}

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

	private Label ageField;

	// private ComboBox fieldSectionEligibity;

	private Image homeEmailImage;

	private Image workEmailImage;

	private TagField tagField;

	@Override
	protected VerticalLayout buildEditor(ValidatingFieldGroup<Contact> fieldGroup2)
	{
		tabs.setSizeFull();

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		overviewTab();
		contactTab();
		relationshipTab();
		youthTab();
		memberTab();
		medicalTab();
		backgroundTab();
		activityTab();
		noteTab();
		googleTab();

		// When a persons birth date changes recalculate their age.
		birthDate.addValueChangeListener(new Property.ValueChangeListener()
		{
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event)
			{
				DateField birthDate = (DateField) event.getProperty();
				ContactDao daoContact = new DaoFactory().getContactDao();
				ageField.setReadOnly(false);
				ageField.setValue("Age " + daoContact.getAge(birthDate.getValue()).toString());
				ageField.setReadOnly(true);
				// fieldSectionEligibity.setReadOnly(false);
				// fieldSectionEligibity.setValue(daoContact.getSectionEligibilty(birthDate.getValue()).getId());
				// fieldSectionEligibity.setReadOnly(true);
			}
		});

		layout.addComponent(tabs);
		// VerticalLayout c = new VerticalLayout();
		// layout.addComponent(c);
		// layout.setExpandRatio(c, 1);

		return layout;
	}

	private DateField overviewTab()
	{
		// Overview tab

		SMMultiColumnFormLayout<Contact> overviewForm = new SMMultiColumnFormLayout<Contact>(3, this.fieldGroup);
		overviewForm.setColumnFieldWidth(0, 100);
		overviewForm.setColumnLabelWidth(1, 0);
		overviewForm.setColumnFieldWidth(1, 120);
		overviewForm.setColumnLabelWidth(2, 40);
		overviewForm.setColumnFieldWidth(2, 80);
		overviewForm.setSizeFull();

		FormHelper<Contact> formHelper = overviewForm.getFormHelper();

		// overviewForm.setMargin(true);
		tabs.addTab(overviewForm, "Overview");
		overviewForm.bindBooleanField("Active", Contact_.active);
		overviewForm.newLine();
		overviewForm.colspan(3);
		final ComboBox role = formHelper.new EntityFieldBuilder<GroupRole>().setLabel("Role")
				.setField(Contact_.groupRole).setListFieldName(GroupRole_.name).build();

		overviewForm.newLine();
		overviewForm.colspan(3);
		overviewForm.bindTokenField(this, "Tags", Contact_.tags, Tag.class);
		overviewForm.colspan(3);
		overviewForm.bindTextField("Firstname", Contact_.firstname);
		overviewForm.newLine();
		overviewForm.colspan(3);
		overviewForm.bindTextField("Middle name", Contact_.middlename);
		overviewForm.newLine();

		overviewForm.colspan(3);
		overviewForm.bindTextField("Lastname", Contact_.lastname);

		overviewForm.newLine();
		birthDate = overviewForm.bindDateField("Birth Date", Contact_.birthDate, "yyyy-MM-dd", Resolution.DAY);
		// ageField = overviewForm.bindTextField("Age", "age");
		// ageField.setReadOnly(true);
		ageField = overviewForm.bindLabel("Age");
		overviewForm.setComponentAlignment(ageField, Alignment.MIDDLE_LEFT);
		overviewForm.newLine();
		overviewForm.bindEnumField("Gender", Contact_.gender, Gender.class);
		overviewForm.newLine();

		role.addValueChangeListener(new ChangeListener(role, ageField));

		return birthDate;

	}

	private void contactTab()
	{
		VerticalLayout contactTab = new VerticalLayout();
		contactTab.setSizeFull();
		// Contact tab
		SMMultiColumnFormLayout<Contact> contactForm = new SMMultiColumnFormLayout<Contact>(3, this.fieldGroup);
		contactForm.setColumnLabelWidth(0, 100);
		contactForm.setColumnLabelWidth(1, 0);
		contactForm.setColumnLabelWidth(2, 60);
		contactForm.setColumnFieldWidth(0, 100);
		contactForm.setColumnFieldWidth(1, 100);
		contactForm.setColumnFieldWidth(2, 20);

		// contactForm.setSizeFull();
		contactForm.setMargin(true);

		contactForm.colspan(3);
		contactForm.bindEnumField("Preferred Communications", "preferredCommunications", PreferredCommunications.class);
		contactForm.newLine();
		contactForm.colspan(3);
		final TextField homeEmail = contactForm.bindTextField("Home Email", Contact_.homeEmail);
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();

		FileResource resource = new FileResource(new File(basepath + "/WEB-INF/images/email.png"));

		homeEmailImage = new Image(null, resource);
		homeEmailImage.setDescription("Click to send an email");
		homeEmailImage.addClickListener(new MouseEventLogged.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void clicked(com.vaadin.event.MouseEvents.ClickEvent event)
			{
				showMailForm(homeEmail);

			}
		});
		contactForm.addComponent(homeEmailImage);
		homeEmailImage.setVisible(false);
		contactForm.newLine();
		contactForm.colspan(3);
		final TextField workEmail = contactForm.bindTextField("Work Email", Contact_.workEmail);
		workEmailImage = new Image(null, resource);
		workEmailImage.setDescription("Click to send an email");
		contactForm.addComponent(workEmailImage);
		workEmailImage.addClickListener(new MouseEventLogged.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void clicked(com.vaadin.event.MouseEvents.ClickEvent event)
			{
				showMailForm(workEmail);

			}
		});

		workEmailImage.setVisible(false);

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

		// Now add the child activity crud
		contactTab.addComponent(contactForm);

		tabs.addTab(contactTab, "Contact");

	}

	private void activityTab()
	{
		// Now add the child activity crud
		ChildActivityView activityView = new ChildActivityView();
		activityView.setSizeFull();
		super.addChildCrudListener(activityView);

		tabs.addTab(activityView, "Activity");

		// contactTab.setExpandRatio(activityView, 1.0f);

	}

	private void relationshipTab()
	{
		// Now add the child relationship crud
		ChildRelationshipView relationshipView = new ChildRelationshipView();
		relationshipView.setSizeFull();
		super.addChildCrudListener(relationshipView);

		tabs.addTab(relationshipView, "Relationship");

	}

	private void noteTab()
	{
		// Now add the child note crud
		ChildNoteView noteView = new ChildNoteView();
		noteView.setSizeFull();
		super.addChildCrudListener(noteView);

		tabs.addTab(noteView, "Note");
	}

	private void youthTab()
	{
		// Youth tab
		SMMultiColumnFormLayout<Contact> youthForm = new SMMultiColumnFormLayout<Contact>(1, this.fieldGroup);
		youthForm.setColumnLabelWidth(0, 120);
		youthForm.setColumnFieldWidth(0, 400);
		youthTab = tabs.addTab(youthForm, "Youth");
		youthForm.setSizeFull();
		youthForm.setMargin(true);
		youthForm.newLine();
		youthForm.bindTextField("School", "school");
		youthForm.bindBooleanField("Custody Order", Contact_.custodyOrder);
		youthForm.newLine();
		youthForm.bindTextAreaField("Custody Order Details", Contact_.custodyOrderDetails, 4);

	}

	private void memberTab()
	{
		// Member tab
		SMMultiColumnFormLayout<Contact> memberForm = new SMMultiColumnFormLayout<Contact>(2, this.fieldGroup);
		memberForm.setColumnLabelWidth(0, 120);
		tabs.addTab(memberForm, "Member");
		memberForm.setMargin(true);
		memberForm.setSizeFull();
		memberForm.bindBooleanField("Member", Contact_.isMember);
		memberForm.newLine();
		// memberForm.colspan(2);

		FormHelper<Contact> formHelper = memberForm.getFormHelper();

		formHelper.new EntityFieldBuilder<SectionType>().setLabel("Section").setField(Contact_.section)
				.setListFieldName(SectionType_.name).build();

		memberForm.newLine();
		// fieldSectionEligibity = formHelper.new
		// EntityFieldBuilder<SectionType>()
		// .setLabel("Section Eligibility").setField(
		// Contact_.sectionEligibility).setListFieldName(SectionType_.name).build();
		// fieldSectionEligibity.setReadOnly(true);

		memberForm.colspan(2);
		memberForm.bindTextField("Member No", Contact_.memberNo);
		memberForm.newLine();
		memberForm.colspan(2);
		memberForm.bindDateField("Member Since", Contact_.memberSince, "yyyy-MM-dd", Resolution.DAY);
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
		medicalForm.bindTextField("Medicare No.", Contact_.medicareNo);
		medicalForm.newLine();
		medicalForm.bindBooleanField("Ambulance Subscriber", Contact_.ambulanceSubscriber);
		medicalForm.newLine();
		medicalForm.bindBooleanField("Private Medical Ins.", Contact_.privateMedicalInsurance);
		medicalForm.newLine();
		medicalForm.bindTextField("Private Medical Fund", Contact_.privateMedicalFundName);
		medicalForm.newLine();
		medicalForm.bindTextField("Medical Fund No.", Contact_.medicalFundNo);
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
		background.bindTextAreaField("Hobbies", Contact_.hobbies, 4);
		background.newLine();
		background.colspan(2);
		background.bindDateField("Affiliated Since", Contact_.affiliatedSince, "yyyy-MM-dd", Resolution.DAY);
		background.newLine();
		background.colspan(2);
		background.bindTextField("Current Employer", Contact_.currentEmployer);
		background.newLine();
		background.colspan(2);
		background.bindTextField("Job Title", Contact_.jobTitle);
		background.newLine();
		background.bindBooleanField("License", Contact_.hasLicense);
		background.newLine();
		background.bindBooleanField("Has WWC", Contact_.hasWWC);
		background.bindDateField("WWC Expiry", Contact_.wwcExpiry, "yyyy-MM-dd", Resolution.DAY);
		background.bindTextField("WWC No.", Contact_.wwcNo);
		background.newLine();
		background.bindBooleanField("Has Police Check", Contact_.hasPoliceCheck);
		background.bindDateField("Police Check Expiry", Contact_.policeCheckExpiry, "yyyy-MM-dd", Resolution.DAY);
		background.newLine();
		background.bindBooleanField("Has Food Handling", Contact_.hasFoodHandlingCertificate);
		background.bindBooleanField("Has First Aid Certificate", Contact_.hasFirstAidCertificate);
	}

	private final class ChangeListener implements Property.ValueChangeListener
	{
		private final Label fieldAge;
		private final ComboBox role;
		private static final long serialVersionUID = 1L;

		private ChangeListener(ComboBox role, Label fieldAge)
		{
			this.fieldAge = fieldAge;
			this.role = role;
		}

		public void valueChange(ValueChangeEvent event)
		{
			Long groupRoleId = (Long) this.role.getValue();
			if (groupRoleId != null)
			{

				GroupRoleDao daoGroupRole = new DaoFactory().getGroupRoleDao();

				GroupRole groupRole = daoGroupRole.findById(groupRoleId);

				switch (groupRole.getBuiltIn())
				{
					case YouthMember:
						showYouth(true);
						break;
					default:
						showYouth(false);
						break;
				}
			}
			else
				showYouth(true);

			ContactDao daoContact = new DaoFactory().getContactDao();
			fieldAge.setReadOnly(false);
			fieldAge.setValue(daoContact.getAge(ContactView.super.getCurrent()).toString());
			fieldAge.setReadOnly(true);
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
	public void enter(ViewChangeEvent event)
	{
		JPAContainer<Contact> container = new DaoFactory().getContactDao().createVaadinContainer();

		Builder<Contact> builder = new HeadingPropertySet.Builder<Contact>();
		builder.addColumn("Firstname", Contact_.firstname).addColumn("Lastname", Contact_.lastname)
				.addColumn("Section", Contact_.section).addColumn("Phone", Contact.PRIMARY_PHONE)
				.addColumn("Member", Contact_.isMember).addColumn("Group Role", Contact_.groupRole);

		super.init(Contact.class, container, builder.build());

	}

	@Override
	protected Filter getContainerFilter(final String filterString, boolean advancedSearchActive)
	{
		setQueryModifier(filterString);
		return null;
	}

	@Override
	protected void clearAdvancedFilters()
	{
		EntityProvider<Contact> provider = container.getEntityProvider();

		ArrayList<Tag> tags = tagField.getTags();

		for (Tag tag : tags)
		{
			tagField.removeToken(tag);
		}
		provider.setQueryModifierDelegate(null);

	}

	private void setQueryModifier(final String filterString)
	{
		EntityProvider<Contact> provider = container.getEntityProvider();

		// hook the query delegate so we can fix the jpa query on the way
		// through.
		provider.setQueryModifierDelegate(new ContactDefaultQueryModifierDelegate(tagField.getTags(), filterString));

	}

	private void showMailForm(TextField emailField)
	{
		Window mailWindow = new Window("Send Email");
		mailWindow.setWidth("80%");
		mailWindow.setHeight("80%");
		User sender = (User) getSession().getAttribute("user");
		mailWindow.setContent(new EmailForm(mailWindow, sender, this.getCurrent(), emailField.getValue()));
		mailWindow.setVisible(true);
		mailWindow.center();
		UI.getCurrent().addWindow(mailWindow);

	}

	@Override
	public void rowChanged(EntityItem<Contact> item)
	{
		super.rowChanged(item);
		if (item != null)
		{
			Contact entity = item.getEntity();
			if (entity != null)
			{
				homeEmailImage.setVisible((entity.getHomeEmail() != null && entity.getHomeEmail().length() > 0));
				workEmailImage.setVisible((entity.getWorkEmail() != null && entity.getWorkEmail().length() > 0));
			}
		}
	}

	@Override
	protected AbstractLayout getAdvancedSearchLayout()
	{
		VerticalLayout advancedSearchLayout = new VerticalLayout();
		HorizontalLayout tagSearchLayout = new HorizontalLayout();
		tagField = new TagField("Search Tags", true);
		tagSearchLayout.addComponent(tagField);

		tagField.addChangeListener(this);
		tagSearchLayout.setSizeFull();
		advancedSearchLayout.addComponent(tagSearchLayout);

		HorizontalLayout stringSearchLayout = new HorizontalLayout();
		stringSearchLayout.addComponent(searchField);
		stringSearchLayout.setWidth("100%");

		advancedSearchLayout.addComponent(stringSearchLayout);

		return advancedSearchLayout;

	}

	public void onTagListChanged(ArrayList<Tag> tags)
	{
		triggerFilter();
		//resetFilter(tags, super.getSearchFieldText());

	}

//	void resetFilter(ArrayList<Tag> tags, String fullTextSearch)
//	{
//		setQueryModifier(fullTextSearch);
//
//	}
//
}
