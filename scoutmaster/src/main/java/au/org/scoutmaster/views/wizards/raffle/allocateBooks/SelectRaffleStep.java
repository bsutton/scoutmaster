package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

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

	private final RaffleBookAllocationWizardView setupWizardView;

	private ComboBox raffleField;

	public SelectRaffleStep(final RaffleBookAllocationWizardView setupWizardView)
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
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		final Label label = new Label("<h1>Select Raffle to Allocate from.</h1>", ContentMode.HTML);
		label.setContentMode(ContentMode.HTML);

		layout.addComponent(label);

		final RaffleDao daoRaffle = new DaoFactory().getRaffleDao();
		this.raffleField = new ComboBox();
		this.raffleField.setContainerDataSource(daoRaffle.createVaadinContainer());
		this.raffleField.setItemCaptionPropertyId(Raffle_.name.getName());
		this.raffleField.setNullSelectionAllowed(false);

		this.raffleField.setRequired(true);

		Raffle raffle = null;
		@SuppressWarnings("unchecked")
		final List<Raffle> list = daoRaffle.findAll(new SingularAttribute[]
				{ Raffle_.startDate }, new boolean[]
						{ false });

		if (list.size() > 0)
		{
			raffle = list.get(0);
		}
		this.raffleField.setValue(raffle.getId());

		layout.addComponent(this.raffleField);

		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		boolean advance = false;
		if (this.raffleField.isValid())
		{
			final Long id = (Long) this.raffleField.getValue();
			final JpaBaseDao<Raffle, Long> raffleDao = DaoFactory.getGenericDao(Raffle.class);
			final Raffle raffle = raffleDao.findById(id);
			this.setupWizardView.setRaffle(raffle);
			advance = true;
		}
		else
		{
			SMNotification.show("Please select a Raffle", Type.WARNING_MESSAGE);
		}
		return advance;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}
}
