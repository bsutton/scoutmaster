package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Defines the set of relationships between contacts and schools, organisations,
 * households or other contacts.
 *
 * @author bsutton
 *
 */

@Entity(name = "RelationshipType")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "RelationshipType")
@Access(AccessType.FIELD)
public class RelationshipType extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	public enum Type
	{
		CONTACT, ORGANISATION, SCHOOL, HOUSEHOLD
	};

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

	public RelationshipType(final String lhs, final Type lhsType, final String rhs, final Type rhsType)
	{
		this.lhs = lhs;
		this.lhsType = lhsType;
		this.rhs = rhs;
		this.rhsType = rhsType;
	}

	@Override
	public String toString()
	{
		return this.lhs;
	}

	@Override
	public String getName()
	{
		return toString();
	}

}
