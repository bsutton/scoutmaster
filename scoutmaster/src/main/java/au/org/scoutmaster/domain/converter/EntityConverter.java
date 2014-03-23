package au.org.scoutmaster.domain.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.BaseEntity;

public class EntityConverter<E extends BaseEntity> extends BaseConverter<E>
{
	private static Logger logger = LogManager.getLogger(EntityConverter.class);
	private static final long serialVersionUID = 1L;
	
	private Class<E> type;

	@Override
	public Class<E> getModelType()
	{
		return type;
	}

	@Override
	protected E newInstance(Object value)
	{
		E newInstance = null;
		try
		{
			 newInstance = type.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			// should never happen
			logger.error(e);
		}
		return newInstance;
	}

	@Override
	protected JpaBaseDao<E, Long> getDao()
	{
		return DaoFactory.getGenericDao(type);
	}
}
