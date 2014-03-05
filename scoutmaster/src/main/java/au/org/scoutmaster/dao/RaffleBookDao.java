package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;

import au.com.vaadinutils.dao.JpaBaseDao;
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

	public RaffleBookDao(EntityManager em)
	{
		super(em);
	}

	@Override
	public JPAContainer<RaffleBook> createVaadinContainer()
	{
		JPAContainer<RaffleBook> container = super.createVaadinContainer();

		container.sort(new Object[]
		{ RaffleBook_.firstNo.getName() }, new boolean[]
		{ true });
		
		container.addNestedContainerProperty(new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.allocatedTo).getName());
		container.addNestedContainerProperty(new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.dateAllocated).getName());
		container.addNestedContainerProperty(new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.issuedBy).getName());
		container.addNestedContainerProperty(new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.dateIssued).getName());

		return container;
	}

	public List<RaffleBook> findFirstUnallocated(Raffle raffle)
	{
		return super.findAllByAttribute(RaffleBook_.raffle, raffle, RaffleBook_.firstNo);
		//super.findListBySingleParameter(RaffleBook.FIND_FIRST_UNALLOCATED, RaffleBook_.raffle, raffle);
		
	}
}
