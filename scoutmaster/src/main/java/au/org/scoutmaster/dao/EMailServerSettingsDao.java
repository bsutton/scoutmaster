package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;

import au.org.scoutmaster.domain.EMailServerSettings;

public class EMailServerSettingsDao extends JpaBaseDao<EMailServerSettings, Long> implements Dao<EMailServerSettings, Long>
{

	public EMailServerSettingsDao()
	{
		// inherit the default per request em. 
	}
	public EMailServerSettingsDao(EntityManager em)
	{
		super(em);
	}

	@Override
	public List<EMailServerSettings> findAll()
	{
		return super.findAll(EMailServerSettings.FIND_ALL);
	}
	public EMailServerSettings findSettings()
	{
		EMailServerSettings settings = null;
		List<EMailServerSettings> list = findAll();
		
		if (list.size() > 1)
			throw new IllegalStateException("Found more than 1 EMailServerSetting");
		else if (list.size() == 1)
			settings = list.get(0);
		else if (list.size() == 0)
		{
			// If not initialised before then do it now.
			settings = new EMailServerSettings();
			settings.setAuthRequired(false);
			settings.setSmtpPort(25);
			settings.setSmtpFQDN("localhost");
			this.persist(settings);
		}
		
		return settings;
	}
}
