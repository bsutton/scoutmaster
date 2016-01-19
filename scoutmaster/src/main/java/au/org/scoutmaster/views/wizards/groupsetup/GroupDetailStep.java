package au.org.scoutmaster.views.wizards.groupsetup;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;
import org.xml.sax.SAXException;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.GroupDao;
import au.org.scoutmaster.domain.Group;
import au.org.scoutmaster.domain.GroupRole;
import au.org.scoutmaster.domain.GroupType;
import au.org.scoutmaster.domain.SectionType;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.ui.SimpleFormLayout;
import au.org.scoutmaster.util.SMNotification;
import au.org.scoutmaster.xml.GroupSetup;

public class GroupDetailStep implements WizardStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(GroupDetailStep.class);
	private SimpleFormLayout form;
	private TextField groupName;
	private TextField phoneNo;
	private TextField street;
	private TextField city;
	private TextField state;
	private TextField postcode;
	private ComboBox countries;
	private ComboBox groupTypeField;

	public GroupDetailStep(final GroupSetupWizardView setupWizardView)
	{
	}

	@Override
	public String getCaption()
	{
		return "Group Details";
	}

	@Override
	public Component getContent()
	{
		if (form == null)
		{
			form = new SimpleFormLayout();
			form.setMargin(true);

			final Label label = new Label("<h1>Please enter your Group's details.</h1>", ContentMode.HTML);
			label.setContentMode(ContentMode.HTML);

			form.addComponent(label);

			groupName = new TextField("Group Name");
			form.addComponent(groupName);

			groupTypeField = new ComboBox("Group Type");
			groupTypeField.setItemCaptionMode(ItemCaptionMode.ID_TOSTRING);
			groupTypeField.setTextInputAllowed(false);
			groupTypeField.setRequired(true);
			loadGroupTypes();
			form.addComponent(groupTypeField);

			phoneNo = new TextField("Phone No.");
			form.addComponent(phoneNo);
			street = new TextField("Street");
			form.addComponent(street);
			city = new TextField("City/Suburb");
			form.addComponent(city);
			state = new TextField("State");
			form.addComponent(state);
			postcode = new TextField("Postcode/Zip Code");
			form.addComponent(postcode);

			countries = new ComboBox("Country");
			loadCountries(countries);
			form.addComponent(countries);

			groupName.addValidator(
					new StringLengthValidator("Group Name must be between 6 and 255 characters long.", 6, 255, false));
			groupName.addValidator(value -> {
				// tell the user if their group name is unique.
				GroupDao groupDao = new DaoFactory().getGroupDao();
				String groupNameString = ((String) value).trim();
				if (groupNameString.length() > 0)
				{
					if (groupDao.findByName(groupNameString) != null)
						throw new Validator.InvalidValueException("Group name already exists. Please choose another.");
				}

			});
		}

		return form;
	}

	private void loadGroupTypes()
	{
		this.groupTypeField.addItem(GroupType.Scouts);
		this.groupTypeField.addItem(GroupType.GirlGuides);

	}

	private void loadCountries(ComboBox countries)
	{

		String[] locales = Locale.getISOCountries();

		for (String countryCode : locales)
		{

			Locale locale = new Locale("", countryCode);

			countries.addItem(locale.getDisplayCountry());
		}

		// Aussie, Aussie, Aussie, Oi, Oi Oi.
		// I can' believe that I just said that ;<
		countries.select("Australia");

	}

	@Override
	public boolean onAdvance()
	{
		boolean valid = form.validate();
		if (!valid)
			SMNotification.show("Please fix the form errors", Type.ERROR_MESSAGE);
		return valid;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	public GroupSetup getGroupSetup() throws IOException, SAXException
	{
		GroupType groupType = (GroupType) this.groupTypeField.getValue();
		GroupSetup setup = GroupSetup.load(groupType, (String) countries.getValue());

		return setup;
	}

	/**
	 * Called during the final step to create the group and set the current
	 * tenant.
	 *
	 * @return
	 */
	public Group createGroup()
	{
		// So looks like they are going to register so lets create the group.

		JpaBaseDao<Group, Long> dao = DaoFactory.getGenericDao(Group.class);

		Group group = new Group();
		group.setName(this.groupName.getValue());

		group.setStreet(this.street.getValue());
		group.setCity(this.city.getValue());
		group.setState(this.state.getValue());
		group.setPostcode(this.postcode.getValue());
		group.setCountry((String) this.countries.getValue());

		GroupType groupType = (GroupType) this.groupTypeField.getValue();
		group.setGroupType(groupType);

		dao.persist(group);
		// flush the cache to actualise the id of the group (which is required
		// when we call setGroup to configure the current tenant).
		dao.flush();

		SMSession.INSTANCE.setGroup(group);

		// Load the TentantSetup for the seleted group type and country.
		GroupSetup setup;
		try
		{
			setup = GroupSetup.load(groupType, (String) countries.getValue());

			// Now create the SectionTypes based on the Group Type.
			JpaBaseDao<SectionType, Long> daoSectionType = DaoFactory.getGenericDao(SectionType.class);
			List<SectionType> sectionTypes = setup.getSectionTypes();
			for (SectionType sectionType : sectionTypes)
			{
				daoSectionType.persist(sectionType);
			}

			// Add builtin Tags
			JpaBaseDao<Tag, Long> daoTag = DaoFactory.getGenericDao(Tag.class);
			List<Tag> tags = setup.getTags();
			for (Tag tag : tags)
			{
				daoTag.persist(tag);
			}

			// Add the built-in GroupRoles
			JpaBaseDao<GroupRole, Long> daoGroupRole = DaoFactory.getGenericDao(GroupRole.class);
			List<GroupRole> groupRoles = setup.getGroupRoles();
			for (GroupRole role : groupRoles)
			{
				daoGroupRole.persist(role);
				daoGroupRole.flush();
			}

		}
		catch (IOException | SAXException e)
		{
			SMNotification.show(e, Type.ERROR_MESSAGE);
		}
		return group;
	}

}
