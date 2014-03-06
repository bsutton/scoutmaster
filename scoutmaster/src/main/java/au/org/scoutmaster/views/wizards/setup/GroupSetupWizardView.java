package au.org.scoutmaster.views.wizards.setup;

import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.access.UserDao;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@Menu(display = "Group Setup", path="Wizards")
public class GroupSetupWizardView extends VerticalLayout implements View, WizardProgressListener
{
	private static final long serialVersionUID = 1L;

	public static final String NAME = "SetupWizard";

	private Wizard wizard;

	private WelcomeStep welcomeStep;
	private NewAccountStep newAccountStep;
	private GroupDetailStep groupDetailStep;
	private SmsProviderStep smsProviderStep;
	private SmtpStep smtpStep;
	private FinalStep finalStep;

	public WelcomeStep getWelcomeStep()
	{
		return welcomeStep;
	}

	public GroupDetailStep getGroupDetailStep()
	{
		return groupDetailStep;
	}

	public NewAccountStep getNewAccountStep()
	{
		return newAccountStep;
	}

	public SmsProviderStep getSmsProviderStep()
	{
		return smsProviderStep;
	}

	public SmtpStep getSmtpStep()
	{
		return smtpStep;
	}

	@Override
	public void enter(ViewChangeEvent event)
	{

		welcomeStep = new WelcomeStep(this);
		newAccountStep = new NewAccountStep(this);
		groupDetailStep = new GroupDetailStep(this);
		smsProviderStep = new SmsProviderStep(this);
		smtpStep = new SmtpStep(this);
		finalStep = new FinalStep(this);

		UserDao daoUser = new DaoFactory().getUserDao();
		boolean skipAccountSetup = false;
		if (daoUser.findAll().size() > 0)
			skipAccountSetup = true;

		// create the Wizard component and add the steps
		wizard = new Wizard();
		wizard.setUriFragmentEnabled(true);
		wizard.addListener(this);
		if (!skipAccountSetup)
		{
			wizard.addStep(welcomeStep, "Welcome");
			wizard.addStep(newAccountStep, "NewAccount");
		}
		wizard.addStep(groupDetailStep, "GroupDetails");
		wizard.addStep(smsProviderStep, "SMSProvider");
		wizard.addStep(smtpStep, "SMTP");
		wizard.addStep(finalStep, "Final");
		wizard.setSizeFull();
		wizard.setUriFragmentEnabled(true);

		/* Main layout */
		this.setMargin(true);
		this.setSpacing(true);
		this.addComponent(wizard);
		this.setComponentAlignment(wizard, Alignment.TOP_CENTER);
		this.setSizeFull();

	}

	@Override
	public void activeStepChanged(WizardStepActivationEvent event)
	{
		// NOOP

	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent event)
	{
		Page.getCurrent().setTitle(event.getComponent().getCaption());

	}

	@Override
	public void wizardCompleted(WizardCompletedEvent event)
	{
		this.endWizard("Setup Completed!");

	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event)
	{
		this.endWizard("Setup Cancelled!");

	}

	private void endWizard(String message)
	{
		wizard.setVisible(false);
		Notification.show(message);
		Page.getCurrent().setTitle(message);
		Page.getCurrent().setLocation("");
	}

}
