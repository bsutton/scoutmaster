package au.org.scoutmaster.views;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.vaadin.tokenfield.TokenField;

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
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
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

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.help.HelpProvider;
import au.com.vaadinutils.listener.MouseEventLogged;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.GroupRoleDao;
import au.org.scoutmaster.dao.SectionTypeDao;
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
import au.org.scoutmaster.fields.TagField;
import au.org.scoutmaster.forms.EmailForm;
import au.org.scoutmaster.help.HelpPageIdentifier;
import au.org.scoutmaster.util.ButtonEventSource;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;
import rx.util.functions.Action1;

@Menu(display = "Contacts", path = "Members")
public class ContactView extends BaseCrudView<Contact> implements View, Selected<Contact>, HelpProvider
{

	@Override
	protected String getTitleText()
	{
		return "Contacts";
	}

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger();

	public static final String NAME = "Contact";

	private CheckBox primaryPhone1;
	private CheckBox primaryPhone2;
	private CheckBox primaryPhone3;
	private Tab youthTab;

	TabSheet tabs = new TabSheet();

	private DateField birthDate;

	private Label ageField;

	private ComboBox fieldMemberSectionEligibity;

	private Image homeEmailImage;

	private Image workEmailImage;

	private TagField tagSearchField;

	private ComboBox groupRoleField;

	private ComboBox sectionTypeField;

	private ChangeListener changeListener;

	private TokenField tagField;

	private CheckBox isMemberField;

	public TextField membershipNoField;

	public DateField memberSinceField;

	public DateField dateMemberInvested;

	@Override
	protected VerticalLayout buildEditor(final ValidatingFieldGroup<Contact> fieldGroup2)
	{
		this.tabs.setSizeFull();

		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		// this needs to be updated when the contact changes
		this.changeListener = new ChangeListener();

		overviewTab();
		relationshipTab();
		youthTab();
		memberTab();
		medicalTab();
		backgroundTab();
		logTab();
		noteTab();
		// googleTab();

		// When a persons birth date changes recalculate their age.
		this.birthDate.addValueChangeListener(this.changeListener);

		layout.addComponent(this.tabs);
		// VerticalLayout c = new VerticalLayout();
		// layout.addComponent(c);
		// layout.setExpandRatio(c, 1);

		return layout;
	}

	private DateField overviewTab()
	{
		// Overview tab

		final SMMultiColumnFormLayout<Contact> overviewForm = new SMMultiColumnFormLayout<Contact>(3, this.fieldGroup);
		overviewForm.setColumnFieldWidth(0, 100);
		overviewForm.setColumnLabelWidth(1, 0);
		overviewForm.setColumnFieldWidth(1, 140);
		overviewForm.setColumnLabelWidth(2, 40);
		overviewForm.setColumnFieldWidth(2, 80);
		overviewForm.setMargin(true);

		// overviewForm.setColumnLabelWidth(0, 100);
		// overviewForm.setColumnLabelWidth(1, 0);
		// overviewForm.setColumnLabelWidth(2, 60);
		// overviewForm.setColumnFieldWidth(0, 100);
		// overviewForm.setColumnFieldWidth(1, 100);
		// overviewForm.setColumnFieldWidth(2, 20);

		// overviewForm.setSizeFull();

		final FormHelper<Contact> formHelper = overviewForm.getFormHelper();

		// overviewForm.setMargin(true);
		this.tabs.addTab(overviewForm, "Overview");
		overviewForm.bindBooleanField("Active", Contact_.active);
		overviewForm.newLine();
		overviewForm.colspan(3);
		this.groupRoleField = formHelper.new EntityFieldBuilder<GroupRole>().setLabel("Role")
				.setField(Contact_.groupRole).setListFieldName(GroupRole_.name).build();
		this.groupRoleField.addValueChangeListener(this.changeListener);

		overviewForm.newLine();
		overviewForm.colspan(3);
		overviewForm.bindTextField("Firstname", Contact_.firstname);
		overviewForm.newLine();
		overviewForm.colspan(3);
		overviewForm.bindTextField("Middle name", Contact_.middlename);
		overviewForm.newLine();

		overviewForm.colspan(3);
		overviewForm.bindTextField("Lastname", Contact_.lastname);

		overviewForm.newLine();
		this.birthDate = overviewForm.bindDateField("Birth Date", Contact_.birthDate, "yyyy/MM/dd", Resolution.DAY);
		// fieldOverviewSectionEligibity = overviewForm.addTextField(null);
		// fieldOverviewSectionEligibity.setReadOnly(true);
		this.ageField = overviewForm.bindLabel("Age");
		// overviewForm.setComponentAlignment(this.ageField,
		// Alignment.MIDDLE_LEFT);
		overviewForm.newLine();
		overviewForm.bindEnumField("Gender", Contact_.gender, Gender.class);
		overviewForm.newLine();

		overviewForm.colspan(3);
		overviewForm.bindBooleanField("Do Not Send Bulk Communications", Contact_.doNotSendBulkCommunications);
		overviewForm.newLine();

		overviewForm.colspan(3);
		overviewForm.bindEnumField("Preferred Communications", Contact_.preferredCommunications,
				PreferredCommunications.class);
		overviewForm.newLine();

		overviewForm.colspan(3);
		final TextField homeEmail = overviewForm.bindTextField("Home Email", Contact_.homeEmail);
		final String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();

		final FileResource resource = new FileResource(new File(basepath + "/images/email.png"));

		this.homeEmailImage = new Image(null, resource);
		this.homeEmailImage.setDescription("Click to send an email");
		this.homeEmailImage.addClickListener(new MouseEventLogged.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void clicked(final com.vaadin.event.MouseEvents.ClickEvent event)
			{
				showMailForm(homeEmail);

			}
		});
		overviewForm.addComponent(this.homeEmailImage);
		this.homeEmailImage.setVisible(false);
		overviewForm.newLine();
		overviewForm.colspan(3);
		final TextField workEmail = overviewForm.bindTextField("Work Email", Contact_.workEmail);
		this.workEmailImage = new Image(null, resource);
		this.workEmailImage.setDescription("Click to send an email");
		overviewForm.addComponent(this.workEmailImage);
		this.workEmailImage.addClickListener(new MouseEventLogged.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void clicked(final com.vaadin.event.MouseEvents.ClickEvent event)
			{
				showMailForm(workEmail);

			}
		});

		this.workEmailImage.setVisible(false);

		overviewForm.newLine();

		overviewForm.bindTextField("Phone 1", "phone1.phoneNo");
		overviewForm.bindEnumField(null, "phone1.phoneType", PhoneType.class);
		this.primaryPhone1 = overviewForm.bindBooleanField("Primary",
				Contact_.phone1.getName() + "." + Phone_.primaryPhone.getName());
		this.primaryPhone1.addValueChangeListener(new PhoneChangeListener());

		overviewForm.newLine();
		overviewForm.bindTextField("Phone 2", "phone2.phoneNo");
		overviewForm.bindEnumField(null, "phone2.phoneType", PhoneType.class);
		this.primaryPhone2 = overviewForm.bindBooleanField("Primary", "phone2.primaryPhone");
		this.primaryPhone2.addValueChangeListener(new PhoneChangeListener());

		overviewForm.newLine();
		overviewForm.bindTextField("Phone 3", "phone3.phoneNo");
		overviewForm.bindEnumField(null, "phone3.phoneType", PhoneType.class);
		this.primaryPhone3 = overviewForm.bindBooleanField("Primary", "phone3.primaryPhone");
		this.primaryPhone3.addValueChangeListener(new PhoneChangeListener());

		overviewForm.newLine();
		overviewForm.colspan(3);
		overviewForm.bindTextField("Street", "address.street");
		overviewForm.newLine();
		overviewForm.colspan(3);
		overviewForm.bindTextField("City", "address.city");

		overviewForm.newLine();
		overviewForm.bindTextField("State", "address.state");
		overviewForm.bindTextField("Postcode", "address.postcode");
		overviewForm.newLine();

		overviewForm.colspan(3);
		this.tagField = overviewForm.bindTagField(this, "Tags", Contact_.tags);

		// tabs.addTab(overviewForm, "Contact");

		return this.birthDate;

	}

	private void logTab()
	{
		// Now add the child log crud
		final ChildCommunicationView logView = new ChildCommunicationView(this);
		logView.setSizeFull();
		super.addChildCrudListener(logView);

		this.tabs.addTab(logView, "Log");

	}

	private void relationshipTab()
	{
		// Now add the child relationship crud
		final ChildRelationshipView relationshipView = new ChildRelationshipView(this);
		relationshipView.setSizeFull();
		super.addChildCrudListener(relationshipView);

		this.tabs.addTab(relationshipView, "Relationship");

	}

	private void noteTab()
	{
		// Now add the child note crud
		final ChildNoteView noteView = new ChildNoteView(this);
		noteView.setSizeFull();
		super.addChildCrudListener(noteView);

		this.tabs.addTab(noteView, "Note");
	}

	private void youthTab()
	{
		// Youth tab
		final SMMultiColumnFormLayout<Contact> youthForm = new SMMultiColumnFormLayout<Contact>(1, this.fieldGroup);
		youthForm.setColumnLabelWidth(0, 120);
		youthForm.setColumnFieldWidth(0, 400);
		youthForm.setMargin(true);
		youthForm.newLine();
		youthForm.bindTextField("School", "school");
		youthForm.bindBooleanField("Custody Order", Contact_.custodyOrder);
		youthForm.newLine();
		youthForm.bindTextAreaField("Custody Order Details", Contact_.custodyOrderDetails, 4);

		this.youthTab = this.tabs.addTab(youthForm, "Youth");
	}

	private void memberTab()
	{
		// Member tab
		final SMMultiColumnFormLayout<Contact> memberForm = new SMMultiColumnFormLayout<Contact>(2, this.fieldGroup);
		memberForm.setColumnLabelWidth(0, 120);
		memberForm.setMargin(true);
		this.isMemberField = memberForm.bindBooleanField("Member", Contact_.isMember);
		this.isMemberField.addValueChangeListener(this.changeListener);
		memberForm.newLine();

		final FormHelper<Contact> formHelper = memberForm.getFormHelper();

		this.sectionTypeField = formHelper.new EntityFieldBuilder<SectionType>().setLabel("Section")
				.setField(Contact_.section).setListFieldName(SectionType_.name).build();
		this.sectionTypeField.addValueChangeListener(this.changeListener);
		this.sectionTypeField.setNullSelectionAllowed(true);

		memberForm.newLine();
		this.fieldMemberSectionEligibity = formHelper.new EntityFieldBuilder<SectionType>()
				.setLabel("Section Eligibility").setField(Contact_.sectionEligibility)
				.setListFieldName(SectionType_.name).build();
		this.fieldMemberSectionEligibity.setReadOnly(true);
		memberForm.newLine();

		memberForm.colspan(2);
		this.membershipNoField = memberForm.bindTextField("Member No", Contact_.memberNo);
		memberForm.newLine();
		memberForm.colspan(2);
		this.memberSinceField = memberForm.bindDateField("Member Since", Contact_.memberSince, "yyyy-MM-dd",
				Resolution.DAY);

		memberForm.newLine();
		memberForm.colspan(2);
		this.dateMemberInvested = memberForm.bindDateField("Investiture Date", Contact_.dateMemberInvested,
				"yyyy-MM-dd", Resolution.DAY);

		this.tabs.addTab(memberForm, "Member");

	}

	private void medicalTab()
	{
		// SMSession.INSTANCE.getLoggedInUser().getBelongsTo().contains(Role)
		// Medical Tab
		final SMMultiColumnFormLayout<Contact> medicalForm = new SMMultiColumnFormLayout<Contact>(2, this.fieldGroup);
		medicalForm.setColumnLabelWidth(0, 140);
		medicalForm.setColumnFieldWidth(0, 200);
		medicalForm.setColumnLabelWidth(1, 0);
		medicalForm.setColumnFieldWidth(1, 100);
		this.tabs.addTab(medicalForm, "Medical");
		medicalForm.setMargin(true);
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

	// private void googleTab()
	// {
	// // Medical Tab
	// GoogleField googleField = new GoogleField();
	//
	// tabs.addTab(googleField, "Map");
	//
	// }

	private void backgroundTab()
	{
		// Background tab
		final SMMultiColumnFormLayout<Contact> background = new SMMultiColumnFormLayout<Contact>(2, this.fieldGroup);
		background.setColumnLabelWidth(0, 120);
		this.tabs.addTab(background, "Background");
		background.setMargin(true);

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
		final DateField wwcExpiryDate = background.bindDateField("WWC Expiry", Contact_.wwcExpiry, "yyyy-MM-dd",
				Resolution.DAY);
		// WWC expiry is five years.
		wwcExpiryDate.setValue(new DateTime().plusYears(5).toDate());
		background.bindTextField("WWC No.", Contact_.wwcNo);
		background.newLine();
		background.bindBooleanField("Has Police Check", Contact_.hasPoliceCheck);
		final DateField policeCheckExpiry = background.bindDateField("Police Check Expiry", Contact_.policeCheckExpiry,
				"yyyy-MM-dd", Resolution.DAY);
		policeCheckExpiry.setValue(new DateTime().plusYears(5).toDate());
		background.newLine();
		background.bindBooleanField("Has Food Handling", Contact_.hasFoodHandlingCertificate);
		background.bindBooleanField("Has First Aid Certificate", Contact_.hasFirstAidCertificate);
	}

	private final class ChangeListener implements Property.ValueChangeListener
	{
		private GroupRole currentGroupRole;
		private SectionType currentSectionType;
		private Date currentBirthDate;
		private Boolean currentIsMember;

		private static final long serialVersionUID = 1L;

		private void reset(final Contact contact)
		{
			if (contact != null)
			{
				this.currentGroupRole = contact.getRole();
				this.currentSectionType = contact.getSection();
				this.currentBirthDate = contact.getBirthDate();
				this.currentIsMember = contact.getIsMember();
			}
			else
			{
				this.currentGroupRole = null;
				this.currentSectionType = null;
				this.currentBirthDate = null;
				this.currentIsMember = null;
			}
		}

		@Override
		public void valueChange(final ValueChangeEvent event)
		{
			// Long groupRoleId = (Long) this.role.getValue();

			@SuppressWarnings("rawtypes")
			final Property source = event.getProperty();

			if (source == ContactView.this.groupRoleField)
			{
				final Long newGroupRoleId = (Long) event.getProperty().getValue();
				if (newGroupRoleId != null && this.currentGroupRole != null
						&& this.currentGroupRole.getId().equals(newGroupRoleId))
				{
					final GroupRoleDao daoGroupRole = new DaoFactory().getGroupRoleDao();

					final GroupRole newGroupRole = daoGroupRole.findById(newGroupRoleId);

					if (newGroupRole != null)
					{

						final GroupRole oldGroupRole = this.currentGroupRole;

						// Update the tag which represents this role
						if (newGroupRole != oldGroupRole && newGroupRole != null)
						{
							// Contact contact = ContactView.this.getCurrent();
							// First remove the old set of tags associated with
							// the
							// group
							if (oldGroupRole != null)
							{
								for (final Tag tag : oldGroupRole.getTags())
								{
									ContactView.this.tagField.removeToken(tag);
								}
							}

							// Now add the new set of tags associated with the
							// new
							// group role.
							for (final Tag tag : newGroupRole.getTags())
							{
								ContactView.this.tagField.addToken(tag);
							}

						}

						switch (newGroupRole.getBuiltIn())
						{
							case YouthMember:
								showYouth(true);
								break;
							default:
								showYouth(false);
								break;
						}
						this.currentGroupRole = newGroupRole;
					}
					else
					{
						throw new IllegalStateException("No group role found for : " + newGroupRoleId);
					}

				}

				if (newGroupRoleId == null)
				{
					showYouth(true);
					this.currentGroupRole = null;
				}
			}
			else if (source == ContactView.this.birthDate)
			{
				final ContactDao daoContact = new DaoFactory().getContactDao();

				final DateField birthDate = (DateField) event.getProperty();
				final Date newBirthDate = birthDate.getValue();
				if (this.currentBirthDate != null)
				{
					ContactView.this.fieldMemberSectionEligibity.setReadOnly(false);
					ContactView.this.fieldMemberSectionEligibity
							.setValue(daoContact.getSectionEligibilty(newBirthDate).getId());
					ContactView.this.fieldMemberSectionEligibity.setReadOnly(true);
					// fieldOverviewSectionEligibity.setReadOnly(false);
					// fieldOverviewSectionEligibity.setValue(daoContact.getSectionEligibilty(newBirthDate).getName());
					// fieldOverviewSectionEligibity.setReadOnly(true);
					this.currentBirthDate = newBirthDate;
					ContactView.this.ageField.setValue("Age " + daoContact.getAge(newBirthDate).toString() + " "
							+ daoContact.getSectionEligibilty(newBirthDate).getName() + "");
				}
			}
			else if (source == ContactView.this.sectionTypeField)
			{
				// The change event may have been for the section type
				// The Contacts section has just been changed so we need to
				// reset
				// the tag used to represent that section type.
				@SuppressWarnings("unchecked")
				final Property<Long> property = event.getProperty();

				final Long newSectionTypeId = property.getValue();
				if (!(this.currentSectionType == null && newSectionTypeId != null) && this.currentSectionType != null
						&& !newSectionTypeId.equals(this.currentSectionType.getId()))
				{
					final SectionType newValue = updateSectionTags(newSectionTypeId);
					this.currentSectionType = newValue;
				}
			}
			else if (source == ContactView.this.isMemberField)
			{
				@SuppressWarnings("unchecked")
				final Property<Boolean> property = event.getProperty();

				final Boolean newIsMember = property.getValue();
				if (this.currentIsMember != null && this.currentIsMember != newIsMember)
				{

					final boolean isMember = ContactView.this.isMemberField.getValue() == null ? false
							: ContactView.this.isMemberField.getValue();

					updateMembership(isMember);
					this.currentIsMember = isMember;
				}
			}

		}

		/**
		 * Only members belong to a section so add/remove section tags according
		 * to the users membership.
		 *
		 * @param isMember
		 */
		private void updateMembership(final boolean isMember)
		{
			final SectionTypeDao daoSectionType = new DaoFactory().getSectionTypeDao();
			final Tag tag = daoSectionType.getTag(this.currentSectionType);
			if (isMember == false)
			{
				if (tag != null)
				{
					ContactView.this.tagField.removeToken(tag);
				}
				ContactView.this.sectionTypeField.select(null);
				ContactView.this.sectionTypeField.setReadOnly(true);
				ContactView.this.membershipNoField.setReadOnly(true);
				ContactView.this.memberSinceField.setReadOnly(true);
			}
			else
			{
				ContactView.this.sectionTypeField.setReadOnly(false);
				ContactView.this.membershipNoField.setReadOnly(false);
				ContactView.this.memberSinceField.setReadOnly(false);
				if (tag != null)
				{
					ContactView.this.tagField.addToken(tag);
				}
			}
		}

		private SectionType updateSectionTags(final Long newSectionTypeId)
		{
			final SectionTypeDao daoSectionType = new DaoFactory().getSectionTypeDao();

			SectionType newValue = null;
			if (newSectionTypeId != null)
			{
				newValue = daoSectionType.findById(newSectionTypeId);
			}

			final boolean isMember = ContactView.this.isMemberField.getValue() == null ? false
					: ContactView.this.isMemberField.getValue();

			// both of these conditions should always be true.
			if (this.currentSectionType != newValue)
			{
				if (this.currentSectionType != null)
				{
					final Tag oldTag = daoSectionType.getTag(this.currentSectionType);
					ContactView.this.tagField.removeToken(oldTag);
				}

				// Only members belong to a section.
				if (newValue != null && isMember == true)
				{
					final Tag newTag = daoSectionType.getTag(newValue);
					ContactView.this.tagField.addToken(newTag);
				}
			}
			return newValue;
		}

		private void showYouth(final boolean visible)
		{
			ContactView.this.youthTab.setVisible(visible);
		}
	}

	public class PhoneChangeListener implements ValueChangeListener
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(final ValueChangeEvent event)
		{
			final CheckBox property = (CheckBox) event.getProperty();
			final Boolean value = property.getValue();

			if (property == ContactView.this.primaryPhone1 && value == true)
			{
				ContactView.this.primaryPhone2.setValue(false);
				ContactView.this.primaryPhone3.setValue(false);
			}
			else if (property == ContactView.this.primaryPhone2 && value == true)
			{
				ContactView.this.primaryPhone1.setValue(false);
				ContactView.this.primaryPhone3.setValue(false);
			}
			else if (property == ContactView.this.primaryPhone3 && value == true)
			{
				ContactView.this.primaryPhone2.setValue(false);
				ContactView.this.primaryPhone1.setValue(false);
			}
		}

	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		final JPAContainer<Contact> container = new DaoFactory().getContactDao().createVaadinContainer();

		final Builder<Contact> builder = new HeadingPropertySet.Builder<Contact>();
		builder.addColumn("Firstname", Contact_.firstname).addColumn("Lastname", Contact_.lastname)
				.addColumn("Section", Contact_.section).addColumn("Phone", Contact.PRIMARY_PHONE)
				.addColumn("Member", Contact_.isMember).addColumn("Group Role", Contact_.groupRole);

		super.init(Contact.class, container, builder.build());

	}

	@Override
	protected Filter getContainerFilter(final String filterString, final boolean advancedSearchActive)
	{
		setQueryModifier(filterString);
		return null;
	}

	@Override
	protected void clearAdvancedFilters()
	{
		final EntityProvider<Contact> provider = this.container.getEntityProvider();

		final ArrayList<Tag> tags = this.tagSearchField.getTags();

		for (final Tag tag : tags)
		{
			this.tagSearchField.removeToken(tag);
		}
		provider.setQueryModifierDelegate(null);

	}

	private void setQueryModifier(final String filterString)
	{
		final EntityProvider<Contact> provider = this.container.getEntityProvider();

		// hook the query delegate so we can fix the jpa query on the way
		// through.
		provider.setQueryModifierDelegate(
				new ContactDefaultQueryModifierDelegate(this.tagSearchField.getTags(), null, filterString, false));

	}

	private void showMailForm(final TextField emailField)
	{
		final Window mailWindow = new Window("Send Email");
		mailWindow.setWidth("80%");
		mailWindow.setHeight("80%");
		final User sender = (User) getSession().getAttribute("user");
		mailWindow.setContent(new EmailForm(mailWindow, sender, getCurrent(), emailField.getValue()));
		mailWindow.setVisible(true);
		mailWindow.center();
		UI.getCurrent().addWindow(mailWindow);

	}

	@Override
	public void rowChanged(final EntityItem<Contact> item)
	{
		if (item != null)
		{
			final Contact entity = item.getEntity();
			if (entity != null)
			{
				this.changeListener.reset(entity);
				super.rowChanged(item);
				this.homeEmailImage.setVisible(entity.getHomeEmail() != null && entity.getHomeEmail().length() > 0);
				this.workEmailImage.setVisible(entity.getWorkEmail() != null && entity.getWorkEmail().length() > 0);
				// fieldOverviewSectionEligibity.setReadOnly(false);
				// fieldOverviewSectionEligibity.setValue(entity.getSectionEligibility().getName());
				// fieldOverviewSectionEligibity.setReadOnly(true);

			}
			else
			{
				this.changeListener.reset(null);
				super.rowChanged(item);
			}
		}
		else
		{
			this.changeListener.reset(null);
			super.rowChanged(item);
		}

	}

	@Override
	protected AbstractLayout getAdvancedSearchLayout()
	{
		final VerticalLayout advancedSearchLayout = new VerticalLayout();
		advancedSearchLayout.setSpacing(true);

		final HorizontalLayout tagSearchLayout = new HorizontalLayout();
		this.tagSearchField = new TagField("Search Tags", true);
		tagSearchLayout.addComponent(this.tagSearchField);

		tagSearchLayout.setSizeFull();
		advancedSearchLayout.addComponent(tagSearchLayout);

		final HorizontalLayout stringSearchLayout = new HorizontalLayout();
		stringSearchLayout.addComponent(this.searchField);
		stringSearchLayout.setWidth("100%");

		advancedSearchLayout.addComponent(stringSearchLayout);

		final Button searchButton = new Button("Search");
		final Action1<ClickEvent> searchClickAction = new SearchClickAction();
		ButtonEventSource.fromActionOf(searchButton).subscribe(searchClickAction);

		advancedSearchLayout.addComponent(searchButton);
		advancedSearchLayout.setComponentAlignment(searchButton, Alignment.MIDDLE_RIGHT);

		return advancedSearchLayout;

	}

	public class SearchClickAction implements Action1<ClickEvent>
	{
		@Override
		public void call(final ClickEvent t1)
		{
			ContactView.super.triggerFilter();
		}
	}

	@Override
	public HelpPageIdentifier getHelpId()
	{
		return HelpPageIdentifier.ContactView;
	}
}
