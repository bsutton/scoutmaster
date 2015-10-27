package au.org.scoutmaster.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.ImportColumnFieldMapping;
import au.org.scoutmaster.domain.ImportUserMapping;

public class ImportUserMappingDao extends JpaBaseDao<ImportUserMapping, Long> implements Dao<ImportUserMapping, Long>
{
	@SuppressWarnings("unused")
	static private Logger logger = LogManager.getLogger(ImportUserMappingDao.class);

	public ImportUserMappingDao()
	{
		// inherit the default per request em.
	}

	

	public List<ImportUserMapping> findByName(final String name)
	{
		return super.findListBySingleParameter(ImportUserMapping.FIND_BY_NAME, "name", name);
	}

	public void addColumnFieldMapping(final ImportUserMapping importMapping,
			final ImportColumnFieldMapping columnMapping)
	{
		columnMapping.setUserMapping(importMapping);
		importMapping.getColumnFieldMappings().add(columnMapping);
	}

	public void clearMappings(final ImportUserMapping importMapping)
	{
		importMapping.getColumnFieldMappings().clear();

	}

	@Override
	public JPAContainer<ImportUserMapping> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

}
