package au.org.scoutmaster.domain.access;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.security.eRole;

/**
 * A user can belong to one or more roles.
 *
 * A role is really defined by the set of permissions associated with the Role.
 *
 * A feature describes a UI feature such as the Contacts View
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
	// @Column(unique = true)
	@Enumerated(EnumType.STRING)
	private eRole erole;

	/**
	 * A meaningful description of the the roles purpose.
	 */
	@NotBlank
	private String description;

	/**
	 * The set of permission associated with this role
	 */
	@ManyToMany(targetEntity = Permission.class, mappedBy = "accessibleBy")
	List<Permission> permitted = new ArrayList<>();

	// Required by JPA
	public Role()
	{
	}

	public Role(eRole erole)
	{
		this.erole = erole;
		this.description = erole.getLabel();
	}

	@Override
	public String getName()
	{
		return this.erole.name();
	}

	public eRole getERole()
	{
		return this.erole;
	}

	public void addPermission(Permission feature)
	{
		this.permitted.add(feature);
	}

	@Override
	public String toString()
	{
		return this.erole.name();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.erole == null) ? 0 : this.erole.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (this.erole != other.erole)
			return false;
		return true;
	}
}
