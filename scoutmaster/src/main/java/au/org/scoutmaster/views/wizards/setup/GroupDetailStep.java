package au.org.scoutmaster.views.wizards.setup;

import org.apache.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Address;
import au.org.scoutmaster.domain.Organisation;
import au.org.scoutmaster.util.MultiColumnFormLayout;
import au.org.scoutmaster.util.ValidatingFieldGroup;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class GroupDetailStep extends SingleEntityStep<Organisation> implements WizardStep
{
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(GroupDetailStep.class);

	public GroupDetailStep(SetupWizardView setupWizardView)
	{
		super(setupWizardView, new DaoFactory().getOrganisationDao(), Organisation.class);
	}

	@Override
	public String getCaption()
	{
		return "Group Details";
	}

	@Override
	public Component buildEditor(ValidatingFieldGroup<Organisation> fieldGroup)
	{
		MultiColumnFormLayout<Organisation> formLayout = new MultiColumnFormLayout<>(1, fieldGroup);
		formLayout.setWidth("500px");

		Label label = new Label("<h1>Please enter your Scout Group's details.</h1>", ContentMode.HTML);
		formLayout.bindLabel(label);

		TextField groupName = formLayout.bindTextField("Scout Group Name", "name");
		formLayout.bindTextField("Phone No.", "primaryPhone");
		formLayout.bindTextField("Street", "location.street");
		formLayout.bindTextField("City", "location.city");
		formLayout.bindTextField("State", "location.state");
		formLayout.bindTextField("Postcode", "location.postcode");

		groupName.addValidator(new StringLengthValidator("Scout Group Name", 6, 255, false));

		return formLayout;
	}

	@Override
	protected void initEntity(Organisation entity)
	{
		entity.setOurScoutGroup(true);
		entity.setLocation(new Address());
		
	}

	@Override
	protected Organisation findEntity()
	{
		return new DaoFactory().getOrganisationDao().findOurScoutGroup();
	}

}