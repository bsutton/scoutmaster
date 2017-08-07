package au.org.scoutmaster.domain.security;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import au.org.scoutmaster.domain.BaseEntity;

/**
 * A feature describes a UI feature such as the Contacts View
 *
 * A user has access to the sum of features (or more accurately the Actions they
 * can apply to those features) defined by the SecurityRoles they belong to.
 *
 * @author bsutton
 *
 */
@Entity
@Table(name = "Feature")
@Access(AccessType.FIELD)
public class Feature extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the feature.
	 */
	@NotBlank
	private String name;

	// Required by JPA
	public Feature()
	{
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public String toString()
	{
		return this.name;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Feature other = (Feature) obj;
		if (this.name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!this.name.equals(other.name))
			return false;
		return true;
	}

}
