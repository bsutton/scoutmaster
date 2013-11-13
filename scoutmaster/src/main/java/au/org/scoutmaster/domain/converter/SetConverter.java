package au.org.scoutmaster.domain.converter;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;

import au.com.vaadinutils.dao.JpaBaseDao;

import com.vaadin.data.util.converter.Converter;

public class SetConverter<E, D extends JpaBaseDao<E,Long>> implements Converter<Set<? extends Object>, Set<E>>
{
	private static Logger logger = Logger.getLogger(SetConverter.class);
	private static final long serialVersionUID = 1L;
	private D dao;

	SetConverter(D dao)
	{
		this.dao = dao;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Set<E> convertToModel(Set<? extends Object> value, Class<? extends Set<E>> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		HashSet<E> result = null;

		logger.debug("converToModel: value=" + value + "valueType:" + (value != null ? value.getClass() : "null")
				+ " targetType:" + targetType);

		if (value instanceof Set)
		{
			logger.debug("Calling findByName");
			HashSet<Object> set = (HashSet<Object>) value;
			result = new HashSet<E>();
			for (Object item : set.toArray())
			{
				// Now we have to determine if the element is an identity or an
				// actual E
				if (item instanceof Long)
					result.add(dao.findById((Long) item));
				else
					result.add((E) item);
			}
		}

		logger.debug("result:" + result);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<? extends Object> convertToPresentation(Set<E> value,
			Class<? extends Set<? extends Object>> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		Set<? extends Object> result = new HashSet<>();
		if (targetType.getName().equals("java.util.Set"))
		{
			result = new HashSet<E>();

			logger.debug("convertToPresentation: value" + value + " targetType:" + targetType);
			if (value != null)
				((HashSet<E>) result).addAll(value);
		}
		else
			throw new UnsupportedOperationException("Conversion for E from type " + targetType + " not supported");

		logger.debug("result: " + result.toString());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Set<E>> getModelType()
	{
		Set<E> a = new HashSet<>();
		return (Class<Set<E>>) a.getClass();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Set<? extends Object>> getPresentationType()
	{
		Set<Object> a = new HashSet<>();
		return (Class<Set<? extends Object>>) a.getClass();
	}

}
