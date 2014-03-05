package au.org.scoutmaster.dao;

import javax.persistence.EntityManager;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.RaffleAllocation;
import au.org.scoutmaster.domain.RaffleAllocation_;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class RaffleAllocationDao extends JpaBaseDao<RaffleAllocation, Long> implements Dao<RaffleAllocation, Long>
{

	public RaffleAllocationDao()
	{
		// inherit the default per request em.
	}

	public RaffleAllocationDao(EntityManager em)
	{
		super(em);
	}

	@Override
	public JPAContainer<RaffleAllocation> createVaadinContainer()
	{
		JPAContainer<RaffleAllocation> container = super.createVaadinContainer();

		container.sort(new Object[]
		{ RaffleAllocation_.dateAllocated.getName(), RaffleAllocation_.allocatedTo.getName()}, new boolean[]
		{ true, true });
		

		return container;
	}
}
