package au.org.scoutmaster.domain.converter;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.BaseEntity;

import com.vaadin.data.util.converter.Converter;

public abstract class BaseConverter<E extends BaseEntity> implements Converter<Object, E>
{
	private static Logger logger = LogManager.getLogger(BaseConverter.class);
	private static final long serialVersionUID = 1L;

	@Override
	public E convertToModel(final Object value, final Class<? extends E> targetType, final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		E result = null;
		final JpaBaseDao<E, Long> daoBaseEntity = getDao();

		BaseConverter.logger.debug("converToModel: value: {}  valueType: {} targetType: {}", value,
				value != null ? value.getClass() : "null", targetType);

		if (value instanceof Long)
		{
			BaseConverter.logger.debug("Calling findById");
			result = daoBaseEntity.findById((Long) value);
		}
		else if (value instanceof Object || value instanceof String)
		{
			result = newInstance(value);
		}

		BaseConverter.logger.debug("result: {}");
		return result;
	}

	abstract protected E newInstance(Object value);

	abstract protected JpaBaseDao<E, Long> getDao();

	@Override
	public Object convertToPresentation(final E entity, final Class<? extends Object> targetType, final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		String result = "0";

		BaseConverter.logger.debug("convertToPresentation: value {} targetType:{}", entity, targetType);
		if (entity != null)
		{
			result = entity.toString();
		}
		else
		{
			result = "";
		}
		BaseConverter.logger.debug("result: {}", result);
		return result;
	}

	@Override
	public Class<Object> getPresentationType()
	{
		return Object.class;
	}

}
