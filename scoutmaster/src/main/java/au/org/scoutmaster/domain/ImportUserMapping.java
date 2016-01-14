package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

/**
 * Used to save the set of 'csv field' to 'Entity field' mappings that the user
 * has selected during the import.
 *
 * @author bsutton
 *
 */
@Entity(name = "ImportUserMapping")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "ImportUserMapping")
@Access(AccessType.FIELD)
@NamedQueries(
{ @NamedQuery(name = ImportUserMapping.FIND_BY_NAME, query = "SELECT import FROM ImportUserMapping import WHERE import.mappingName = :name"), })
public class ImportUserMapping extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_NAME = "ImportUserMapping.findByName";

	// Name of this save import mapping
	@Column(unique = true)
	private String mappingName;

	@OneToMany(mappedBy = "userMapping", fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = ImportColumnFieldMapping.class)
	// @JoinColumn(name="importUserMappingId")
	private List<ImportColumnFieldMapping> columnFieldMappings = new ArrayList<>();

	public ImportUserMapping()
	{
	}

	public ImportUserMapping(final String mappingName)
	{
		setMappingName(mappingName);
	}

	public void setName(final String mappingName)
	{
		setMappingName(mappingName);
	}

	public String getMappingName()
	{
		return null;
	}

	public void setMappingName(final String mappingName)
	{
		this.mappingName = mappingName;
	}

	public List<ImportColumnFieldMapping> getColumnFieldMappings()
	{
		return this.columnFieldMappings;
	}

	@Override
	public String getName()
	{
		return this.mappingName;
	}
}
