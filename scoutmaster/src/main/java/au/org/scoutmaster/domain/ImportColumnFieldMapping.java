package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity(name = "ImportColumnFieldMapping")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "ImportColumnFieldMapping")
@Access(AccessType.FIELD)
public class ImportColumnFieldMapping extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false, targetEntity = ImportUserMapping.class)
	private ImportUserMapping userMapping;

	/**
	 * Name of the CSV column that is being mapped
	 */
	String csvColumnName;

	/**
	 * name of the database table field (column) that we are mapping the csv
	 * column to.
	 */
	String dbFieldName;

	public ImportColumnFieldMapping()
	{

	}

	public ImportColumnFieldMapping(final String csvColumnName, final String dbFieldName)
	{
		this.csvColumnName = csvColumnName;
		this.dbFieldName = dbFieldName;
	}

	@Override
	public String toString()
	{
		return this.csvColumnName + " -> " + this.dbFieldName;

	}

	public void setUserMapping(final ImportUserMapping importUserMapping)
	{
		this.userMapping = importUserMapping;
	}

	@Override
	public String getName()
	{
		return toString();
	}

}
