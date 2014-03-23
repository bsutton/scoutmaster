package au.org.scoutmaster.domain;

import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-03-16T17:00:13.911+1100")
@StaticMetamodel(RaffleAllocation.class)
public class RaffleAllocation_ extends BaseEntity_ {
	public static volatile SingularAttribute<RaffleAllocation, Raffle> raffle;
	public static volatile SingularAttribute<RaffleAllocation, Contact> allocatedTo;
	public static volatile SingularAttribute<RaffleAllocation, Date> dateAllocated;
	public static volatile SingularAttribute<RaffleAllocation, Contact> issuedBy;
	public static volatile SingularAttribute<RaffleAllocation, Date> dateIssued;
	public static volatile SingularAttribute<RaffleAllocation, String> notes;
	public static volatile SetAttribute<RaffleAllocation, RaffleBook> books;
}
