package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * Used to save the set of 'csv field' to 'Entity field' mappings that the user has
 * selected during the import.
 * 
 * @author bsutton
 * 
 */
@Entity
@NamedQueries(
{
		@NamedQuery(name = ImportUserMapping.FIND_ALL, query = "SELECT import FROM ImportUserMapping import"),
		@NamedQuery(name = ImportUserMapping.FIND_BY_NAME, query = "SELECT import FROM ImportUserMapping import WHERE import.mappingName = :name"), })
public class ImportUserMapping extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL = "ImportUserMapping.findAll";
	public static final String FIND_BY_NAME = "ImportUserMapping.findByName";

	// Name of this save import mapping
	private String mappingName;

	@OneToMany(mappedBy = "userMapping", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	// @JoinColumn(name="importUserMappingId")
	private List<ImportColumnFieldMapping> columnFieldMappings = new ArrayList<>();

	public ImportUserMapping()
	{
	}

	public ImportUserMapping(String mappingName)
	{
		this.setMappingName(mappingName);
	}

	public void setName(String mappingName)
	{
		this.setMappingName(mappingName);
	}

	public String getMappingName()
	{
		return mappingName;
	}

	public void setMappingName(String mappingName)
	{
		this.mappingName = mappingName;
	}

	public List<ImportColumnFieldMapping> getColumnFieldMappings()
	{
		return columnFieldMappings;
	}
}
