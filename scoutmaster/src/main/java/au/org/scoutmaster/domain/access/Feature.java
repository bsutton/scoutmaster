package au.org.scoutmaster.domain.access;

import javax.persistence.Entity;

import au.org.scoutmaster.domain.BaseEntity;

/**
 * Features are used to describe a user accessible feature of scoutmaster.
 * 
 * A role is really defined by the set of features that a User that belongs to that role has accessed to.
 * @author bsutton
 *
 */
@Entity
public class Feature extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * An informal hierarchical descriptor of the feature
	 * e.g.  Contact.list, Contact.edit, Contact.delete
	 */
	String descriptor;
	
	String description;

}
