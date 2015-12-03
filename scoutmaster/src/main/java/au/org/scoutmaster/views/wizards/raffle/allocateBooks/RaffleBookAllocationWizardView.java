package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

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

@Menu(display = "Allocate Books", path = "Raffle")
public class RaffleBookAllocationWizardView extends VerticalLayout implements View, WizardProgressListener, HelpProvider
{
	private static final long serialVersionUID = 1L;

	public static final String NAME = "RaffleBookAllocationWizardView";

	private static final String SELECTION_STEP = "SelectionStep";

	private static final String ALLOCATION_STEP = "AllocationStep";

	private Wizard wizard;

	private WelcomeStep welcomeStep;
	private AllocationMethodStep allocationMethodStep;
	private SelectRaffleStep raffleStep;
	private SelectStep selectStep;
	private AllocationStep allocationStep;
	private FinalStep finalStep;

	/**
	 * The raffle we are importing the books into.
	 */
	private Raffle raffle = null;

	private boolean hasRaffleSelectionStep = true;

	public RaffleBookAllocationWizardView()
	{
	}

	public WelcomeStep getWelcomeStep()
	{
		return this.welcomeStep;
	}

	public AllocationStep getAllocatedStep()
	{
		return this.allocationStep;
	}

	public SelectStep getSelectStep()
	{
		return this.selectStep;
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
			this.hasRaffleSelectionStep = false;
		}

		this.welcomeStep = new WelcomeStep(this);
		this.allocationMethodStep = new AllocationMethodStep(this);
		this.selectStep = new BulkSelectionStep(this);
		this.raffleStep = new SelectRaffleStep(this);
		this.allocationStep = new BulkAllocationStep(this);
		this.finalStep = new FinalStep(this);

		// create the Wizard component and add the steps
		this.wizard = new Wizard();
		this.wizard.setUriFragmentEnabled(true);
		this.wizard.addListener(this);
		this.wizard.addStep(this.welcomeStep, "Welcome");
		if (this.hasRaffleSelectionStep)
		{
			this.wizard.addStep(this.raffleStep, "Raffle");
		}
		this.wizard.addStep(this.allocationMethodStep);
		this.wizard.addStep(this.selectStep, RaffleBookAllocationWizardView.SELECTION_STEP);
		this.wizard.addStep(this.allocationStep, RaffleBookAllocationWizardView.ALLOCATION_STEP);
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
		endWizard("Allocation Completed");

	}

	@Override
	public void wizardCancelled(final WizardCancelledEvent event)
	{
		endWizard("Allocation Cancelled!");

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

	public void addAllocationStep(final SelectStep selectionStep, final AllocationStep allocationStep)
	{
		this.wizard.removeStep(RaffleBookAllocationWizardView.SELECTION_STEP);
		this.wizard.removeStep(RaffleBookAllocationWizardView.ALLOCATION_STEP);
		if (this.hasRaffleSelectionStep)
		{
			this.wizard.addStep(selectionStep, RaffleBookAllocationWizardView.SELECTION_STEP, 3);
			this.wizard.addStep(allocationStep, RaffleBookAllocationWizardView.ALLOCATION_STEP, 4);
		}
		else
		{
			this.wizard.addStep(selectionStep, RaffleBookAllocationWizardView.SELECTION_STEP, 2);
			this.wizard.addStep(allocationStep, RaffleBookAllocationWizardView.ALLOCATION_STEP, 3);
		}
		this.selectStep = selectionStep;
		this.allocationStep = allocationStep;
	}

	public void setRaffle(final Raffle raffle)
	{
		this.raffle = raffle;

	}

	@Override
	public HelpPageIdentifier getHelpId()
	{
		return HelpPageIdentifier.RaffleAllocationWizard;
	}
}
