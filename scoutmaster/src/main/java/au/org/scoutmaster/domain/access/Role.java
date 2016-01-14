package au.org.scoutmaster.domain.access;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import au.org.scoutmaster.domain.BaseEntity;

/**
 * A user can belong to one or more roles.
 *
 * A role is really defined by the set of features associated with the Role.
 *
 * A feature describes a UI feature such as the ability to create new Contacts.
 *
 * A user has access to the sum of features defined by the roles they belong to.
 *
 * @author bsutton
 *
 */
@Entity
@Table(name = "Role")
@Access(AccessType.FIELD)
public class Role extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The name of the role
	 */
	@NotBlank
	@Column(unique = true)
	String name;

	/**
	 * A meaningful description of the the roles purpose.
	 */
	@NotBlank
	String description;

	/**
	 * The set of features this role is permitted to access.
	 */
	@ManyToMany(targetEntity = Feature.class)
	List<Feature> permitted = new ArrayList<>();

	@Override
	public String getName()
	{
		return this.name;
	}

}
