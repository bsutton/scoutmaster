package au.org.scoutmaster.domain.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.CommunicationType;

public class ActivityTypeConverter extends BaseConverter<CommunicationType>
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger();
	private static final long serialVersionUID = 1L;

	@Override
	public Class<CommunicationType> getModelType()
	{
		return CommunicationType.class;
	}

	@Override
	protected CommunicationType newInstance(Object value)
	{
		return new CommunicationType((String)value, null);
	}

	@Override
	protected JpaBaseDao<CommunicationType, Long> getDao()
	{
		return new DaoFactory().getActivityTypeDao();
	}
}
