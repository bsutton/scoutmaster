package au.org.scoutmaster.views.wizards.groupsetup;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import au.org.scoutmaster.domain.Group;
import au.org.scoutmaster.views.ContactView;

public class FinalStep implements WizardStep
{

	private GroupSetupWizardView setupWizard;

	public FinalStep(final GroupSetupWizardView setupWizardView)
	{
		this.setupWizard = setupWizardView;
	}

	@Override
	public String getCaption()
	{
		return "Finalise";
	}

	@Override
	public Component getContent()
	{
		final VerticalLayout layout = new VerticalLayout();

		final StringBuilder sb = new StringBuilder();
		sb.append("<h1>Configuration complete.</h1>");
		sb.append("<p>Congratulations Scoutmaster is now setup and ready to go.</p>");
		sb.append("<p>Click the 'Finish' button and start growing your Group.</p>");

		final Label label = new Label(sb.toString());
		label.setContentMode(ContentMode.HTML);

		layout.addComponent(label);
		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		// So they have decided to go with scoutmaster so lets create the group
		// and account.
		Group group = createGroup();

		// Now the account
		createUser(group);
		UI.getCurrent().getNavigator().navigateTo(ContactView.NAME);
		return false;
	}

	private void createUser(Group group)
	{
		setupWizard.getNewAccountStep().createUser(group);

	}

	private Group createGroup()
	{
		return setupWizard.getGroupDetailStep().createGroup();
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

}
