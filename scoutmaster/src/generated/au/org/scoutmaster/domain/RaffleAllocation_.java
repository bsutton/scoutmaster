package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(RaffleAllocation.class)
public class RaffleAllocation_ extends BaseEntity_
{

	public static volatile SingularAttribute<RaffleAllocation, Date> dateIssued;
	public static volatile SingularAttribute<RaffleAllocation, String> notes;
	public static volatile SetAttribute<RaffleAllocation, RaffleBook> books;
	public static volatile SingularAttribute<RaffleAllocation, Raffle> raffle;
	public static volatile SingularAttribute<RaffleAllocation, Contact> allocatedTo;
	public static volatile SingularAttribute<RaffleAllocation, Contact> issuedBy;
	public static volatile SingularAttribute<RaffleAllocation, Date> dateAllocated;

}