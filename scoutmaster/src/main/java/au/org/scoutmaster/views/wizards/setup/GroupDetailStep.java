package au.org.scoutmaster.views.wizards.setup;

import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.OrganisationDao;
import au.org.scoutmaster.domain.Address;
import au.org.scoutmaster.domain.Organisation;
import au.org.scoutmaster.util.FormHelper;
import au.org.scoutmaster.util.MultiColumnFormLayout;
import au.org.scoutmaster.util.ValidatingFieldGroup;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class GroupDetailStep implements WizardStep, ClickListener
{
	private static Logger logger = Logger.getLogger(GroupDetailStep.class);
	private static final long serialVersionUID = 1L;
	private TextField groupName;
	private TextField street;
	private TextField city;
	private TextField state;
	private TextField postcode;
	private TextField phone;
	private boolean created = false;

	private ValidatingFieldGroup<Organisation> fieldGroup;
	private JPAContainer<Organisation> container;
	private OrganisationDao organisationDao;
	private Organisation organisation;

	private boolean isNew = false;

	public GroupDetailStep(SetupWizardView setupWizardView)
	{
		organisationDao = new DaoFactory().getOrganisationDao();
		container = organisationDao.makeJPAContainer();
	}

	@Override
	public String getCaption()
	{
		return "Group Details";
	}

	@Override
	public Component getContent()
	{
		fieldGroup = new ValidatingFieldGroup<Organisation>(Organisation.class);
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth("100%");
		layout.setMargin(true);
		// layout.setSizeFull();
		StringBuilder sb = new StringBuilder();
		sb.append("<h1>Please enter your Scout Group's details.</h1>");
		Label label = new Label(sb.toString());
		label.setContentMode(ContentMode.HTML);
		layout.addComponent(label);

		MultiColumnFormLayout<Organisation> form = new MultiColumnFormLayout<>(1, fieldGroup);
		form.setWidth("500px");
		// form.setSizeFull();
		layout.addComponent(form);
		layout.setComponentAlignment(form, Alignment.TOP_LEFT);

		TextField groupName = form.bindTextField("Scout Group Name", "name");
		form.bindTextField("Phone No.", "primaryPhone");
		form.bindTextField("Street", "location.street");
		form.bindTextField("City", "location.city");
		form.bindTextField("State", "location.state");
		form.bindTextField("Postcode", "location.postcode");

		groupName.addValidator(new StringLengthValidator("Scout Group Name", 6, 255, false));

		// Create login button
		Button saveButton;

		saveButton = new Button("Save", this);
		saveButton.setClickShortcut(KeyCode.ENTER);
		saveButton.addStyleName("default");
		layout.addComponent(saveButton);

		organisation = organisationDao.findOurScoutGroup();
		if (organisation == null)
		{
			isNew = true;
			organisation = new Organisation();
			organisation.setOurScoutGroup(true);
			organisation.setLocation(new Address());
			EntityItem<Organisation> entityItem = organisationDao.makeJPAContainer().createEntityItem(organisation);
			fieldGroup.setItemDataSource(entityItem);
		}
		else
		{
			isNew = false;
			Item item = container.getItem(organisation.getId());
			fieldGroup.setItemDataSource(item);
		}

		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		if (created == false)
			Notification.show("Saving your settings first.");
		return created;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	public TextField getGroupName()
	{
		return groupName;
	}

	public void setGroupName(TextField groupName)
	{
		this.groupName = groupName;
	}

	public TextField getStreet()
	{
		return street;
	}

	public void setStreet(TextField street)
	{
		this.street = street;
	}

	public TextField getCity()
	{
		return city;
	}

	public void setCity(TextField city)
	{
		this.city = city;
	}

	public TextField getState()
	{
		return state;
	}

	public void setState(TextField state)
	{
		this.state = state;
	}

	public TextField getPhone()
	{
		return phone;
	}

	public void setPhone(TextField phone)
	{
		this.phone = phone;
	}

	public TextField getPostcode()
	{
		return postcode;
	}

	public void setPostcode(TextField postcode)
	{
		this.postcode = postcode;
	}

	@Override
	public void buttonClick(ClickEvent event)
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
				if (isNew)
					container.addEntity(organisation);

				created = true;
				Notification.show("The organisation details have been saved.", Type.TRAY_NOTIFICATION);
			}
		}
		catch (ConstraintViolationException e)
		{
			FormHelper.showConstraintViolation(e);
		}
		catch (CommitException e)
		{
			logger.error(e, e);
			Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
		}

	}

}
