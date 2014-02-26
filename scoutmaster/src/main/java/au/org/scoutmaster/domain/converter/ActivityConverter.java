package au.org.scoutmaster.domain.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Activity;

public class ActivityConverter extends BaseConverter<Activity>
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger();
	private static final long serialVersionUID = 1L;

	@Override
	public Class<Activity> getModelType()
	{
		return Activity.class;
	}

	@Override
	protected Activity newInstance(Object value)
	{
		return new Activity();
	}

	@Override
	protected JpaBaseDao<Activity, Long> getDao()
	{
		return new DaoFactory().getActivityDao();
	}
}
