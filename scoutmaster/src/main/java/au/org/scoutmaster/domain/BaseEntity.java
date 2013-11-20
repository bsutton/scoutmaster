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
	@Column(name="id")
	protected Long id;
	
	@Column(name="created")
	private Date created = new Date(new java.util.Date().getTime());
	
	// TODO: how do we get this updated each time.
	@Column(name="updated")
	private Date updated = new Date(new java.util.Date().getTime());

	@Column(nullable = false, name="CONSISTENCYVERSION")
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
	public Long getId()
	{
		return id;
	}

	/**
	 * Set the primary key for this entity. Usually, this method should never be
	 * called.
	 * 
	 * @param id
	 *            New primary key
	 */
	public void setId(Long id)
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
		return consistencyVersion;
	}

	/**
	 * Set the concurrency version number for this entity. Usually, this method
	 * should never be called.
	 * 
	 * @param consistencyVersion
	 *            New consistency version
	 */
	public void setConsistencyVersion(Long consistencyVersion)
	{
		this.consistencyVersion = consistencyVersion;
	}

}
