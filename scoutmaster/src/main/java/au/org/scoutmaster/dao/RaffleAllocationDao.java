package au.org.scoutmaster.dao;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.com.vaadinutils.dao.Path;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.RaffleAllocation;
import au.org.scoutmaster.domain.RaffleAllocation_;

public class RaffleAllocationDao extends JpaBaseDao<RaffleAllocation, Long> implements Dao<RaffleAllocation, Long>
{

	public RaffleAllocationDao()
	{
		// inherit the default per request em.
	}

	

	@Override
	public JPAContainer<RaffleAllocation> createVaadinContainer()
	{
		final JPAContainer<RaffleAllocation> container = super.createVaadinContainer();

		container.addNestedContainerProperty(new Path(RaffleAllocation_.allocatedTo, Contact_.fullname).getName());
		container.addNestedContainerProperty(new Path(RaffleAllocation_.issuedBy, Contact_.fullname).getName());

		container.sort(
				new Object[]
				{ RaffleAllocation_.dateAllocated.getName(),
						new Path(RaffleAllocation_.allocatedTo, Contact_.fullname).getName() }, new boolean[]
				{ true, true });

		return container;
	}

}
