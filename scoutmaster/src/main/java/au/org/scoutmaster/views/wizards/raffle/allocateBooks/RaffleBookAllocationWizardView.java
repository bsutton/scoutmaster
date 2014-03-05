package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

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

@Menu(display = "Raffle Book Allocation", path="Wizards")
public class RaffleBookAllocationWizardView extends VerticalLayout implements View, WizardProgressListener, URIParameterListener
{
	private static final long serialVersionUID = 1L;

	public static final String NAME = "RaffleBookAllocationWizardView";

	private Wizard wizard;

	private WelcomeStep welcomeStep;
	private SelectContactStep selectContactStep;
	private BooksAllocatedStep booksAllocatedStep;
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

	public BooksAllocatedStep getBooksAllocatedStep()
	{
		return booksAllocatedStep;
	}

	public SelectContactStep getSelectContactStep()
	{
		return selectContactStep;
	}


	@Override
	public void enter(ViewChangeEvent event)
	{

		welcomeStep = new WelcomeStep(this);
		selectContactStep = new SelectContactStep(this);
		booksAllocatedStep = new BooksAllocatedStep(this);
		finalStep = new FinalStep(this);

		// create the Wizard component and add the steps
		wizard = new Wizard();
		wizard.setUriFragmentEnabled(true);
		wizard.addListener(this);
		wizard.addStep(welcomeStep, "Welcome");
		wizard.addStep(selectContactStep, "SelectContact");
		wizard.addStep(booksAllocatedStep, "AllocateBooks");
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
