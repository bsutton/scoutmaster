package au.org.scoutmaster.views.wizards.bulkEmail;


import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import au.com.vaadinutils.menu.Menu;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@Menu(display="Bulk Email", path="Communication")
public class WizardView extends VerticalLayout implements View, WizardProgressListener
{
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "BulkEmail";

	private Wizard wizard;
	
	private StepSelectRecipients recipientStep;
	private StepEnterDetails details;
	private StepConfirmDetails confirm;
	private StepShowProgress send;

	public StepEnterDetails getDetails()
	{
		return details;
	}


	public StepShowProgress getSend()
	{
		return send;
	}


	public StepSelectRecipients getRecipientStep()
	{
		return recipientStep;
	}


	@Override
	public void enter(ViewChangeEvent event)
	{

		recipientStep = new StepSelectRecipients(this);
		details = new StepEnterDetails(this);
		send = new StepShowProgress(this);
		confirm = new StepConfirmDetails(this);


		// create the Wizard component and add the steps
		wizard = new Wizard();
		wizard.addListener(this);
		wizard.addStep(recipientStep, "select");
		wizard.addStep(details, "enter");
		wizard.addStep(confirm, "confirm");
		wizard.addStep(send, "send");
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
		this.endWizard("Transmission Completed!");

	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event)
	{
		this.endWizard("Transmission Cancelled!");
		
	}

	private void endWizard(String message)
	{
		wizard.setVisible(false);
		Notification.show(message);
		Page.getCurrent().setTitle(message);
		Page.getCurrent().setLocation("");
	}

}
