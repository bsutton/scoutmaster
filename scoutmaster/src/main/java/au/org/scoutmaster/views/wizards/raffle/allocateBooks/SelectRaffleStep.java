package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.RaffleDao;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.Raffle_;
import au.org.scoutmaster.util.SMNotification;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

public class SelectRaffleStep implements WizardStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(SelectRaffleStep.class);

	private RaffleBookAllocationWizardView setupWizardView;

	private ComboBox raffleField;

	public SelectRaffleStep(RaffleBookAllocationWizardView setupWizardView)
	{
		this.setupWizardView = setupWizardView;
	}

	@Override
	public String getCaption()
	{
		return "Select Raffle";
	}

	@Override
	public Component getContent()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		Label label = new Label("<h1>Select Raffle to Allocate from.</h1>", ContentMode.HTML);
		label.setContentMode(ContentMode.HTML);

		layout.addComponent(label);

		RaffleDao daoRaffle = new DaoFactory().getRaffleDao();
		raffleField = new ComboBox();
		raffleField.setContainerDataSource(daoRaffle.createVaadinContainer());
		raffleField.setItemCaptionPropertyId(Raffle_.name.getName());
		raffleField.setNullSelectionAllowed(false);

		raffleField.setRequired(true);

		layout.addComponent(raffleField);

		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		boolean advance = false;
		if (raffleField.isValid())
		{
			Long id = (Long) raffleField.getValue();
			JpaBaseDao<Raffle, Long> raffleDao = DaoFactory.getGenericDao(Raffle.class);
			Raffle raffle = raffleDao.findById(id);
			setupWizardView.setRaffle(raffle);
			advance = true;
		}
		else
			SMNotification.show("Please select a Raffle", Type.WARNING_MESSAGE);
		return advance;
	}

	public boolean onBack()
	{
		return true;
	}
}

