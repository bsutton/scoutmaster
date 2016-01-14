package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "Relationship")
@Access(AccessType.FIELD)
public class Relationship extends BaseEntity implements ChildCrudEntity
{
	private static final long serialVersionUID = 1L;

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

}
