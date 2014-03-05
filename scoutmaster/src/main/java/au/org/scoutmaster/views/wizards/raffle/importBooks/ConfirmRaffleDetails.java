package au.org.scoutmaster.views.wizards.raffle.importBooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.dao.EntityManagerProvider;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.RaffleDao;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.Raffle_;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ConfirmRaffleDetails  implements WizardStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(ConfirmRaffleDetails.class);

	private RaffleBookImportWizardView setupWizardView;

	public ConfirmRaffleDetails(RaffleBookImportWizardView setupWizardView)
	{
		this.setupWizardView = setupWizardView;
	}

	@Override
	public String getCaption()
	{
		return "Confirm Details";
	}


	@Override
	public Component getContent()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		Label label = new Label("<h1>Confirm the Raffle has been setup correctly.</h1>",
				ContentMode.HTML);
		label.setContentMode(ContentMode.HTML);

		layout.addComponent(label);

		
		Raffle raffle = setupWizardView.getRaffle();
		ValidatingFieldGroup<Raffle> fieldGroup = new ValidatingFieldGroup<Raffle>(Raffle.class);
		
		RaffleDao daoRaffle = new DaoFactory().getRaffleDao();
		// we are going to manipulate the event on a different thread.
		EntityManagerProvider.detach(raffle);
		
		JPAContainer<Raffle> container = daoRaffle.createVaadinContainer();
		EntityItem<Raffle> entityItem = container.createEntityItem(raffle);
		
		fieldGroup.setItemDataSource(entityItem);
		
		SMMultiColumnFormLayout<Raffle> overviewForm = new SMMultiColumnFormLayout<>(1,  fieldGroup);
		overviewForm.setWidth("600px");
		overviewForm.setColumnFieldWidth(0, 400);

		overviewForm.bindTextField("Name", Raffle_.name).setReadOnly(true);
		overviewForm.bindDateField("Start Date", Raffle_.startDate, "yyyy/MM/dd", Resolution.DAY).setReadOnly(true);
		overviewForm.bindDateField("Return Date", Raffle_.returnDate, "yyyy/MM/dd", Resolution.DAY).setReadOnly(true);

		overviewForm.bindTextField("Book No. Prefix", Raffle_.raffleNoPrefix).setReadOnly(true);
		overviewForm.bindTextField("Tickets per Book", Raffle_.ticketsPerBook).setReadOnly(true);
		overviewForm.bindTextField("Sales Price per Ticket", Raffle_.salePricePerTicket).setReadOnly(true);
		overviewForm.bindTextAreaField("Notes", Raffle_.notes, 6).setReadOnly(true);
		
		layout.addComponent(overviewForm);

		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		return true;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

}
