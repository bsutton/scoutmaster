package au.org.scoutmaster.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class ImportColumnFieldMapping extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	public ImportColumnFieldMapping()
	{

	}

	public ImportColumnFieldMapping(String csvColumnName, String dbFieldName)
	{
		this.csvColumnName = csvColumnName;
		this.dbFieldName = dbFieldName;
	}

	@ManyToOne(optional = false)
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

	@Override
	public String toString()
	{
		return this.csvColumnName + " -> " + this.dbFieldName;

	}

	public void setUserMapping(ImportUserMapping importUserMapping)
	{
		this.userMapping = importUserMapping;
	}

}