package au.org.scoutmaster.domain.access;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import au.org.scoutmaster.domain.BaseEntity;

/**
 * Features are used to describe a user accessible feature of scoutmaster.
 *
 * A role is really defined by the set of features that a User that belongs to
 * that role has accessed to.
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
	 * An informal hierarchical descriptor of the feature e.g. Contact.list,
	 * Contact.edit, Contact.delete
	 */
	@NotBlank
	@Column(unique = true)
	String descriptor;

	@NotBlank
	String description;

	@Override
	public String getName()
	{
		return this.description;
	}

}
