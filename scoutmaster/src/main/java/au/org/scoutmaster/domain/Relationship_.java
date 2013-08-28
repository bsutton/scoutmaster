package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-27T23:08:58.280+1000")
@StaticMetamodel(Relationship.class)
public class Relationship_ extends BaseEntity_ {
	public static volatile SingularAttribute<Relationship, Contact> first;
	public static volatile SingularAttribute<Relationship, RelationshipType> type;
	public static volatile SingularAttribute<Relationship, Contact> secondContact;
	public static volatile SingularAttribute<Relationship, Organisation> organisation;
	public static volatile SingularAttribute<Relationship, School> school;
	public static volatile SingularAttribute<Relationship, Household> household;
}
