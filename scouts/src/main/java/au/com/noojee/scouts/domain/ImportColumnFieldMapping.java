package au.com.noojee.scouts.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ImportColumnFieldMapping
{
	public ImportColumnFieldMapping()
	{
		
	}
	
	public ImportColumnFieldMapping(String csvColumnName, String dbFieldName)
	{
		this.csvColumnName = csvColumnName;
		this.dbFieldName = dbFieldName;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	
	@ManyToOne(optional=false)
	private ImportUserMapping userMapping;

	
	/**
	 * Name of the CSV column that is being mapped
	 */
	String csvColumnName;
	
	/**
	 * name of the database table field (column) that we are mapping the csv column to.
	 */
	String dbFieldName;
	
	public String toString()
	{
		return csvColumnName + " -> " + dbFieldName;
				
	}

	public void setUserMapping(ImportUserMapping importUserMapping)
	{
		userMapping = importUserMapping;
	}

}
