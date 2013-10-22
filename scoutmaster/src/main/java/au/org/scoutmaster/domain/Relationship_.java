package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-10-19T18:28:36.384+1100")
@StaticMetamodel(Relationship.class)
public class Relationship_ extends BaseEntity_ {
	public static volatile SingularAttribute<Relationship, Contact> lhs;
	public static volatile SingularAttribute<Relationship, RelationshipType> type;
	public static volatile SingularAttribute<Relationship, Contact> rhs;
}
