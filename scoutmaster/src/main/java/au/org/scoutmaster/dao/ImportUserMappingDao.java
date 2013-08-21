package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import au.org.scoutmaster.domain.ImportColumnFieldMapping;
import au.org.scoutmaster.domain.ImportUserMapping;


public class ImportUserMappingDao extends JpaBaseDao<ImportUserMapping, Long> implements Dao<ImportUserMapping, Long>
{
	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(ImportUserMappingDao.class);

	public ImportUserMappingDao()
	{
		// inherit the default per request em. 
	}
	public ImportUserMappingDao(EntityManager em)
	{
		super(em);
	}

	@Override
	public List<ImportUserMapping> findAll()
	{
		return super.findAll(ImportUserMapping.FIND_ALL);
	}

	public List<ImportUserMapping> findByName(String name)
	{
		return super.findListBySingleParameter(ImportUserMapping.FIND_BY_NAME, "name", name);
	}
	
	public void addColumnFieldMapping(ImportUserMapping importMapping, ImportColumnFieldMapping columnMapping)
	{
		columnMapping.setUserMapping(importMapping);
		importMapping.getColumnFieldMappings().add(columnMapping);
	}

	public void clearMappings(ImportUserMapping importMapping)
	{
		importMapping.getColumnFieldMappings().clear();

	}


}
