package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.accounting.Money;

@Generated(value="Dali", date="2014-03-03T08:16:39.825+1100")
@StaticMetamodel(Raffle.class)
public class Raffle_ extends BaseEntity_ {
	public static volatile SingularAttribute<Raffle, String> name;
	public static volatile SingularAttribute<Raffle, String> notes;
	public static volatile SingularAttribute<Raffle, Date> startDate;
	public static volatile SingularAttribute<Raffle, Date> collectionsDate;
	public static volatile SingularAttribute<Raffle, Date> returnDate;
	public static volatile SingularAttribute<Raffle, Contact> groupRaffleManager;
	public static volatile SingularAttribute<Raffle, Contact> branchRaffleContact;
	public static volatile SingularAttribute<Raffle, String> raffleNoPrefix;
	public static volatile SingularAttribute<Raffle, Integer> ticketsPerBook;
	public static volatile SingularAttribute<Raffle, Integer> totalTicketsSold;
	public static volatile SingularAttribute<Raffle, Integer> ticketsOutstanding;
	public static volatile SingularAttribute<Raffle, Money> salePricePerTicket;
	public static volatile SingularAttribute<Raffle, Money> revenueTarget;
	public static volatile SingularAttribute<Raffle, Money> revenueRaised;
	public static volatile SetAttribute<Raffle, RaffleBook> available;
	public static volatile SetAttribute<Raffle, RaffleAllocation> allocated;
}
