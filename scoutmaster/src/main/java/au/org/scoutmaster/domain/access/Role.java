package au.org.scoutmaster.domain.access;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
/**
 * A user can belong to one or more roles.
 * 
 * A role is really defined by the set of features that a User that belongs to that role has accessed to.
 * 
 * A user has access to the sum of features defined by the roles they belong to. 
 *  
 * @author bsutton
 *
 */
@Entity
public class Role
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** 
	 * The name of the role
	 */
	String name;
	
	/**
	 * A meaningful description of the the roles purpose.
	 */
	String description;
	
	/**
	 * The set of features this role is permitted to access.
	 */
	@ManyToMany
	List<Feature> permitted = new ArrayList<>();

}
