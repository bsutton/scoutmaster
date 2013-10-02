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
	 * The type of relationship
	 */
	@ManyToOne
	@NotNull
	RelationshipType type;
	
	/**
	 * The contact this relation links to.
	 * of the following.
	 */
	@ManyToOne
	Contact related;
	
}
