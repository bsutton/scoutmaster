package au.org.scoutmaster.views;

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
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.filter.EntityManagerProvider;
import au.org.scoutmaster.util.FormHelper;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
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
	

	private TextField searchField = new TextField();
	private Button addNewContactButton = new Button("New");
	private Button removeContactButton = new Button("Remove this contact");
	private Button saveContactButton = new Button("Save");
	private Button cancelContactButton = new Button("Cancel");
	public FieldGroup editorFields = new FieldGroup();
	public FormLayout layoutFields = new FormLayout();

	private VerticalLayout mainEditPanel = new VerticalLayout();

	private FormHelper commonHelp;

	private FormHelper adultHelp;

	private FormHelper youthHelp;

	private FormHelper memberHelp;

	private FormHelper affiliatedHelp;

	private FormHelper affiliateAdultHelper;


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
		private static final long serialVersionUID = 1L;

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

		commonHelp = new FormHelper(this.layoutFields, this.editorFields);

		commonHelp.bindBooleanField("Active", "active");
		final ComboBox role = commonHelp.bindEnumField("Role", "role", GroupRole.class);
		commonHelp.bindTextField("Prefix", "prefix");
		commonHelp.bindTextField("Firstname", Contact.FIRSTNAME);
		commonHelp.bindTextField("Middle name", "middlename");
		commonHelp.bindTextField("Lastname", Contact.LASTNAME);
		DateField birthDate = commonHelp.bindDateField("Birth Date", Contact.BIRTH_DATE);
		final Label labelAge = commonHelp.bindLabelField("Age", "age");
		commonHelp.bindEnumField("Gender", "gender", Gender.class);
		commonHelp.bindTokenField("Tags", "tag", Tag.class);

		// Adult fields

		adultHelp = new FormHelper(this.layoutFields, this.editorFields);
		adultHelp.bindTextField("Home Phone", "homePhone");
		adultHelp.bindTextField("Work Phone", "workPhone");
		adultHelp.bindTextField("Mobile", "mobile");
		adultHelp.bindEnumField("Preferred Phone", "preferredPhone", PreferredPhone.class);
		adultHelp.bindTextField("Home Email", "homeEmail");
		adultHelp.bindTextField("Work Email", "workEmail");
		adultHelp.bindEnumField("Preferred Email", "preferredEmail", PreferredEmail.class);
		adultHelp.bindEnumField("Preferred Communications", "preferredCommunications", PreferredCommunications.class);

		// adultLayout.setCaption("Adult");
		// adultLayout.setMargin(true);

		// mainEditPanel.addComponent(adultLayout);

		youthHelp = new FormHelper(this.layoutFields, this.editorFields);

		youthHelp.bindBooleanField("Custody Order", "custodyOrder");
		youthHelp.bindTextAreaField("Custody Order Details", "custodyOrderDetails", 4);
		youthHelp.bindTextField("School", "school");
		youthHelp.bindEntityField("Section Eligib.", "sectionEligibility", SectionType.class);
		// bindPanel(youthLayout,"Address", "address");
		// mainEditPanel.addComponent(youthLayout);

		memberHelp = new FormHelper(this.layoutFields, this.editorFields);

		memberHelp.bindBooleanField("Member", "isMember");
		memberHelp.bindTextField("Member No", "memberNo");
		memberHelp.bindDateField("Member Since", "memberSince");
		memberHelp.bindEntityField("Section ", "section", SectionType.class);
		// mainEditPanel.addComponent(memberLayout);

		affiliatedHelp = new FormHelper(this.layoutFields, this.editorFields);

		affiliatedHelp.bindTextField("Hobbies", "hobbies");
		affiliatedHelp.bindDateField("Affiliated Since", "affiliatedSince");
		affiliatedHelp.bindTextField("Allergies", "allergies");
		affiliatedHelp.bindBooleanField("Ambulance Subscriber", "ambulanceSubscriber");
		affiliatedHelp.bindBooleanField("Private Medical Ins.", "privateMedicalInsurance");
		affiliatedHelp.bindTextField("Private Medical Fund", "privateMedicalFundName");

		// Affiliated Adult fields
		// adultLayout.setCaption("Adult");
		// adultLayout.setMargin(true);

		affiliateAdultHelper = new FormHelper(this.layoutFields, this.editorFields);
		affiliateAdultHelper.bindTextField("Current Employer", "currentEmployer");
		affiliateAdultHelper.bindTextField("Job Title", "jobTitle");
		// bindTextField(affiliatedAdultLayout, "Assets", "assets");
		affiliateAdultHelper.bindBooleanField("Has WWC", "hasWWC");
		affiliateAdultHelper.bindDateField("WWC Expiry", "wwcExpiry");
		affiliateAdultHelper.bindTextField("WWC No.", "wwcNo");
		affiliateAdultHelper.bindBooleanField("Has Police Check", "hasPoliceCheck");
		affiliateAdultHelper.bindDateField("Police Check Expiry", "policeCheckExpiry");
		affiliateAdultHelper.bindBooleanField("Has Food Handling", "hasFoodHandlingCertificate");
		affiliateAdultHelper.bindBooleanField("Has First Aid Certificate", "hasFirstAidCertificate");

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
						showAdult(false);
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
				for (AbstractField<?> field : ContactView.this.affiliateAdultHelper.getFieldList())
				{
					field.setVisible(visible);
				}

			}

			private void showAffiliate(boolean visible)
			{
				for (AbstractField<?> field : ContactView.this.affiliatedHelp.getFieldList())
				{
					field.setVisible(visible);
				}

			}

			private void showMember(boolean visible)
			{
				for (AbstractField<?> field : ContactView.this.memberHelp.getFieldList())
				{
					field.setVisible(visible);
				}

			}

			private void showYouth(boolean visible)
			{
				for (AbstractField<?> field : ContactView.this.youthHelp.getFieldList())
				{
					field.setVisible(visible);
				}

			}

			private void showAdult(boolean visible)
			{
				for (AbstractField<?> field : ContactView.this.adultHelp.getFieldList())
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
}