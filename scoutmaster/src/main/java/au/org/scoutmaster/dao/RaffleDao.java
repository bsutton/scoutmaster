package au.org.scoutmaster.dao;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.Raffle_;

public class RaffleDao extends JpaBaseDao<Raffle, Long> implements Dao<Raffle, Long>
{

	public RaffleDao()
	{
		// inherit the default per request em.
	}

	

	@Override
	public JPAContainer<Raffle> createVaadinContainer()
	{
		final JPAContainer<Raffle> container = super.createVaadinContainer();

		container.sort(new Object[]
				{ Raffle_.startDate.getName() }, new boolean[]
						{ true });

		return container;
	}
}
