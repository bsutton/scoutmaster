package au.org.scoutmaster.domain;

import au.org.scoutmaster.domain.RelationshipType.Type;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-04-15T13:07:13.908+1000")
@StaticMetamodel(RelationshipType.class)
public class RelationshipType_ extends BaseEntity_ {
	public static volatile SingularAttribute<RelationshipType, String> lhs;
	public static volatile SingularAttribute<RelationshipType, Type> lhsType;
	public static volatile SingularAttribute<RelationshipType, String> rhs;
	public static volatile SingularAttribute<RelationshipType, Type> rhsType;
}
