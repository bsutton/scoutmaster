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
 * Used to save the set of csv fields to Entity field mappings that the user has selected during the import.
 *  
 * @author bsutton
 *
 */
@Entity
@NamedQueries(
{ 
	@NamedQuery(name = "ImportUserMapping.findAll", query = "SELECT import FROM ImportUserMapping import"),
	@NamedQuery(name = "ImportUserMapping.findByName", query = "SELECT import FROM ImportUserMapping import WHERE import.mappingName = :name"), 
})

public class ImportUserMapping extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	// Name of this save import mapping
	private String mappingName;
	
	@OneToMany(mappedBy = "userMapping", cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	//@JoinColumn(name="importUserMappingId")
	private List<ImportColumnFieldMapping> columnFieldMappings = new ArrayList<>();
	
	
	public ImportUserMapping()
	{ 
	}

	public ImportUserMapping(String mappingName)
	{
		this.mappingName = mappingName;
	}
	
	public void addColumnFieldMapping(ImportColumnFieldMapping mapping)
	{
		mapping.setUserMapping(this);
		columnFieldMappings.add(mapping);
	}

	public void clearMappings()
	{
		columnFieldMappings.clear();
		
	}

	public void setName(String mappingName)
	{
		this.mappingName = mappingName;
	}
	

	
}
