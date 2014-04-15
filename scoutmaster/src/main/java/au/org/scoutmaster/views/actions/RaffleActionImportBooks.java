package au.org.scoutmaster.views.actions;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.CrudAction;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.views.wizards.raffle.importBooks.RaffleBookImportWizardView;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.ui.UI;

public class RaffleActionImportBooks implements CrudAction<Raffle>
{
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isDefault()
	{
		return false;
	}

	@Override
	public void exec(final BaseCrudView<Raffle> crud, final EntityItem<Raffle> entity)
	{
		// Launch the raffle book import wizard.
		final Raffle raffle = entity.getEntity();
		UI.getCurrent().getNavigator().navigateTo(RaffleBookImportWizardView.NAME + "/ID=" + raffle.getId());
	}

	@Override
	public String toString()
	{
		return "Import Books";
	}

}
