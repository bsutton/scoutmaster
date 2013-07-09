package au.org.scoutmaster.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Gender;
import au.org.scoutmaster.domain.GroupRole;
import au.org.scoutmaster.domain.Note;
import au.org.scoutmaster.domain.PreferredCommunications;
import au.org.scoutmaster.domain.PreferredEmail;
import au.org.scoutmaster.domain.PreferredPhone;
import au.org.scoutmaster.domain.SectionType;
import au.org.scoutmaster.filter.EntityManagerProvider;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@Menu(display = "Contact")
public class ContactView extends VerticalLayout implements View
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME = "Contact";

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
	private Table contactList = new Table()
	{
		@Override
		protected String formatPropertyValue(Object rowId, Object colId, Property property)
		{
			if (property.getType() == Set.class)
			{
				return null;
			}
			try
			{
				property.getValue();
			}
			catch (Exception e)
			{
				return null;
			}
			return super.formatPropertyValue(rowId, colId, property);
		}
	};
	private TextField searchField = new TextField();
	private Button addNewContactButton = new Button("New");
	private Button removeContactButton = new Button("Remove this contact");
	private Button saveContactButton = new Button("Save");
	private Button cancelContactButton = new Button("Cancel");
	private FieldGroup editorFields = new FieldGroup();
	private FormLayout layoutFields = new FormLayout();

	private VerticalLayout mainEditPanel = new VerticalLayout();

	@Override
	public void enter(ViewChangeEvent event)
	{
		contactContainer = JPAContainerFactory.make(Contact.class, EntityManagerProvider.INSTANCE.getEntityManager());

		initLayout();
		initContactList();
		initEditor();
		initSearch();
		initAddRemoveButtons();
		this.setVisible(true);
	}

	/*
	 * In this example layouts are programmed in Java. You may choose use a
	 * visual editor, CSS or HTML templates for layout instead.
	 */
	private void initLayout()
	{
		this.setSizeFull();

		/* Root of the user interface component tree is set */
		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		this.addComponent(splitPanel);

		this.setExpandRatio(splitPanel, 1);

		/* Build the component tree */
		VerticalLayout leftLayout = new VerticalLayout();
		splitPanel.addComponent(leftLayout);
		splitPanel.addComponent(mainEditPanel);
		leftLayout.addComponent(contactList);
		HorizontalLayout bottomLeftLayout = new HorizontalLayout();
		leftLayout.addComponent(bottomLeftLayout);
		bottomLeftLayout.addComponent(searchField);
		bottomLeftLayout.addComponent(addNewContactButton);

		/* Set the contents in the left of the split panel to use all the space */
		leftLayout.setSizeFull();

		/*
		 * On the left side, expand the size of the contactList so that it uses
		 * all the space left after from bottomLeftLayout
		 */
		leftLayout.setExpandRatio(contactList, 1);
		contactList.setSizeFull();

		/*
		 * In the bottomLeftLayout, searchField takes all the width there is
		 * after adding addNewContactButton. The height of the layout is defined
		 * by the tallest component.
		 */
		bottomLeftLayout.setWidth("100%");
		searchField.setWidth("100%");
		bottomLeftLayout.setExpandRatio(searchField, 1);

		/* Put a little margin around the fields in the right side editor */
		// mainEditPanel.setMargin(true);
		mainEditPanel.setVisible(true);
	}

	private void initAddRemoveButtons()
	{
		addNewContactButton.addClickListener(new ClickListener()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event)
			{
				/*
				 * Rows in the Container data model are called Item. Here we add
				 * a new row in the beginning of the list.
				 */
				contactContainer.removeAllContainerFilters();
				Long contactid = (Long) contactContainer.addEntity(new Contact());

				/*
				 * Each Item has a set of Properties that hold values. Here we
				 * set a couple of those.
				 */
				// contactList.getContainerProperty(contactid,
				// FNAME).setValue("New");
				// contactList.getContainerProperty(contactid,
				// LNAME).setValue("Contact");
				// contactList.getContainerProperty(contactid,
				// BIRTH_DATE).setValue("2000/1/1");

				/* Lets choose the newly created contact to edit it. */
				contactList.select(contactid);
			}
		});

		removeContactButton.addClickListener(new ClickListener()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event)
			{
				Object contactId = contactList.getValue();
				contactList.removeItem(contactId);
			}
		});

		cancelContactButton.addClickListener(new ClickListener()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event)
			{
				editorFields.discard();
			}
		});

	}

	private void initEditor()
	{

		mainEditPanel.addComponent(removeContactButton);

		// Common fields
		ArrayList<AbstractField<?>> commonLayout = new ArrayList<AbstractField<?>>();
		// commonLayout.setCaption("Common");
		// commonLayout.setMargin(true);

		bindBooleanField(commonLayout, "Active", "active");
		final ComboBox role = bindEnumField(commonLayout, "Role", "role", GroupRole.class);
		bindTextField(commonLayout, "Prefix", "prefix");
		bindTextField(commonLayout, "Firstname", Contact.FIRSTNAME);
		bindTextField(commonLayout, "Middle name", "middlename");
		bindTextField(commonLayout, "Lastname", Contact.LASTNAME);
		DateField birthDate = bindDateField(commonLayout, "Birth Date", Contact.BIRTH_DATE);
		final Label labelAge = bindLabelField(commonLayout, "Age", "age");
		bindEnumField(commonLayout, "Gender", "gender", Gender.class);

		// Adult fields
		final ArrayList<AbstractField<?>> adultLayout = new ArrayList<AbstractField<?>>();
		// adultLayout.setCaption("Adult");
		// adultLayout.setMargin(true);

		bindTextField(adultLayout, "Home Phone", "homePhone");
		bindTextField(adultLayout, "Work Phone", "workPhone");
		bindTextField(adultLayout, "Mobile", "mobile");
		bindEnumField(adultLayout, "Preferred Phone", "preferredPhone", PreferredPhone.class);
		bindTextField(adultLayout, "Home Email", "homeEmail");
		bindTextField(adultLayout, "Work Email", "workEmail");
		bindEnumField(adultLayout, "Preferred Email", "preferredEmail", PreferredEmail.class);
		bindEnumField(adultLayout, "Preferred Communications", "preferredCommunications", PreferredCommunications.class);

		// mainEditPanel.addComponent(adultLayout);

		// Youth fields
		// FormLayout youthLayout = new FormLayout();
		final ArrayList<AbstractField<?>> youthLayout = new ArrayList<AbstractField<?>>();
		// youthLayout.setCaption("Youth");
		// youthLayout.setMargin(true);

		bindBooleanField(youthLayout, "Custody Order", "custodyOrder");
		bindTextAreaField(youthLayout, "Custody Order Details", "custodyOrderDetails", 4);
		bindTextField(youthLayout, "School", "school");
		bindEntityField(youthLayout, "Section Eligib.", "sectionEligibility", SectionType.class);
		// bindPanel(youthLayout,"Address", "address");
		// mainEditPanel.addComponent(youthLayout);

		// Member fields
		// FormLayout memberLayout = new FormLayout();
		final ArrayList<AbstractField<?>> memberLayout = new ArrayList<AbstractField<?>>();
		// memberLayout.setCaption("Member");
		// memberLayout.setMargin(true);

		bindBooleanField(memberLayout, "Member", "isMember");
		bindTextField(memberLayout, "Member No", "memberNo");
		bindDateField(memberLayout, "Member Since", "memberSince");
		bindEntityField(youthLayout, "Section ", "section", SectionType.class);
		// mainEditPanel.addComponent(memberLayout);

		// Affiliate fields
		final ArrayList<AbstractField<?>> affiliatedLayout = new ArrayList<AbstractField<?>>();
		// FormLayout affiliatedLayout = new FormLayout();
		// affiliatedLayout.setCaption("Affiliate");
		// affiliatedLayout.setMargin(true);

		bindTextField(affiliatedLayout, "Hobbies", "hobbies");
		bindDateField(affiliatedLayout, "Affiliated Since", "affiliatedSince");
		bindTextField(affiliatedLayout, "Allergies", "allergies");
		bindBooleanField(affiliatedLayout, "Ambulance Subscriber", "ambulanceSubscriber");
		bindBooleanField(affiliatedLayout, "Private Medical Ins.", "privateMedicalInsurance");
		bindTextField(affiliatedLayout, "Private Medical Fund", "privateMedicalFundName");

		// Affiliated Adult fields
		final ArrayList<AbstractField<?>> affiliatedAdultLayout = new ArrayList<AbstractField<?>>();
		// adultLayout.setCaption("Adult");
		// adultLayout.setMargin(true);

		bindTextField(affiliatedAdultLayout, "Current Employer", "currentEmployer");
		bindTextField(affiliatedAdultLayout, "Job Title", "jobTitle");
		// bindTextField(affiliatedAdultLayout, "Assets", "assets");
		bindBooleanField(affiliatedAdultLayout, "Has WWC", "hasWWC");
		bindDateField(affiliatedAdultLayout, "WWC Expiry", "wwcExpiry");
		bindTextField(affiliatedAdultLayout, "WWC No.", "wwcNo");
		bindBooleanField(affiliatedAdultLayout, "Has Police Check", "hasPoliceCheck");
		bindDateField(affiliatedAdultLayout, "Police Check Expiry", "policeCheckExpiry");
		bindBooleanField(affiliatedAdultLayout, "Has Food Handling", "hasFoodHandlingCertificate");
		bindBooleanField(affiliatedAdultLayout, "Has First Aid Certificate", "hasFirstAidCertificate");

		// mainEditPanel.addComponent(affiliatedLayout);

		mainEditPanel.addComponent(layoutFields);

		// Tags

		birthDate.addValueChangeListener(new Property.ValueChangeListener()
		{
			private static final long serialVersionUID = 1L;

			// not working?
			public void valueChange(ValueChangeEvent event)
			{
				Long contactId = (Long) contactList.getValue();

				ContactDao daoContact = new ContactDao();
				Contact contact = contactContainer.getItem(contactId).getEntity();
				
				labelAge.setValue(daoContact.getAge(contact).toString());

				/*
				 * When a contact is selected from the list, we want to show
				 * that in our editor on the right. This is nicely done by the
				 * FieldGroup that binds all the fields to the corresponding
				 * Properties in our contact at once.
				 */
				if (contactId != null)
					editorFields.setItemDataSource(contactList.getItem(contactId));

				mainEditPanel.setVisible(contactId != null);
			}
		});

		role.addValueChangeListener(new Property.ValueChangeListener()
		{
			private static final long serialVersionUID = 1L;

			// not working?
			public void valueChange(ValueChangeEvent event)
			{
				switch ((GroupRole) role.getValue())
				{
					case AdultHelper:
					case Volunteer:
					case QuarterMaster:
					case CommitteeMember:
						showAdult(true);
						showYouth(false);
						showMember(false);
						showAffiliate(true);
						showAffiliateAdult(true);
						break;

					case Gardian:
					case Parent:
						showAdult(true);
						showYouth(false);
						showMember(false);
						showAffiliate(true);
						showAffiliateAdult(true);
						break;
					case AssistantLeader:
					case Leader:
					case GroupLeader:
					case President:
					case Secretary:
					case Treasurer:
						showAdult(true);
						showYouth(false);
						showMember(true);
						showAffiliate(true);
						showAffiliateAdult(true);
						break;
					case YouthMember:
						showAdult(true);
						showYouth(true);
						showMember(true);
						showAffiliate(true);
						showAffiliateAdult(false);
						break;
					default:
						break;
				}
				Object contactId = contactList.getValue();

				ContactDao daoContact = new ContactDao();
				
				Contact contact = contactContainer.getItem(contactId).getEntity();
				labelAge.setValue(daoContact.getAge(contact).toString());

				/*
				 * When a contact is selected from the list, we want to show
				 * that in our editor on the right. This is nicely done by the
				 * FieldGroup that binds all the fields to the corresponding
				 * Properties in our contact at once.
				 */
				if (contactId != null)
					editorFields.setItemDataSource(contactList.getItem(contactId));

				mainEditPanel.setVisible(contactId != null);
			}

			private void showAffiliateAdult(boolean visible)
			{
				for (AbstractField<?> field : affiliatedAdultLayout)
				{
					field.setVisible(visible);
				}

			}

			private void showAffiliate(boolean visible)
			{
				for (AbstractField<?> field : affiliatedLayout)
				{
					field.setVisible(visible);
				}

			}

			private void showMember(boolean visible)
			{
				for (AbstractField<?> field : memberLayout)
				{
					field.setVisible(visible);
				}

			}

			private void showYouth(boolean visible)
			{
				for (AbstractField<?> field : youthLayout)
				{
					field.setVisible(visible);
				}

			}

			private void showAdult(boolean visible)
			{
				for (AbstractField<?> field : adultLayout)
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
		editorFields.setBuffered(false);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.addComponent(saveContactButton);
		buttonLayout.addComponent(cancelContactButton);
		mainEditPanel.addComponent(buttonLayout);
	}

	private ComboBox bindEntityField(ArrayList<AbstractField<?>> group, String fieldLabel, String fieldName,
			Class<?> clazz)
	{
		JPAContainer container = JPAContainerFactory.make(clazz, EntityManagerProvider.INSTANCE.getEntityManager());
		
		ComboBox field = new ComboBox(fieldLabel, container);
		field.setNewItemsAllowed(false);
		field.setNullSelectionAllowed(false);
		field.setTextInputAllowed(false);
		layoutFields.addComponent(field);
		field.setWidth("100%");
		field.setImmediate(true);
		editorFields.bind(field, fieldName);
		group.add(field);
		return field;
	}

	private ComboBox bindEnumField(ArrayList<AbstractField<?>> group, String fieldLabel, String fieldName,
			Class<?> clazz)
	{
		ComboBox field = new ComboBox(fieldLabel, createContainerFromEnumClass(fieldName, clazz));
		field.setNewItemsAllowed(false);
		field.setNullSelectionAllowed(false);
		field.setTextInputAllowed(false);
		layoutFields.addComponent(field);
		field.setWidth("100%");
		field.setImmediate(true);
		editorFields.bind(field, fieldName);
		group.add(field);
		return field;
	}

	private CheckBox bindBooleanField(ArrayList<AbstractField<?>> group, String fieldLabel, String fieldName)
	{
		CheckBox field = new CheckBox(fieldLabel);
		layoutFields.addComponent(field);
		field.setWidth("100%");
		field.setImmediate(true);
		editorFields.bind(field, fieldName);
		group.add(field);
		return field;

	}

	private Label bindLabelField(ArrayList<AbstractField<?>> group, String fieldLabel, String fieldName)
	{
		Label field = new Label(fieldLabel);
		layoutFields.addComponent(field);
		field.setWidth("100%");
		// group.add(field);
		return field;
	}

	private TextField bindTextField(ArrayList<AbstractField<?>> group, String fieldLabel, String fieldName)
	{
		TextField field = new TextField(fieldLabel);
		layoutFields.addComponent(field);
		field.setWidth("100%");
		field.setImmediate(true);
		editorFields.bind(field, fieldName);
		group.add(field);
		return field;
	}

	private TextArea bindTextAreaField(ArrayList<AbstractField<?>> group, String fieldLabel, String fieldName, int rows)
	{
		TextArea field = new TextArea(fieldLabel);
		field.setRows(rows);
		layoutFields.addComponent(field);
		field.setWidth("100%");
		field.setImmediate(true);
		editorFields.bind(field, fieldName);
		group.add(field);
		return field;

	}

	private DateField bindDateField(ArrayList<AbstractField<?>> group, String fieldLabel, String fieldName)
	{
		DateField field = new DateField(fieldLabel);
		field.setDateFormat("yyyy-MM-dd");

		layoutFields.addComponent(field);
		field.setImmediate(true);
		field.setWidth("100%");
		editorFields.bind(field, fieldName);
		group.add(field);
		return field;
	}

	private void initContactList()
	{
		contactList.setContainerDataSource(contactContainer);
		contactList.setVisibleColumns((Object[]) new String[]
		// { Contact.FIRSTNAME, Contact.LASTNAME, Contact.ROLE, Contact.SECTION
		// });
				{ Contact.FIRSTNAME, Contact.LASTNAME, Contact.SECTION });

		contactList.setSelectable(true);
		contactList.setImmediate(true);

		contactList.addValueChangeListener(new Property.ValueChangeListener()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event)
			{
				Object contactId = contactList.getValue();

				/*
				 * When a contact is selected from the list, we want to show
				 * that in our editor on the right. This is nicely done by the
				 * FieldGroup that binds all the fields to the corresponding
				 * Properties in our contact at once.
				 */
				if (contactId != null)
					editorFields.setItemDataSource(contactList.getItem(contactId));

				mainEditPanel.setVisible(contactId != null);
			}
		});
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
				contactContainer.addContainerFilter(new ContactFilter(event.getText()));
			}
		});
	}

	/*
	 * A custom filter for searching names and companies in the
	 * contactContainer.
	 */
	private class ContactFilter implements Filter
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String needle;

		public ContactFilter(String needle)
		{
			this.needle = needle.toLowerCase();
		}

		public boolean passesFilter(Object itemId, Item item)
		{
			String haystack = ("" + ((String) item.getItemProperty(Contact.FIRSTNAME).getValue()).toLowerCase()
					+ ((String) item.getItemProperty(Contact.LASTNAME).getValue()).toLowerCase()
					+ item.getItemProperty(Contact.SECTION).getValue() + item.getItemProperty(Contact.ROLE).getValue());
			return haystack.contains(needle);
		}

		public boolean appliesToProperty(Object id)
		{
			return true;
		}
	}

	public Container createContainerFromEnumClass(String fieldName, Class<?> clazz)
	{
		LinkedHashMap<Enum<?>, String> enumMap = new LinkedHashMap<Enum<?>, String>();
		for (Object enumConstant : clazz.getEnumConstants())
		{
			enumMap.put((Enum<?>) enumConstant, enumConstant.toString());
		}

		return createContainerFromMap(fieldName, enumMap);
	}

	@SuppressWarnings("unchecked")
	public Container createContainerFromMap(String fieldName, Map<?, String> hashMap)
	{
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(fieldName, String.class, "");

		Iterator<?> iter = hashMap.keySet().iterator();
		while (iter.hasNext())
		{
			Object itemId = iter.next();
			container.addItem(itemId);
			container.getItem(itemId).getItemProperty(fieldName).setValue(hashMap.get(itemId));
		}

		return container;
	}

	// use it this way: createContainerFromEnumClass(MyEnum.class)

}
