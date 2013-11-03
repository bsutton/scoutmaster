package au.org.scoutmaster.views.wizards.messaging;

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

@Menu(display="Bulk SMS", path="Communication")
public class BulkSMSWizardView extends VerticalLayout implements View, WizardProgressListener
{
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "BulkSMS";

	private Wizard wizard;
	
	private SelectRecipientsStep recipientStep;
	private MessageDetailsStep details;
	private ConfirmDetailsStep confirm;
	private ShowProgressStep send;

	public MessageDetailsStep getDetails()
	{
		return details;
	}


	public ShowProgressStep getSend()
	{
		return send;
	}


	public SelectRecipientsStep getRecipientStep()
	{
		return recipientStep;
	}


	@Override
	public void enter(ViewChangeEvent event)
	{

		recipientStep = new SelectRecipientsStep(this);
		details = new MessageDetailsStep(this);
		send = new ShowProgressStep(this);
		confirm = new ConfirmDetailsStep(this);


		// create the Wizard component and add the steps
		wizard = new Wizard();
		wizard.setUriFragmentEnabled(true);
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
