package au.org.scoutmaster.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Defines a relationship to an contact.
 * 
 * Several entities may have relationships to a contact. 
 * e.g. 
 * Contact, Organisation, School or Household.
 * 
 * @author bsutton
 *
 */
@Entity(name="Relationship")
@Table(name="Relationship")
public class Relationship extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The contact on the Left Hand Side (LHS) of the
	 * relationship type.
	 * 
	 */
	@ManyToOne
	Contact lhs;
	
	/**
	 * The type of relationship
	 */
	@ManyToOne
	@NotNull
	RelationshipType type;
	
	/**
	 * The contact on the Right Hand Side (RHS) of the
	 * relationship type.
	 */
	@ManyToOne
	Contact rhs;
	
	
	public void setLHS(Contact lhs)
	{
		this.lhs = lhs;
	}
	
	public String toString()
	{
		return lhs.getFullname() + " " + type.lhs + " " + rhs.getFullname();
	}

	@Override
	public String getName()
	{
		return toString();
	}
	
}
