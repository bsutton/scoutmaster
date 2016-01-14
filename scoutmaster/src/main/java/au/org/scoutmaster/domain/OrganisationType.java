package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@Table(name = "OrganisationType")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Access(AccessType.FIELD)
public class OrganisationType extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The name of the organisation type.
	 */
	@NotNull
	private String name;

	/*
	 * A description of the use or the organisation type.
	 */
	private String description;

	public OrganisationType()
	{

	}

	@Override
	public String toString()
	{
		return this.name;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

}
