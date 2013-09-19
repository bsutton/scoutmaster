package au.org.scoutmaster.domain.converter;

import org.apache.log4j.Logger;

import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.JpaBaseDao;
import au.org.scoutmaster.domain.ActivityType;

public class ActivityTypeConverter extends BaseConverter<ActivityType>
{
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ActivityTypeConverter.class);
	private static final long serialVersionUID = 1L;

	@Override
	public Class<ActivityType> getModelType()
	{
		return ActivityType.class;
	}

	@Override
	protected ActivityType newInstance(Object value)
	{
		return new ActivityType((String)value, null);
	}

	@Override
	protected JpaBaseDao<ActivityType, Long> getDao()
	{
		return new DaoFactory().getActivityTypeDao();
	}
}
