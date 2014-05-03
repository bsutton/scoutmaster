package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.accounting.Money;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(RaffleBook.class)
public class RaffleBook_ extends BaseEntity_
{

	public static volatile SingularAttribute<RaffleBook, RaffleAllocation> raffleAllocation;
	public static volatile SingularAttribute<RaffleBook, Integer> ticketCount;
	public static volatile SingularAttribute<RaffleBook, String> notes;
	public static volatile SingularAttribute<RaffleBook, Integer> firstNo;
	public static volatile SingularAttribute<RaffleBook, Raffle> raffle;
	public static volatile SingularAttribute<RaffleBook, Money> amountReturned;
	public static volatile SingularAttribute<RaffleBook, Integer> ticketsReturned;
	public static volatile SingularAttribute<RaffleBook, Date> dateReturned;
	public static volatile SingularAttribute<RaffleBook, Boolean> receiptIssued;
	public static volatile SingularAttribute<RaffleBook, Contact> collectedBy;

}