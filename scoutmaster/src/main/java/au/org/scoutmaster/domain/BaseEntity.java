package au.org.scoutmaster.domain;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import au.com.vaadinutils.crud.CrudEntity;

/**
 * This class is an abstract superclass for all Entity classes in the
 * application. This class defines variables which are common for all entity
 * classes.
 *
 * @author Kim
 *
 */
@MappedSuperclass
@Access(AccessType.FIELD)
abstract public class BaseEntity implements Serializable, CrudEntity
{

	private static final long serialVersionUID = -7289994339186082141L;

	public static final String ID = "id";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	protected Long id;

	@Column(name = "created")
	private Date created = new Date(new java.util.Date().getTime());

	// TODO: how do we get this updated each time.
	@Column(name = "updated")
	private Date updated = new Date(new java.util.Date().getTime());

	@Column(nullable = false, name = "CONSISTENCYVERSION")
	@Version
	protected Long consistencyVersion;

	public BaseEntity()
	{

	}

	public Date getCreated()
	{
		return this.created;
	}

	public Date getUpdated()
	{
		return this.updated;
	}

	/**
	 * Get the primary key for this entity.
	 *
	 * @return Primary key
	 */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Set the primary key for this entity. Usually, this method should never be
	 * called.
	 *
	 * @param id
	 *            New primary key
	 */
	@Override
	public void setId(final Long id)
	{
		this.id = id;
	}

	/**
	 * Get the concurrency version number for this entity. The concurrency
	 * version is a number which is used for optimistic locking in the database.
	 *
	 * @return Current consistency version
	 */
	public Long getConsistencyVersion()
	{
		return this.consistencyVersion;
	}

	/**
	 * Set the concurrency version number for this entity. Usually, this method
	 * should never be called.
	 *
	 * @param consistencyVersion
	 *            New consistency version
	 */
	public void setConsistencyVersion(final Long consistencyVersion)
	{
		this.consistencyVersion = consistencyVersion;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.consistencyVersion == null) ? 0 : this.consistencyVersion.hashCode());
		result = prime * result + ((this.created == null) ? 0 : this.created.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.updated == null) ? 0 : this.updated.hashCode());
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
		BaseEntity other = (BaseEntity) obj;
		if (this.consistencyVersion == null)
		{
			if (other.consistencyVersion != null)
				return false;
		}
		else if (!this.consistencyVersion.equals(other.consistencyVersion))
			return false;
		if (this.created == null)
		{
			if (other.created != null)
				return false;
		}
		else if (!this.created.equals(other.created))
			return false;
		if (this.id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!this.id.equals(other.id))
			return false;
		if (this.updated == null)
		{
			if (other.updated != null)
				return false;
		}
		else if (!this.updated.equals(other.updated))
			return false;
		return true;
	}

}
