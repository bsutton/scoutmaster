package au.org.scoutmaster.views.actions;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.CrudAction;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.views.wizards.raffle.allocateBooks.RaffleBookAllocationWizardView;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.ui.UI;

public class RaffleActionAllocateBooks implements CrudAction<Raffle>
{
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isDefault()
	{
		return false;
	}

	@Override
	public void exec(BaseCrudView<Raffle> crud, EntityItem<Raffle> entity)
	{
		// Launch the raffle book import wizard.
		Raffle raffle = entity.getEntity();
		UI.getCurrent().getNavigator().navigateTo(RaffleBookAllocationWizardView.NAME + "/ID=" + raffle.getId());
	}
	
	public String toString()
	{
		return "Allocate Books";
	}

}
