package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import java.util.Map;

import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

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

public class RaffleBookAllocationWizardView extends VerticalLayout implements View, WizardProgressListener, URIParameterListener
{
	private static final long serialVersionUID = 1L;

	public static final String NAME = "RaffleBookAllocationWizardView";

	private static final String SELECTION_STEP = "SelectionStep";

	private static final String ALLOCATION_STEP = "AllocationStep";

	private Wizard wizard;

	private WelcomeStep welcomeStep;
	private SelectStep selectStep;
	private AllocationStep allocationStep;
	private FinalStep finalStep;

	/**
	 * The raffle we are importing the books into.
	 */
	private Raffle raffle = null;

	public RaffleBookAllocationWizardView()
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

	public AllocationStep getAllocatedStep()
	{
		return allocationStep;
	}

	public SelectStep getSelectStep()
	{
		return selectStep;
	}


	@Override
	public void enter(ViewChangeEvent event)
	{

		welcomeStep = new WelcomeStep(this);
		selectStep = new BulkSelectionStep(this);
		allocationStep = new BulkAllocationStep(this);
		finalStep = new FinalStep(this);

		// create the Wizard component and add the steps
		wizard = new Wizard();
		wizard.setUriFragmentEnabled(true);
		wizard.addListener(this);
		wizard.addStep(welcomeStep, "Welcome");
		wizard.addStep(selectStep, SELECTION_STEP);
		wizard.addStep(allocationStep, ALLOCATION_STEP);
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
		this.endWizard("Allocation Completed");

	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event)
	{
		this.endWizard("Allocation Cancelled!");

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

	public void addAllocationStep(SelectStep selectionStep, AllocationStep allocationStep)
	{
		wizard.removeStep(SELECTION_STEP);
		wizard.removeStep(ALLOCATION_STEP);
		wizard.addStep(selectionStep, SELECTION_STEP, 1);
		wizard.addStep(allocationStep, ALLOCATION_STEP, 2);
		this.selectStep = selectionStep;
		this.allocationStep = allocationStep;
	}
}
