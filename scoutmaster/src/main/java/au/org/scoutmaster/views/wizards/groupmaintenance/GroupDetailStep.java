package au.org.scoutmaster.views.wizards.groupmaintenance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.ui.SingleEntityWizardStep;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Group;
import au.org.scoutmaster.domain.Group_;

public class GroupDetailStep extends SingleEntityWizardStep<Group> implements WizardStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(GroupDetailStep.class);
	private VerticalLayout layout;

	public GroupDetailStep(final GroupMaintenanceWizardView setupWizardView)
	{
		super(DaoFactory.getGenericDao(Group.class), Group.class);
	}

	@Override
	public String getCaption()
	{
		return "Group Details";
	}

	@Override
	public Component getContent(final ValidatingFieldGroup<Group> fieldGroup)
	{
		if (layout == null)
		{
			layout = new VerticalLayout();
			layout.setMargin(true);
			final MultiColumnFormLayout<Group> formLayout = new MultiColumnFormLayout<>(1, fieldGroup);
			formLayout.setColumnFieldWidth(0, 250);
			formLayout.setSizeFull();

			final Label label = new Label("<h1>Please update your Group's details.</h1>", ContentMode.HTML);
			label.setContentMode(ContentMode.HTML);

			layout.addComponent(label);
			layout.addComponent(formLayout);

			final TextField groupName = formLayout.bindTextField("Group Name", "name");
			formLayout.bindTextField("Phone No.", Group_.phone1);
			formLayout.bindTextField("Street", Group_.street);
			formLayout.bindTextField("City", Group_.city);
			formLayout.bindTextField("State", Group_.state);
			formLayout.bindTextField("Postcode", Group_.postcode);
			formLayout.bindTextField("Country", Group_.country);

			groupName.addValidator(new StringLengthValidator("Group Name", 6, 255, false));
		}

		return layout;
	}

	@Override
	protected void initEntity(final Group entity)
	{

	}

	@Override
	protected Group findEntity()
	{
		return SMSession.INSTANCE.getGroup();
	}

}
