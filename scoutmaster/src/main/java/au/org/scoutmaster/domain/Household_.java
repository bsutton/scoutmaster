package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-11-24T19:08:01.006+1100")
@StaticMetamodel(Household.class)
public class Household_ extends BaseEntity_ {
	public static volatile SingularAttribute<Household, String> name;
	public static volatile SingularAttribute<Household, Address> location;
	public static volatile SingularAttribute<Household, Contact> primaryContact;
	public static volatile ListAttribute<Household, Relationship> members;
	public static volatile ListAttribute<Household, Note> notes;
	public static volatile ListAttribute<Household, Tag> tags;
	public static volatile ListAttribute<Household, CommunicationLog> activites;
}
