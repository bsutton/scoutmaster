package au.org.scoutmaster.views;

import org.apache.commons.lang.mutable.MutableBoolean;
import org.apache.log4j.Logger;
import org.vaadin.dialogs.ConfirmDialog;

import au.org.scoutmaster.domain.BaseEntity;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class BaseCrudView<T extends BaseEntity> extends HorizontalSplitPanel implements View, RowChangeListener<T>
{
	private static Logger logger = Logger.getLogger(BaseCrudView.class);
	private static final long serialVersionUID = 1L;

	private TextField searchField = new TextField();
	private Button addNewContactButton = new Button("New");
	private Button deleteContactButton = new Button("Delete this contact");
	private Button saveContactButton = new Button("Save");
	private Button cancelContactButton = new Button("Cancel");
	public BeanFieldGroup<T> fieldGroup;
	public FormLayout layoutFields = new FormLayout();

	private JPAContainer<T> container;

	/* User interface components are stored in session. */
	private ContactTable contactTable;
	private VerticalLayout rightLayout;
	private VerticalLayout leftLayout;

	@Override
	public void enter(ViewChangeEvent event)
	{
		initLayout();
		initEditor();
		contactTable.init();
		initSearch();
		initAddRemoveButtons();
		this.setVisible(true);
	}

	private void initLayout()
	{
		this.setSizeFull();

		leftLayout = new VerticalLayout();
		rightLayout = new VerticalLayout();

		// Start by defining the LHS which contains the table
		this.addComponent(leftLayout);
		this.addComponent(rightLayout);
		
		initLeftLayout();
		initRightLayout();

//		scroll.setSizeFull();

	}

	private void initRightLayout()
	{
//		HorizontalLayout buttonLayout = new HorizontalLayout();
//		buttonLayout.setWidth("100%");
//		buttonLayout.addComponent(saveContactButton);
//		buttonLayout.addComponent(cancelContactButton);
//		buttonLayout.setComponentAlignment(cancelContactButton, Alignment.MIDDLE_RIGHT);
//		buttonLayout.setComponentAlignment(saveContactButton, Alignment.MIDDLE_LEFT);
//	//	buttonLayout.setHeight("40px");
//
//		/* Put a little margin around the fields in the right side editor */
//		Panel scroll = new Panel();
//		mainEditPanel.setMargin(true);
//		mainEditPanel.setVisible(true);
//		//mainEditPanel.setSizeUndefined();
//		scroll.setContent(mainEditPanel);
//		//scroll.setSizeFull();
//		
//		// Delete button
//		HorizontalLayout deleteLayout = new HorizontalLayout();
//		deleteLayout.setWidth("100%");
//		deleteLayout.addComponent(deleteContactButton);
//		deleteLayout.setComponentAlignment(deleteContactButton, Alignment.MIDDLE_RIGHT);
//		deleteLayout.setHeight("40px");
//
//		rightLayout.addComponent(buttonLayout);
//		rightLayout.addComponent(scroll);
//		rightLayout.setExpandRatio(scroll, 1);
//		rightLayout.addComponent(deleteLayout);
//
//		rightLayout.setVisible(false);
		
	}

	private void initLeftLayout()
	{
		HorizontalLayout bottomLeftLayout = new HorizontalLayout();
		leftLayout.addComponent(bottomLeftLayout);
		bottomLeftLayout.addComponent(searchField);
		bottomLeftLayout.addComponent(addNewContactButton);
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
		
	}

	private void initAddRemoveButtons()
	{
//		addNewContactButton.addClickListener(new ClickListener()
//		{
//			private static final long serialVersionUID = 1L;
//
//			public void buttonClick(ClickEvent event)
//			{
//				/*
//				 * Rows in the Container data model are called Item. Here we add
//				 * a new row in the beginning of the list.
//				 */
//				contactContainer.removeAllContainerFilters();
//				Long contactid = (Long) contactContainer.addEntity(new Contact());
//
//				/* Lets choose the newly created contact to edit it. */
//				contactTable.select(contactid);
//			}
//		});

		deleteContactButton.addClickListener(new ClickListener()
		{
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event)
			{
//				Object contactId = contactTable.getValue();
//				if (contactId != null)
//				{
//					Contact contact = contactContainer.getItem(contactId).getEntity();
//					ConfirmDialog.show(UI.getCurrent(), "Delete Contact",
//							"Are you sure you want to delete " + contact.toString(), "Delete", "Cancel",
//							new ConfirmDialog.Listener()
//							{
//								private static final long serialVersionUID = 1L;
//
//								@Override
//								public void onClose(ConfirmDialog dialog)
//								{
//									if (dialog.isConfirmed())
//									{
//										Object contactId = contactTable.getValue();
//										contactTable.removeItem(contactId);
//									}
//								}
//							});
//				}
			}
		});

		cancelContactButton.addClickListener(new ClickListener()
		{
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event)
			{
				fieldGroup.discard();
				Notification.show("Changes discarded.", "Any changes you have made to this contact been discarded.",
						Type.TRAY_NOTIFICATION);
			}
		});

		saveContactButton.addClickListener(new ClickListener()
		{
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event)
			{
				commit();

			}
		});

	}

	private void initEditor()
	{
/*

		commonHelp = new FormHelper(this.layoutFields, this.fieldGroup);
		adultHelp = new FormHelper(this.layoutFields, this.fieldGroup);
		youthHelp = new FormHelper(this.layoutFields, this.fieldGroup);
		memberHelp = new FormHelper(this.layoutFields, this.fieldGroup);
		affiliatedHelp = new FormHelper(this.layoutFields, this.fieldGroup);
		affiliateAdultHelper = new FormHelper(this.layoutFields, this.fieldGroup);

		commonHelp.bindBooleanField("Active", "active");
		final ComboBox role = commonHelp.bindEnumField("Role", "role", GroupRole.class);
		commonHelp.bindTextField("Firstname", Contact.FIRSTNAME);
		commonHelp.bindTextField("Middle name", "middlename");
		commonHelp.bindTextField("Lastname", Contact.LASTNAME);
		DateField birthDate = commonHelp.bindDateField("Birth Date", Contact.BIRTH_DATE);
		
		final Label labelAge = commonHelp.bindLabelField("Age", "age");
		final Label labelSectionEligibity = commonHelp.bindLabelField("Section Eligibility", "sectionEligibility");
		
		youthHelp.bindEntityField("Section Eligib.", "sectionEligibility", SectionType.class);
		memberHelp.bindEntityField("Section ", "section", SectionType.class);
		commonHelp.bindEnumField("Gender", "gender", Gender.class);
		commonHelp.bindTokenField(contactTable, "Tags", "tags", Tag.class);

		// Adult fields

		adultHelp.bindTextField("Home Phone", "homePhone");
		adultHelp.bindTextField("Work Phone", "workPhone");
		adultHelp.bindTextField("Mobile", "mobile");
		adultHelp.bindEnumField("Preferred Phone", "preferredPhone", PreferredPhone.class);
		adultHelp.bindTextField("Home Email", "homeEmail");
		adultHelp.bindTextField("Work Email", "workEmail");
		adultHelp.bindEnumField("Preferred Email", "preferredEmail", PreferredEmail.class);
		adultHelp.bindEnumField("Preferred Communications", "preferredCommunications", PreferredCommunications.class);

		youthHelp.bindBooleanField("Custody Order", "custodyOrder");
		youthHelp.bindTextAreaField("Custody Order Details", "custodyOrderDetails", 4);
		youthHelp.bindTextField("School", "school");
		// bindPanel(youthLayout,"Address", "address");

		memberHelp.bindBooleanField("Member", "isMember");
		memberHelp.bindTextField("Member No", "memberNo");
		memberHelp.bindDateField("Member Since", "memberSince");

		affiliatedHelp.bindTextField("Hobbies", "hobbies");
		affiliatedHelp.bindDateField("Affiliated Since", "affiliatedSince");
		affiliatedHelp.bindTextField("Allergies", "allergies");
		affiliatedHelp.bindBooleanField("Ambulance Subscriber", "ambulanceSubscriber");
		affiliatedHelp.bindBooleanField("Private Medical Ins.", "privateMedicalInsurance");
		affiliatedHelp.bindTextField("Private Medical Fund", "privateMedicalFundName");

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


		mainEditPanel.addComponent(layoutFields);

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

				Object contactId = contactTable.getValue();

				ContactDao daoContact = new ContactDao();

				Contact contact = contactContainer.getItem(contactId).getEntity();
				labelAge.setValue(daoContact.getAge(contact).toString());
			}

			private void showAffiliateAdult(boolean visible)
			{
				showFieldSection(affiliateAdultHelper, visible);
			}

			private void showAffiliate(boolean visible)
			{
				showFieldSection(affiliatedHelp, visible);
			}

			private void showMember(boolean visible)
			{
				showFieldSection(memberHelp, visible);
			}

			private void showYouth(boolean visible)
			{
				showFieldSection(youthHelp, visible);
			}

			private void showAdult(boolean visible)
			{
				showFieldSection(adultHelp, visible);

			}

			private void showFieldSection(FormHelper fieldLayout, boolean visible)
			{
				for (AbstractField<?> field : fieldLayout.getFieldList())
				{
					field.setVisible(visible);
				}
			}
		});

*/
		/*
		 * Data can be buffered in the user interface. When doing so, commit()
		 * writes the changes to the data source. Here we choose to write the
		 * changes automatically without calling commit().
		 */
		fieldGroup.setBuffered(true);
		//fieldGroup.setItemDataSource(null);
		

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
				/*
				contactContainer.removeAllContainerFilters();
				contactContainer.addContainerFilter(new Or(new SimpleStringFilter(Contact.FIRSTNAME, event.getText(), true, false),
						new SimpleStringFilter(Contact.LASTNAME, event.getText(), true, false)));
						*/
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

		if (fieldGroup.isModified())
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
	//public void rowChanged(final T contact)
	public void rowChanged(final T contact)
	{
		fieldGroup.setItemDataSource(contact);
		rightLayout.setVisible(contact != null);

	}

	protected void commit()
	{
		try
		{

			fieldGroup.commit();
			Notification.show("Changes Saved", "Any changes you have made to this Contact have been saved.",
					Type.TRAY_NOTIFICATION);
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
		
		Label pleaseAdd = new Label("Click the 'New' button to add new Contact or click an existing contact in the adjacent table to edit it.");
		layout.addComponent(pleaseAdd);
		layout.setComponentAlignment(pleaseAdd, Alignment.MIDDLE_CENTER);
		layout.setSizeFull();
		return layout;
	}


}