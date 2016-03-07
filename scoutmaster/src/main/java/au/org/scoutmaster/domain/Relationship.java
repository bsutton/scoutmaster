package au.org.scoutmaster.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.valid4j.Assertive.require;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import au.com.vaadinutils.crud.ChildCrudEntity;
import au.com.vaadinutils.dao.JpaEntityHelper;

/**
 * Defines a relationship to an contact.
 *
 * Several entities may have relationships to a contact. e.g. Contact,
 * Organisation, School or Household.
 *
 * @author bsutton
 *
 */
@Entity(name = "Relationship")
@Multitenant
@TenantDiscriminatorColumn(name = "ScoutGroup_ID")
@Table(name = "Relationship")
@Access(AccessType.FIELD)
@NamedQueries(
{ @NamedQuery(name = Relationship.FIND, query = "SELECT relationship FROM Relationship relationship WHERE relationship.lhs = :lhs and relationship.type = :type and relationship.rhs = :rhs"), })

public class Relationship extends BaseEntity implements ChildCrudEntity
{
	private static final long serialVersionUID = 1L;

	static public final String FIND = "Relationship.find";

	/**
	 * Entities used in child cruds must have a guid to help uniquely identify
	 * each child.
	 */
	@NotNull
	@Column(updatable = false)
	String guid = JpaEntityHelper.getGuid(this);

	/**
	 * The contact on the Left Hand Side (LHS) of the relationship type.
	 *
	 */
	@ManyToOne(targetEntity = Contact.class)
	@JoinColumn(name = "lhs_id")
	Contact lhs;

	/**
	 * The type of relationship
	 */
	@ManyToOne(targetEntity = RelationshipType.class)
	@NotNull
	RelationshipType type;

	/**
	 * The contact on the Right Hand Side (RHS) of the relationship type.
	 */
	@ManyToOne(targetEntity = Contact.class)
	Contact rhs;

	public Relationship()
	{

	}

	public Relationship(Contact lhs, RelationshipType type, Contact rhs)
	{
		require(lhs, is(notNullValue()));
		require(type, is(notNullValue()));
		require(rhs, is(notNullValue()));
		this.lhs = lhs;
		this.type = type;
		this.rhs = rhs;
	}

	public void setLHS(final Contact lhs)
	{
		this.lhs = lhs;
	}

	@Override
	public String toString()
	{
		return this.lhs.getFullname() + " " + this.type.lhs + " " + this.rhs.getFullname();
	}

	@Override
	public String getName()
	{
		return toString();
	}

	@Override
	public String getGuid()
	{
		return guid;
	}

	public Contact getRHS()
	{
		return this.rhs;
	}

	public Contact getLHS()
	{
		return this.lhs;
	}

	public RelationshipType getReciprocalType()
	{
		return this.type.getReciprocalType();
	}

	public RelationshipType getType()
	{
		return this.type;
	}

}
