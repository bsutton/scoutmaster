package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="OrganisationType")
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
		return name;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	
}
