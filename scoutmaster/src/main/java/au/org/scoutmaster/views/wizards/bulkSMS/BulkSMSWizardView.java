package au.org.scoutmaster.views.wizards.bulkSMS;

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

@Menu(display = "Bulk SMS", path = "Communication")
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
		return this.details;
	}

	public ShowProgressStep getSend()
	{
		return this.send;
	}

	public SelectRecipientsStep getRecipientStep()
	{
		return this.recipientStep;
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{

		this.recipientStep = new SelectRecipientsStep(this);
		this.details = new MessageDetailsStep(this);
		this.send = new ShowProgressStep(this);
		this.confirm = new ConfirmDetailsStep(this);

		// create the Wizard component and add the steps
		this.wizard = new Wizard();
		this.wizard.setUriFragmentEnabled(true);
		this.wizard.addListener(this);
		this.wizard.addStep(this.recipientStep, "select");
		this.wizard.addStep(this.details, "enter");
		this.wizard.addStep(this.confirm, "confirm");
		this.wizard.addStep(this.send, "send");
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
		endWizard("Transmission Completed!");

	}

	@Override
	public void wizardCancelled(final WizardCancelledEvent event)
	{
		endWizard("Transmission Cancelled!");

	}

	private void endWizard(final String message)
	{
		this.wizard.setVisible(false);
		Notification.show(message);
		Page.getCurrent().setTitle(message);
		Page.getCurrent().setLocation("");
	}

}
