package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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
	public ImportUserMapping findById(Long id)
	{
		ImportUserMapping importUserMapping = entityManager.find(this.entityClass, id);
		return importUserMapping;
	}

	@Override
	public List<ImportUserMapping> findAll()
	{
		Query query = entityManager.createNamedQuery(ImportUserMapping.FIND_ALL);
		@SuppressWarnings("unchecked")
		List<ImportUserMapping> list = query.getResultList();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<ImportUserMapping> findByName(String name)
	{
		Query query = entityManager.createNamedQuery(ImportUserMapping.FIND_BY_NAME);
		query.setParameter("name", name);
		List<ImportUserMapping> results = query.getResultList();
		return results;
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
