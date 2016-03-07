package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.hibernate.validator.constraints.NotBlank;

import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.RelationshipTypeDao;

/**
 * Defines the set of relationships between contacts and schools, organisations,
 * households or other contacts.
 *
 * @author bsutton
 *
 */

@Entity(name = "RelationshipType")
@Multitenant
@TenantDiscriminatorColumn(name = "ScoutGroup_ID")
@Table(name = "RelationshipType")
@Access(AccessType.FIELD)
@NamedQueries(
{ @NamedQuery(name = RelationshipType.FIND_BY_TYPE, query = "SELECT relationshiptype FROM RelationshipType relationshiptype WHERE relationshiptype.lhs = :lhs and relationshiptype.lhsType = :lhsType"), })

public class RelationshipType extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	public final static String FIND_BY_TYPE = "RelationshipType.FIND_BY_TYPE";

	public enum Type
	{
		CONTACT, ORGANISATION, SCHOOL, HOUSEHOLD
	};

	/**
	 * Describest the role of the lhs contact in the relationship e.g. Parent of
	 */
	@NotBlank
	String lhs;

	/**
	 * Describes the type of relationship e.g. Contact, Organsisation, Household
	 */
	@Enumerated(EnumType.STRING)
	Type lhsType;

	/**
	 * Describest the role of the rhs contact in the relationship e.g. Child of
	 */
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

	/**
	 * The LHS represents the type of a relationship. The RHS represents the
	 * Reciprocal type of the relationship
	 *
	 * e.g. if LHS is 'Child of' then the reciprocal is 'Parent of'
	 *
	 * @return
	 */
	public RelationshipType getReciprocalType()
	{
		RelationshipTypeDao dao = new DaoFactory().getRelationshipTypeDao();

		RelationshipType type = dao.find(rhs, rhsType);

		return type;
	}

}
