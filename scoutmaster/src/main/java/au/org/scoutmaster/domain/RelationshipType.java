package au.org.scoutmaster.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Defines the set of relationships between contacts and
 * schools, organisations, households or other contacts.
 * 
 * @author bsutton
 *
 */

@Entity(name="RelationshipType")
@Table(name="RelationshipType")
public class RelationshipType extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	public enum Type {CONTACT, ORGANISATION, SCHOOL, HOUSEHOLD};
	
	@NotBlank
	String lhs;
	
	@Enumerated(EnumType.STRING)
	Type lhsType;
	
	@NotBlank
	String rhs;
	
	@Enumerated(EnumType.STRING)
	Type rhsType;
	
	public RelationshipType()
	{
		
	}
	public RelationshipType(String lhs, Type lhsType, String rhs, Type rhsType)
	{
		this.lhs = lhs;
		this.lhsType = lhsType;
		this.rhs = rhs;
		this.rhsType = rhsType;
	}

	
}
