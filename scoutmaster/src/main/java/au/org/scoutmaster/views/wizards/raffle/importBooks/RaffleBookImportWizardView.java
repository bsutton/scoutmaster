package au.org.scoutmaster.views.wizards.raffle.importBooks;

import java.util.Map;

import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.application.URIParameterListener;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.RaffleDao;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.views.RaffleView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Menu(display = "Setup", path="Admin")
public class RaffleBookImportWizardView extends VerticalLayout implements View, WizardProgressListener, URIParameterListener
{
	private static final long serialVersionUID = 1L;

	public static final String NAME = "RaffleBookImportWizardView";

	private Wizard wizard;

	private WelcomeStep welcomeStep;
	private ConfirmRaffleDetails confirmRaffleStep;
	private TicketRangeStep ticketRangeStep;
	private ImportStep importStep;
	private FinalStep finalStep;

	/**
	 * The raffle we are importing the books into.
	 */
	private Raffle raffle = null;

	public RaffleBookImportWizardView()
	{
	}

	@Override
	public void setParameters(Map<String, String> parameters)
	{
		String value = parameters.get("ID");
		
		RaffleDao daoRaffle = new DaoFactory().getRaffleDao();
		
		this.raffle = daoRaffle.findById(Long.valueOf(value));
	}

	
	public WelcomeStep getWelcomeStep()
	{
		return welcomeStep;
	}

	public TicketRangeStep getTicketRangeStep()
	{
		return ticketRangeStep;
	}

	public ConfirmRaffleDetails getConfirmRaffleStep()
	{
		return confirmRaffleStep;
	}

	public ImportStep getImportStep()
	{
		return importStep;
	}


	@Override
	public void enter(ViewChangeEvent event)
	{

		welcomeStep = new WelcomeStep(this);
		confirmRaffleStep = new ConfirmRaffleDetails(this);
		ticketRangeStep = new TicketRangeStep(this);
		importStep = new ImportStep(this);
		finalStep = new FinalStep(this);

		// create the Wizard component and add the steps
		wizard = new Wizard();
		wizard.setUriFragmentEnabled(true);
		wizard.addListener(this);
		wizard.addStep(welcomeStep, "Welcome");
		wizard.addStep(confirmRaffleStep, "Confirm");
		wizard.addStep(ticketRangeStep, "TicketRange");
		wizard.addStep(importStep, "Import");
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
		UI.getCurrent().getNavigator().navigateTo(RaffleView.NAME);
	}
	public Raffle getRaffle()
	{
		return this.raffle;
	}



}
