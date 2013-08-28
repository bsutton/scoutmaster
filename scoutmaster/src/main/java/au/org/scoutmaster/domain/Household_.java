package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-27T23:08:58.272+1000")
@StaticMetamodel(Household.class)
public class Household_ extends BaseEntity_ {
	public static volatile SingularAttribute<Household, String> name;
	public static volatile SingularAttribute<Household, Address> location;
	public static volatile SingularAttribute<Household, Contact> primaryContact;
	public static volatile ListAttribute<Household, Relationship> members;
	public static volatile ListAttribute<Household, Note> notes;
	public static volatile ListAttribute<Household, Tag> tags;
	public static volatile ListAttribute<Household, Activity> activites;
}
