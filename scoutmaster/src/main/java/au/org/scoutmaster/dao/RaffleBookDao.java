package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.com.vaadinutils.dao.Path;
import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.RaffleAllocation_;
import au.org.scoutmaster.domain.RaffleBook;
import au.org.scoutmaster.domain.RaffleBook_;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class RaffleBookDao extends JpaBaseDao<RaffleBook, Long> implements Dao<RaffleBook, Long>
{

	public RaffleBookDao()
	{
		// inherit the default per request em.
	}

	public RaffleBookDao(final EntityManager em)
	{
		super(em);
	}

	@Override
	public JPAContainer<RaffleBook> createVaadinContainer()
	{
		final JPAContainer<RaffleBook> container = super.createVaadinContainer();

		container.sort(new Object[]
		{ RaffleBook_.firstNo.getName() }, new boolean[]
		{ true });

		container.addNestedContainerProperty(new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.allocatedTo)
		.getName());
		container.addNestedContainerProperty(new Path().add(RaffleBook_.raffleAllocation)
				.add(RaffleAllocation_.allocatedTo).add(Contact_.fullname).getName());
		container.addNestedContainerProperty(new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.dateAllocated)
		.getName());
		container.addNestedContainerProperty(new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.issuedBy)
		.getName());
		container.addNestedContainerProperty(new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.dateIssued)
		.getName());
		container.addNestedContainerProperty(new Path(RaffleBook_.raffleAllocation, BaseEntity_.id).getName());

		return container;
	}

	/**
	 * returns a list of unallocated books ordered by their first book no.
	 *
	 * @param raffle
	 * @return
	 */
	public List<RaffleBook> findAllUnallocated(final Raffle raffle)
	{
		return super.findListBySingleParameter(RaffleBook.FIND_ALL_UNALLOCATED, RaffleBook_.raffle.getName(), raffle);

	}

	public List<RaffleBook> findByAllocation(final Long allocationId)
	{
		return super.findListBySingleParameter(RaffleBook.FIND_BY_ALLOCATION, "raffleAllocationId", allocationId);
	}
}
