package au.org.scoutmaster.domain;

import au.org.scoutmaster.domain.RelationshipType.Type;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-10-03T10:29:32.717+1000")
@StaticMetamodel(RelationshipType.class)
public class RelationshipType_ extends BaseEntity_ {
	public static volatile SingularAttribute<RelationshipType, String> lhs;
	public static volatile SingularAttribute<RelationshipType, Type> lhsType;
	public static volatile SingularAttribute<RelationshipType, String> rhs;
	public static volatile SingularAttribute<RelationshipType, Type> rhsType;
}
