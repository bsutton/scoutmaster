package au.org.scoutmaster.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Defines a relationship between two contacts or a contact
 * and an Organisation, School or Household.
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
	 * The first contact in the relationship.
	 * 
	 * Relationships are bi-directional so being the first
	 * doesn't mean much.
	 */
	@ManyToOne
	Contact first;
	
	@ManyToOne
	RelationshipType type;
	
	/**
	 * The relationship must have ONE AND ONLY ONE
	 * of the following.
	 */
	@ManyToOne
	Contact secondContact;
	
	@ManyToOne
	Organisation organisation;
	
	@ManyToOne
	School school;
	
	@ManyToOne
	Household household;
}
