package au.org.scoutmaster.views;

import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import au.org.scoutmaster.views.messagingWizard.EnterMessage;
import au.org.scoutmaster.views.messagingWizard.SendMessage;
import au.org.scoutmaster.views.messagingWizard.TransmissionComplete;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@Menu(display="Messaging")
public class MessagingWizardView extends VerticalLayout implements View, WizardProgressListener
{
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "Messaging";

	private Wizard wizard;
	
	private EnterMessage enter;
	private SendMessage send;
	private TransmissionComplete complete;

	


	public EnterMessage getEnter()
	{
		return enter;
	}


	public SendMessage getSend()
	{
		return send;
	}


	public TransmissionComplete getComplete()
	{
		return complete;
	}


	@Override
	public void enter(ViewChangeEvent event)
	{

		enter = new EnterMessage(this);
		send = new SendMessage(this);
		complete = new TransmissionComplete(this);


		// create the Wizard component and add the steps
		wizard = new Wizard();
		wizard.setUriFragmentEnabled(true);
		wizard.addListener(this);
		wizard.addStep(enter, "enter");
		wizard.addStep(send, "send");
		wizard.addStep(complete, "complete");
		wizard.setHeight("600px");
		wizard.setWidth("800px");
		wizard.setUriFragmentEnabled(true);
		
		/* Main layout */
		this.setMargin(true);
		this.setSpacing(true);
		this.addComponent(wizard);
		this.setComponentAlignment(wizard, Alignment.TOP_CENTER);

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
		Page.getCurrent().setLocation("");
	}

}
