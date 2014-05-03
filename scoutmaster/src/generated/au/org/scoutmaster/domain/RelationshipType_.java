package au.org.scoutmaster.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import au.org.scoutmaster.domain.RelationshipType.Type;

@Generated(value = "EclipseLink-2.5.1.v20130918-rNA", date = "2014-04-24T16:11:50")
@StaticMetamodel(RelationshipType.class)
public class RelationshipType_ extends BaseEntity_
{

	public static volatile SingularAttribute<RelationshipType, Type> lhsType;
	public static volatile SingularAttribute<RelationshipType, String> lhs;
	public static volatile SingularAttribute<RelationshipType, String> rhs;
	public static volatile SingularAttribute<RelationshipType, Type> rhsType;

}