package au.org.scoutmaster.views.wizards.groupmaintenance;

import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.access.UserDao;

@Menu(display = "Group Maintenance", path = "Wizards")
public class GroupMaintenanceWizardView extends VerticalLayout implements View, WizardProgressListener
{
	private static final long serialVersionUID = 1L;

	public static final String NAME = "MaintenanceWizard";

	private Wizard wizard;

	private NewAccountStep newAccountStep;
	private GroupDetailStep groupDetailStep;
	private SmsProviderStep smsProviderStep;
	private FinalStep finalStep;

	public GroupDetailStep getGroupDetailStep()
	{
		return this.groupDetailStep;
	}

	public NewAccountStep getNewAccountStep()
	{
		return this.newAccountStep;
	}

	public SmsProviderStep getSmsProviderStep()
	{
		return this.smsProviderStep;
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{

		this.newAccountStep = new NewAccountStep(this);
		this.groupDetailStep = new GroupDetailStep(this);
		this.smsProviderStep = new SmsProviderStep(this);
		this.finalStep = new FinalStep(this);

		final UserDao daoUser = new DaoFactory().getUserDao();
		boolean skipAccountSetup = false;
		if (daoUser.findAll().size() > 0)
		{
			skipAccountSetup = true;
		}

		// create the Wizard component and add the steps
		this.wizard = new Wizard();
		this.wizard.setUriFragmentEnabled(true);
		this.wizard.addListener(this);
		if (!skipAccountSetup)
		{
			this.wizard.addStep(this.newAccountStep, "NewAccount");
		}
		this.wizard.addStep(this.groupDetailStep, "GroupDetails");
		this.wizard.addStep(this.smsProviderStep, "SMSProvider");
		this.wizard.addStep(this.finalStep, "Final");
		this.wizard.setSizeFull();
		this.wizard.setUriFragmentEnabled(true);

		/* Main layout */
		this.setMargin(true);
		setSpacing(true);
		this.addComponent(this.wizard);
		setComponentAlignment(this.wizard, Alignment.TOP_CENTER);
		setSizeFull();

	}

	@Override
	public void activeStepChanged(final WizardStepActivationEvent event)
	{
		// NOOP

	}

	@Override
	public void stepSetChanged(final WizardStepSetChangedEvent event)
	{
		Page.getCurrent().setTitle(event.getComponent().getCaption());

	}

	@Override
	public void wizardCompleted(final WizardCompletedEvent event)
	{
		endWizard("Setup Completed!");

	}

	@Override
	public void wizardCancelled(final WizardCancelledEvent event)
	{
		endWizard("Setup Cancelled!");

	}

	private void endWizard(final String message)
	{
		this.wizard.setVisible(false);
		Notification.show(message);
		Page.getCurrent().setTitle(message);
		Page.getCurrent().setLocation("");
	}

}
