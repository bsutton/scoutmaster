package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.hibernate.validator.constraints.NotBlank;

/*
 * Used to define different types of qualifications required by leaders, parents and scouts.
 */
@Entity(name = "QualificationType")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "QualificationType")
@Access(AccessType.FIELD)
public class QualificationType extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * A short descriptive name for the qualification Type.
	 */
	@NotBlank
	@Column(unique = true)
	String name;

	/**
	 * A detailed description of the qualification potentially containing HTML
	 * formating.
	 */
	@NotBlank
	String description;

	/**
	 * If try then this type of qualification expires after a defined period of
	 * time.
	 */
	Boolean expires;

	/**
	 * The default period of time that the qualifications is valid for.
	 */
	@Embedded
	Period validFor;

	@Override
	public String getName()
	{
		return this.name;
	}

}
