package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-15T13:07:13.905+1000")
@StaticMetamodel(Relationship.class)
public class Relationship_ extends BaseEntity_ {
	public static volatile SingularAttribute<Relationship, Contact> lhs;
	public static volatile SingularAttribute<Relationship, RelationshipType> type;
	public static volatile SingularAttribute<Relationship, Contact> rhs;
}
