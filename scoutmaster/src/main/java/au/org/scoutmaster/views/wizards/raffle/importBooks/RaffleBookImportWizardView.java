package au.org.scoutmaster.views.wizards.raffle.importBooks;

import java.util.HashMap;

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
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.help.HelpProvider;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.application.NavigatorUI;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.RaffleDao;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.help.HelpPageIdentifier;
import au.org.scoutmaster.views.RaffleView;

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
		return this.welcomeStep;
	}

	public TicketRangeStep getTicketRangeStep()
	{
		return this.ticketRangeStep;
	}

	public ConfirmRaffleDetails getConfirmRaffleStep()
	{
		return this.confirmRaffleStep;
	}

	public FinalStep getImportStep()
	{
		return this.finalStep;
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{

		final HashMap<String, String> parameters = NavigatorUI.extractParameterMap(event);

		final String value = parameters.get("ID");
		if (value != null)
		{
			final RaffleDao daoRaffle = new DaoFactory().getRaffleDao();
			this.raffle = daoRaffle.findById(Long.valueOf(value));
		}

		this.welcomeStep = new WelcomeStep(this);
		this.raffleStep = new SelectRaffleStep(this);
		this.confirmRaffleStep = new ConfirmRaffleDetails(this);
		this.ticketRangeStep = new TicketRangeStep(this);
		this.finalStep = new FinalStep(this);

		// create the Wizard component and add the steps
		this.wizard = new Wizard();
		this.wizard.setUriFragmentEnabled(true);
		this.wizard.addListener(this);
		this.wizard.addStep(this.welcomeStep, "Welcome");
		if (this.raffle == null)
		{
			this.wizard.addStep(this.raffleStep, "Raffle");
		}
		this.wizard.addStep(this.confirmRaffleStep, "Confirm");
		this.wizard.addStep(this.ticketRangeStep, "TicketRange");
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
		endWizard("Import Completed!");

	}

	@Override
	public void wizardCancelled(final WizardCancelledEvent event)
	{
		endWizard("Import Cancelled!");

	}

	private void endWizard(final String message)
	{
		this.wizard.setVisible(false);
		Notification.show(message);
		Page.getCurrent().setTitle(message);
		UI.getCurrent().getNavigator().navigateTo(RaffleView.NAME);

	}

	public Raffle getRaffle()
	{
		return this.raffle;
	}

	public void setRaffle(final Raffle raffle)
	{
		this.raffle = raffle;

	}

	@Override
	public HelpPageIdentifier getHelpId()
	{
		return HelpPageIdentifier.RaffleImportWizard;
	}

}
