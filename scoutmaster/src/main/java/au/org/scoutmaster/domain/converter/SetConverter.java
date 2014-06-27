package au.org.scoutmaster.domain.converter;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.dao.JpaBaseDao;

import com.vaadin.data.util.converter.Converter;

public class SetConverter<E, D extends JpaBaseDao<E, Long>> implements Converter<Set<? extends Object>, Set<E>>
{
	private static Logger logger = LogManager.getLogger(SetConverter.class);
	private static final long serialVersionUID = 1L;
	private final D dao;

	SetConverter(final D dao)
	{
		this.dao = dao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<E> convertToModel(final Set<? extends Object> value, final Class<? extends Set<E>> targetType,
			final Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
			{
		HashSet<E> result = null;

		SetConverter.logger.debug("converToModel: value: {} valueType: {} targetType: {}", value,
				value != null ? value.getClass() : "null", targetType);

		if (value instanceof Set)
		{
			SetConverter.logger.debug("Calling findByName");
			final HashSet<Object> set = (HashSet<Object>) value;
			result = new HashSet<E>();
			for (final Object item : set.toArray())
			{
				// Now we have to determine if the element is an identity or an
				// actual E
				if (item instanceof Long)
				{
					result.add(this.dao.findById((Long) item));
				}
				else
				{
					result.add((E) item);
				}
			}
		}

		SetConverter.logger.debug("result: {}", result);
		return result;
			}

	@SuppressWarnings("unchecked")
	@Override
	public Set<? extends Object> convertToPresentation(final Set<E> value,
			final Class<? extends Set<? extends Object>> targetType, final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
					{
		Set<? extends Object> result = new HashSet<>();
		if (targetType.getName().equals("java.util.Set"))
		{
			result = new HashSet<E>();

			SetConverter.logger.debug("convertToPresentation: value: {}  targetType: {}", value, targetType);
			if (value != null)
			{
				((HashSet<E>) result).addAll(value);
			}
		}
		else
		{
			throw new UnsupportedOperationException("Conversion for E from type " + targetType + " not supported");
		}

		SetConverter.logger.debug("result: {}", result);
		return result;
					}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Set<E>> getModelType()
	{
		final Set<E> a = new HashSet<>();
		return (Class<Set<E>>) a.getClass();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Set<? extends Object>> getPresentationType()
	{
		final Set<Object> a = new HashSet<>();
		return (Class<Set<? extends Object>>) a.getClass();
	}

}
