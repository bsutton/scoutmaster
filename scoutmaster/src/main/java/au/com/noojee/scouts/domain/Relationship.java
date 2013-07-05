package au.com.noojee.scouts.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Defines a relationship between two contacts or a contact
 * and an Organisation, School or Household.
 * 
 * @author bsutton
 *
 */
@Entity
public class Relationship
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

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
