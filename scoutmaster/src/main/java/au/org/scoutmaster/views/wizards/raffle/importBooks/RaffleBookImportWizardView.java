package au.org.scoutmaster.views.wizards.raffle.importBooks;

import java.util.HashMap;

import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.application.NavigatorUI;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.RaffleDao;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.help.HelpPageIdentifier;
import au.org.scoutmaster.help.HelpProvider;
import au.org.scoutmaster.views.RaffleView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Menu(display = "Import Books", path = "Raffle")
public class RaffleBookImportWizardView extends VerticalLayout implements View, WizardProgressListener, HelpProvider
{
	private static final long serialVersionUID = 1L;

	public static final String NAME = "RaffleBookImportWizardView";

	private Wizard wizard;

	private WelcomeStep welcomeStep;
	private SelectRaffleStep raffleStep;
	private ConfirmRaffleDetails confirmRaffleStep;
	private TicketRangeStep ticketRangeStep;
	private FinalStep finalStep;

	/**
	 * The raffle we are importing the books into.
	 */
	private Raffle raffle = null;

	public RaffleBookImportWizardView()
	{
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

	public FinalStep getImportStep()
	{
		return finalStep;
	}

	@Override
	public void enter(ViewChangeEvent event)
	{

		HashMap<String, String> parameters = NavigatorUI.extractParameterMap(event);
		
		String value = parameters.get("ID");
		if (value != null)
		{
			RaffleDao daoRaffle = new DaoFactory().getRaffleDao();
			this.raffle = daoRaffle.findById(Long.valueOf(value));
		}

		welcomeStep = new WelcomeStep(this);
		raffleStep = new SelectRaffleStep(this);
		confirmRaffleStep = new ConfirmRaffleDetails(this);
		ticketRangeStep = new TicketRangeStep(this);
		finalStep = new FinalStep(this);

		// create the Wizard component and add the steps
		wizard = new Wizard();
		wizard.setUriFragmentEnabled(true);
		wizard.addListener(this);
		wizard.addStep(welcomeStep, "Welcome");
		if (raffle == null)
			wizard.addStep(raffleStep, "Raffle");
		wizard.addStep(confirmRaffleStep, "Confirm");
		wizard.addStep(ticketRangeStep, "TicketRange");
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
		this.endWizard("Import Completed!");

	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event)
	{
		this.endWizard("Import Cancelled!");

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

	public void setRaffle(Raffle raffle)
	{
		this.raffle = raffle;

	}
	@Override
	public HelpPageIdentifier getHelpId()
	{
		return HelpPageIdentifier.RaffleImportWizard;
	}

}
