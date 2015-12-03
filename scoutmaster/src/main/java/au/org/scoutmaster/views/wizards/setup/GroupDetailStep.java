package au.org.scoutmaster.views.wizards.setup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.ui.SingleEntityWizardStep;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Address;
import au.org.scoutmaster.domain.Organisation;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class GroupDetailStep extends SingleEntityWizardStep<Organisation> implements WizardStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(GroupDetailStep.class);
	private VerticalLayout layout;

	public GroupDetailStep(final GroupSetupWizardView setupWizardView)
	{
		super(new DaoFactory().getOrganisationDao(), Organisation.class);
	}

	@Override
	public String getCaption()
	{
		return "Group Details";
	}

	@Override
	public Component getContent(final ValidatingFieldGroup<Organisation> fieldGroup)
	{
		if (layout == null)
		{
			layout = new VerticalLayout();
			layout.setMargin(true);
			final MultiColumnFormLayout<Organisation> formLayout = new MultiColumnFormLayout<>(1, fieldGroup);
			formLayout.setColumnFieldWidth(0, 250);
			formLayout.setSizeFull();

			final Label label = new Label("<h1>Please enter your Scout Group's details.</h1>", ContentMode.HTML);
			label.setContentMode(ContentMode.HTML);

			layout.addComponent(label);
			layout.addComponent(formLayout);

			final TextField groupName = formLayout.bindTextField("Scout Group Name", "name");
			formLayout.bindTextField("Phone No.", "primaryPhone");
			formLayout.bindTextField("Street", "location.street");
			formLayout.bindTextField("City", "location.city");
			formLayout.bindTextField("State", "location.state");
			formLayout.bindTextField("Postcode", "location.postcode");

			groupName.addValidator(new StringLengthValidator("Scout Group Name", 6, 255, false));
		}

		return layout;
	}

	@Override
	protected void initEntity(final Organisation entity)
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
