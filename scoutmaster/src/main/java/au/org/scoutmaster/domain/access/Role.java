package au.org.scoutmaster.domain.access;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import au.org.scoutmaster.domain.BaseEntity;
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
@Table(name="Role")
public class Role extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/** 
	 * The name of the role
	 */
	@NotBlank
	@Column(unique=true)
	String name;
	
	/**
	 * A meaningful description of the the roles purpose.
	 */
	@NotBlank
	String description;
	
	/**
	 * The set of features this role is permitted to access.
	 */
	@ManyToMany
	List<Feature> permitted = new ArrayList<>();

}
