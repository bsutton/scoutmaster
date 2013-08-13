package au.org.scoutmaster.views;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang.mutable.MutableBoolean;
import org.apache.log4j.Logger;
import org.vaadin.dialogs.ConfirmDialog;

import au.org.scoutmaster.application.Menu;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Gender;
import au.org.scoutmaster.domain.GroupRole;
import au.org.scoutmaster.domain.Note;
import au.org.scoutmaster.domain.PreferredCommunications;
import au.org.scoutmaster.domain.PreferredEmail;
import au.org.scoutmaster.domain.PreferredPhone;
import au.org.scoutmaster.domain.SectionType;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.filter.EntityManagerProvider;
import au.org.scoutmaster.util.FormHelper;
import au.org.scoutmaster.util.MultiColumnFormLayout;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Menu(display = "Contact")
public class ContactView extends VerticalLayout implements View, RowChangeListener<Contact>, Selected<Contact>
{
	private static Logger logger = Logger.getLogger(ContactView.class);
	private static final long serialVersionUID = 1L;

	public static final String NAME = "Contact";

	private boolean inNew = false;

	private TextField searchField = new TextField();
	private Button newButton = new Button("New");
	private Button deleteButton = new Button("Delete");
	private Button saveContactButton = new Button("Save");
	private Button cancelButton = new Button("Cancel");
	TabSheet tabs = new TabSheet();
	public BeanFieldGroup<Contact> fieldGroup = new BeanFieldGroup<>(Contact.class);

	private VerticalLayout mainEditPanel = new VerticalLayout();

	private MultiColumnFormLayout<Contact> overviewForm;

	private MultiColumnFormLayout<Contact> contactForm;

	private MultiColumnFormLayout<Contact> youthForm;

	private MultiColumnFormLayout<Contact> memberForm;

	private MultiColumnFormLayout<Contact> medicalForm;

	private MultiColumnFormLayout<Contact> background;

	private Contact currentContact;

	/*
	 * Any component can be bound to an external data source. This example uses
	 * just a dummy in-memory list, but there are many more practical
	 * implementations.
	 */
	private JPAContainer<Contact> contactContainer;

	private JPAContainer<Note> notes;

	public JPAContainer<Note> getNotes()
	{
		return notes;
	}

	/* User interface components are stored in session. */
	private ContactTable contactTable;
	private VerticalLayout rightLayout;

	@Override
	public void enter(ViewChangeEvent event)
	{
		contactContainer = JPAContainerFactory.make(Contact.class, EntityManagerProvider.INSTANCE.getEntityManager());
		contactContainer.addNestedContainerProperty("homePhone.phoneNo");
		contactContainer.addNestedContainerProperty("homePhone.primaryPhone");
		contactContainer.addNestedContainerProperty("homePhone.locationType");
		contactContainer.addNestedContainerProperty("homePhone.phoneType");

		contactContainer.addNestedContainerProperty("workPhone.phoneNo");
		contactContainer.addNestedContainerProperty("workPhone.primaryPhone");
		contactContainer.addNestedContainerProperty("workPhone.locationType");
		contactContainer.addNestedContainerProperty("workPhone.phoneType");

		contactContainer.addNestedContainerProperty("mobile.phoneNo");
		contactContainer.addNestedContainerProperty("mobile.primaryPhone");
		contactContainer.addNestedContainerProperty("mobile.locationType");
		contactContainer.addNestedContainerProperty("mobile.phoneType");

		contactContainer.addNestedContainerProperty("address.street");
		contactContainer.addNestedContainerProperty("address.city");
		contactContainer.addNestedContainerProperty("address.postcode");
		contactContainer.addNestedContainerProperty("address.state");

		contactTable = new ContactTable(contactContainer, new String[]
				{ Contact.FIRSTNAME, Contact.LASTNAME, Contact.SECTION });
		contactTable.setRowChangeListener(this);

		initLayout();
		initEditor();
		contactTable.init();
		initSearch();
		initButtons();
		this.setVisible(true);
	}

	/*
	 * build the button layout aned editor panel
	 */

	private void initLayout()
	{
		this.setSizeFull();

		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		this.addComponent(splitPanel);
		this.setExpandRatio(splitPanel, 1);

		// Layout for the table
		VerticalLayout leftLayout = new VerticalLayout();

		// Start by defining the LHS which contains the table
		splitPanel.addComponent(leftLayout);
		HorizontalLayout bottomLeftLayout = new HorizontalLayout();
		leftLayout.addComponent(bottomLeftLayout);
		bottomLeftLayout.addComponent(searchField);
		bottomLeftLayout.addComponent(newButton);
		leftLayout.addComponent(contactTable);
		leftLayout.setSizeFull();

		/*
		 * On the left side, expand the size of the contactList so that it uses
		 * all the space left after from bottomLeftLayout
		 */
		leftLayout.setExpandRatio(contactTable, 1);
		contactTable.setSizeFull();

		/*
		 * In the bottomLeftLayout, searchField takes all the width there is
		 * after adding addNewContactButton. The height of the layout is defined
		 * by the tallest component.
		 */
		bottomLeftLayout.setWidth("100%");
		searchField.setWidth("100%");
		bottomLeftLayout.setExpandRatio(searchField, 1);

		// Now define the edit area
		rightLayout = new VerticalLayout();
		splitPanel.addComponent(rightLayout);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth("100%");
		buttonLayout.addComponent(saveContactButton);
		buttonLayout.addComponent(cancelButton);
		buttonLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
		buttonLayout.setComponentAlignment(saveContactButton, Alignment.MIDDLE_LEFT);

		/* Put a little margin around the fields in the right side editor */
		Panel scroll = new Panel();
		mainEditPanel.setMargin(true);
		mainEditPanel.setVisible(true);
		scroll.setContent(mainEditPanel);

		// Delete button
		HorizontalLayout deleteLayout = new HorizontalLayout();
		deleteLayout.setWidth("100%");
		deleteLayout.addComponent(deleteButton);
		deleteLayout.setComponentAlignment(deleteButton, Alignment.MIDDLE_RIGHT);
		deleteLayout.setHeight("40px");

		rightLayout.addComponent(buttonLayout);
		rightLayout.addComponent(scroll);
		rightLayout.setExpandRatio(scroll, 1);
		rightLayout.addComponent(deleteLayout);

		rightLayout.setVisible(false);
	}

	private void initButtons()
	{
		newButton.addClickListener(new ClickListener()
		{
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event)
			{
				try
				{
					/*
					 * Rows in the Container data model are called Item. Here we
					 * add a new row in the beginning of the list.
					 */
					if (allowRowChange())
					{
						contactContainer.removeAllContainerFilters();
						inNew = true;
						rowChanged(new Contact());
						tabs.setSelectedTab(overviewForm);
						rightLayout.setVisible(true);
					}
				}
				catch (ConstraintViolationException e)
				{
					FormHelper.showConstraintViolation(e);
				}
			}

		});

		deleteButton.addClickListener(new ClickListener()
		{
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event)
			{
				Object contactId = contactTable.getValue();
				if (contactId != null)
				{
					Contact contact = contactContainer.getItem(contactId).getEntity();
					ConfirmDialog.show(UI.getCurrent(), "Delete Contact",
							"Are you sure you want to delete " + contact.toString(), "Delete", "Cancel",
							new ConfirmDialog.Listener()
							{
								private static final long serialVersionUID = 1L;

								@Override
								public void onClose(ConfirmDialog dialog)
								{
									if (dialog.isConfirmed())
									{
										Object contactId = contactTable.getValue();
										contactTable.removeItem(contactId);
										ContactView.this.currentContact = null;
										inNew = false;
									}
								}
							});
				}
			}
		});

		cancelButton.addClickListener(new ClickListener()
		{
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event)
			{
				fieldGroup.discard();
				if (inNew)
				{
					ContactView.this.contactTable.select(null);
				}

				inNew = false;
				Notification.show("Changes discarded.", "Any changes you have made to this contact been discarded.",
						Type.TRAY_NOTIFICATION);
			}
		});

		saveContactButton.addClickListener(new ClickListener()
		{
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event)
			{
				try
				{
					commit();

					if (inNew)
					{
						contactContainer.addEntity(ContactView.this.currentContact);
						inNew = false;
					}
				}
				catch (ConstraintViolationException e)
				{
					FormHelper.showConstraintViolation(e);
				}

			}
		});

	}

	private void initEditor()
	{

		// Overview taba
		overviewForm = new MultiColumnFormLayout<Contact>(2, this.fieldGroup);
		overviewForm.setSizeFull();
		tabs.addTab(overviewForm, "Overview");
		overviewForm.setMargin(true);
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
		DateField birthDate = overviewForm.bindDateField("Birth Date", Contact.BIRTH_DATE);

		final Label labelAge = overviewForm.bindLabelField("Age", "age");
		overviewForm.bindEnumField("Gender", "gender", Gender.class);
		overviewForm.newLine();


		// Contact tab
		contactForm = new MultiColumnFormLayout<Contact>(2, this.fieldGroup);
		contactForm.setSizeFull();
		tabs.addTab(contactForm, "Contact");
		contactForm.setMargin(true);
		
		contactForm.colspan(2);
		contactForm.bindEnumField("Preferred Communications", "preferredCommunications", PreferredCommunications.class);
		contactForm.newLine();
		contactForm.colspan(2);
		contactForm.bindTextField("Home Email", "homeEmail");
		contactForm.newLine();
		contactForm.colspan(2);
		contactForm.bindTextField("Work Email", "workEmail");
		contactForm.newLine();
		contactForm.bindTextField("Home Phone", "homePhone.phoneNo");
		contactForm.bindBooleanField("Primary", "homePhone.primaryPhone");
		contactForm.bindTextField("Work Phone", "workPhone.phoneNo");
		contactForm.bindBooleanField("Primary", "workPhone.primaryPhone");
		contactForm.bindTextField("Mobile Phone", "mobile.phoneNo");
		contactForm.bindBooleanField("Primary", "mobile.primaryPhone");
		contactForm.colspan(2);
		contactForm.bindTextField("Street", "address.street");
		contactForm.newLine();
		contactForm.colspan(2);
		contactForm.bindTextField("City", "address.city");
		contactForm.newLine();
		contactForm.bindTextField("State", "address.state");
		contactForm.bindTextField("Postcode", "address.postcode");
		contactForm.newLine();
		contactForm.bindEnumField("Preferred Phone", "preferredPhone", PreferredPhone.class);
		contactForm.bindEnumField("Preferred Email", "preferredEmail", PreferredEmail.class);

		
		// Youth tab
		youthForm = new MultiColumnFormLayout<Contact>(2, this.fieldGroup);
		tabs.addTab(youthForm, "Youth");
		youthForm.setSizeFull();
		youthForm.setMargin(true);
		final Label labelSectionEligibity = youthForm.bindLabelField("Section Eligibility", "sectionEligibility");
		youthForm.newLine();
		youthForm.bindBooleanField("Custody Order", "custodyOrder");
		youthForm.newLine();
		youthForm.colspan(2);
		youthForm.bindTextAreaField("Custody Order Details", "custodyOrderDetails", 4);
		youthForm.colspan(2);
		youthForm.bindTextField("School", "school");


		// Member tab
		memberForm = new MultiColumnFormLayout<Contact>(1, this.fieldGroup);
		tabs.addTab(memberForm, "Member");
		memberForm.setMargin(true);
		memberForm.setSizeFull();
		memberForm.bindBooleanField("Member", "isMember");
		memberForm.bindEntityField("Section ", "section", "name", SectionType.class);
		memberForm.bindTextField("Member No", "memberNo");
		memberForm.bindDateField("Member Since", "memberSince");


		// Medical Tab
		medicalForm = new MultiColumnFormLayout<Contact>(1, this.fieldGroup);
		tabs.addTab(medicalForm, "Medical");
		medicalForm.setMargin(true);
		medicalForm.setSizeFull();
		medicalForm.bindTextField("Allergies", "allergies");
		medicalForm.bindBooleanField("Ambulance Subscriber", "ambulanceSubscriber");
		medicalForm.bindBooleanField("Private Medical Ins.", "privateMedicalInsurance");
		medicalForm.bindTextField("Private Medical Fund", "privateMedicalFundName");

		// Background tab
		background = new MultiColumnFormLayout<Contact>(1, this.fieldGroup);
		tabs.addTab(background, "Background");
		background.setMargin(true);
		background.setSizeFull();

		background.bindTextField("Hobbies", "hobbies");
		background.bindDateField("Affiliated Since", "affiliatedSince");
		background.bindTextField("Current Employer", "currentEmployer");
		background.bindTextField("Job Title", "jobTitle");
//		background.bindTextField("Assets", "assets");
		background.bindBooleanField("Has WWC", "hasWWC");
		background.bindDateField("WWC Expiry", "wwcExpiry");
		background.bindTextField("WWC No.", "wwcNo");
		background.bindBooleanField("Has Police Check", "hasPoliceCheck");
		background.bindDateField("Police Check Expiry", "policeCheckExpiry");
		background.bindBooleanField("Has Food Handling", "hasFoodHandlingCertificate");
		background.bindBooleanField("Has First Aid Certificate", "hasFirstAidCertificate");

		tabs.setSizeFull();

		mainEditPanel.addComponent(tabs);
		mainEditPanel.setExpandRatio(tabs, (float) 1.0);

		// When a persons birth date changes recalculate their age.
		birthDate.addValueChangeListener(new Property.ValueChangeListener()
		{
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event)
			{
				DateField birthDate = (DateField) event.getProperty();
				ContactDao daoContact = new ContactDao();
				labelAge.setValue(daoContact.getAge(birthDate.getValue()).toString());
				labelSectionEligibity.setValue(daoContact.getSectionEligibilty(birthDate.getValue()).toString());
			}
		});

		role.addValueChangeListener(new Property.ValueChangeListener()
		{
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event)
			{
//				switch ((GroupRole) role.getValue())
//				{
//					case AdultHelper:
//					case Volunteer:
//					case QuarterMaster:
//					case CommitteeMember:
//						showAdult(true);
//						showYouth(false);
//						showMember(false);
//						showAffiliate(true);
//						showAffiliateAdult(true);
//						break;
//
//					case Gardian:
//					case Parent:
//						showAdult(true);
//						showYouth(false);
//						showMember(false);
//						showAffiliate(true);
//						showAffiliateAdult(true);
//						break;
//					case AssistantLeader:
//					case Leader:
//					case GroupLeader:
//					case President:
//					case Secretary:
//					case Treasurer:
//						showAdult(true);
//						showYouth(false);
//						showMember(true);
//						showAffiliate(true);
//						showAffiliateAdult(true);
//						break;
//					case YouthMember:
//						showAdult(false);
//						showYouth(true);
//						showMember(true);
//						showAffiliate(true);
//						showAffiliateAdult(false);
//						break;
//					default:
//						break;
//				}
				ContactDao daoContact = new ContactDao();
				labelAge.setValue(daoContact.getAge(ContactView.this.currentContact).toString());
			}

			private void showAffiliateAdult(boolean visible)
			{
				showFieldSection(background, visible);
			}

			private void showAffiliate(boolean visible)
			{
				showFieldSection(medicalForm, visible);
			}

			private void showMember(boolean visible)
			{
				showFieldSection(memberForm, visible);
			}

			private void showYouth(boolean visible)
			{
				showFieldSection(youthForm, visible);
			}

			private void showAdult(boolean visible)
			{
				showFieldSection(contactForm, visible);

			}

			private void showFieldSection(MultiColumnFormLayout<Contact> fieldLayout, boolean visible)
			{
				for (AbstractComponent field : fieldLayout.getFieldList())
				{
					field.setVisible(visible);
				}
			}
		});

		/*
		 * Data can be buffered in the user interface. When doing so, commit()
		 * writes the changes to the data source. Here we choose to write the
		 * changes automatically without calling commit().
		 */
		fieldGroup.setBuffered(true);
		// fieldGroup.setItemDataSource((Contact)null);

	}

	private void initSearch()
	{

		/*
		 * We want to show a subtle prompt in the search field. We could also
		 * set a caption that would be shown above the field or description to
		 * be shown in a tooltip.
		 */
		searchField.setInputPrompt("Search contacts");

		/*
		 * Granularity for sending events over the wire can be controlled. By
		 * default simple changes like writing a text in TextField are sent to
		 * server with the next Ajax call. You can set your component to be
		 * immediate to send the changes to server immediately after focus
		 * leaves the field. Here we choose to send the text over the wire as
		 * soon as user stops writing for a moment.
		 */
		searchField.setTextChangeEventMode(TextChangeEventMode.LAZY);

		/*
		 * When the event happens, we handle it in the anonymous inner class.
		 * You may choose to use separate controllers (in MVC) or presenters (in
		 * MVP) instead. In the end, the preferred application architecture is
		 * up to you.
		 */
		searchField.addTextChangeListener(new TextChangeListener()
		{
			/**
                         *
                         */
			private static final long serialVersionUID = 1L;

			public void textChange(final TextChangeEvent event)
			{

				/* Reset the filter for the contactContainer. */
				contactContainer.removeAllContainerFilters();
				contactContainer.addContainerFilter(new Or(new SimpleStringFilter(Contact.FIRSTNAME, event.getText(),
						true, false), new SimpleStringFilter(Contact.LASTNAME, event.getText(), true, false)));
			}
		});
	}

	@Override
	/** Called when the currently selected row in the 
	 *  table part of this view has changed.
	 *  We use this to update the editor's current item.
	 */
	public boolean allowRowChange()
	{
		final MutableBoolean allowChange = new MutableBoolean(false);

		if (fieldGroup.isModified() || inNew)
		{
			ConfirmDialog
					.show(UI.getCurrent(),
							"Discard changes?",
							"You have unsaved changes for this Contact. Continuing will result in those changes being discarded. ",
							"Continue", "Cancel", new ConfirmDialog.Listener()
							{
								private static final long serialVersionUID = 1L;

								public void onClose(ConfirmDialog dialog)
								{
									if (dialog.isConfirmed())
									{
										/*
										 * When a contact is selected from the
										 * list, we want to show that in our
										 * editor on the right. This is nicely
										 * done by the FieldGroup that binds all
										 * the fields to the corresponding
										 * Properties in our contact at once.
										 */
										fieldGroup.discard();
										inNew = false;
										allowChange.setValue(true);
									}
									else
									{
										// User did not confirm so don't allow
										// the change.

									}
								}
							});
		}
		else
		{
			allowChange.setValue(true);
		}
		return allowChange.toBoolean();

	}

	@Override
	/** Called when the currently selected row in the 
	 *  table part of this view has changed.
	 *  We use this to update the editor's current item.
	 */
	public void rowChanged(Contact contact)
	{
		this.currentContact = contact;

		// The contact is null if the row is de-selected
		if (contact != null)
		{
			// When selecting the groups data source we need to wrap it in a
			// BeanItem
			// to support bean validation and sub-entities.
			BeanItem<Contact> beanItem = new BeanItem<Contact>(contact);
			beanItem.addNestedProperty("homePhone.phoneNo");
			beanItem.addNestedProperty("homePhone.primaryPhone");
			beanItem.addNestedProperty("homePhone.locationType");
			beanItem.addNestedProperty("homePhone.phoneType");

			beanItem.addNestedProperty("workPhone.phoneNo");
			beanItem.addNestedProperty("workPhone.primaryPhone");
			beanItem.addNestedProperty("workPhone.locationType");
			beanItem.addNestedProperty("workPhone.phoneType");

			beanItem.addNestedProperty("mobile.phoneNo");
			beanItem.addNestedProperty("mobile.primaryPhone");
			beanItem.addNestedProperty("mobile.locationType");
			beanItem.addNestedProperty("mobile.phoneType");

			beanItem.addNestedProperty("address.street");
			beanItem.addNestedProperty("address.city");
			beanItem.addNestedProperty("address.postcode");
			beanItem.addNestedProperty("address.state");
			fieldGroup.setItemDataSource(beanItem);
		}

		rightLayout.setVisible(contact != null);

	}

	protected void commit()
	{
		try
		{
			if (!fieldGroup.isValid())
			{
				Notification.show("Validation Errors", "Please fix any field errors and try again.",
						Type.WARNING_MESSAGE);
			}
			else
			{
				fieldGroup.commit();
				
				ContactDao daoContact = new ContactDao();
				contactContainer.
				daoContact.merge(currentContact);
				
				Notification.show("Changes Saved", "Any changes you have made to this Contact have been saved.",
						Type.TRAY_NOTIFICATION);
			}
		}
		catch (CommitException e)
		{
			Notification.show("Error saving changes.",
					"Any error occured attempting to save your changes: " + e.getMessage(), Type.ERROR_MESSAGE);
			logger.error(e, e);
		}

	}

	VerticalLayout getEmptyPanel()
	{
		VerticalLayout layout = new VerticalLayout();

		Label pleaseAdd = new Label(
				"Click the 'New' button to add new Contact or click an existing contact in the adjacent table to edit it.");
		layout.addComponent(pleaseAdd);
		layout.setComponentAlignment(pleaseAdd, Alignment.MIDDLE_CENTER);
		layout.setSizeFull();
		return layout;
	}

	@Override
	public Contact getCurrent()
	{
		return currentContact;
	}

}