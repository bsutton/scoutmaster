package au.com.noojee.scouts.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Defines the set of relationships between contacts and
 * schools, organisations, households or other contacts.
 * 
 * @author bsutton
 *
 */

@Entity
public class RelationshipType
{

	public enum Type {CONTACT, ORGANISATION, SCHOOL, HOUSEHOLD};
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	String lhs;
	Type lhsType;
	String rhs;
	Type rhsType;
	
	public RelationshipType(String lhs, Type lhsType, String rhs, Type rhsType)
	{
		this.lhs = lhs;
		this.lhsType = lhsType;
		this.rhs = rhs;
		this.rhsType = rhsType;
	}

	
}
