package au.org.scoutmaster.domain.access;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Features are used to describe a user accessible feature of scoutmaster.
 * 
 * A role is really defined by the set of features that a User that belongs to that role has accessed to.
 * @author bsutton
 *
 */
@Entity
public class Feature
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * An informal hierarchical descriptor of the feature
	 * e.g.  Contact.list, Contact.edit, Contact.delete
	 */
	String descriptor;
	
	String description;

}
