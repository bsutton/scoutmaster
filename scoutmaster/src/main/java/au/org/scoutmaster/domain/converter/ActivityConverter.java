package au.org.scoutmaster.domain.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.CommunicationLog;

public class ActivityConverter extends BaseConverter<CommunicationLog>
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger();
	private static final long serialVersionUID = 1L;

	@Override
	public Class<CommunicationLog> getModelType()
	{
		return CommunicationLog.class;
	}

	@Override
	protected CommunicationLog newInstance(final Object value)
	{
		return new CommunicationLog();
	}

	@Override
	protected JpaBaseDao<CommunicationLog, Long> getDao()
	{
		return new DaoFactory().getCommunicationLogDao();
	}
}
