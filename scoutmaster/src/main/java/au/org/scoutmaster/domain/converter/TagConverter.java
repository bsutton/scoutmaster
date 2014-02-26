package au.org.scoutmaster.domain.converter;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.org.scoutmaster.dao.TagDao;
import au.org.scoutmaster.domain.Tag;

import com.vaadin.data.util.converter.Converter;

public class TagConverter implements Converter<Set<? extends Object>, Set<Tag>>
{
	private static Logger logger = LogManager.getLogger(TagConverter.class);
	private static final long serialVersionUID = 1L;

	@Override
	public Set<Tag> convertToModel(Set<? extends Object> value, Class<? extends Set<Tag>> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		HashSet<Tag> result = null;
		TagDao daoTag = new TagDao();

		logger.debug("converToModel: value: {}  valueType: {} targetType: {}", value, (value != null ? value.getClass()
				: "null"), targetType);

		if (value instanceof Set)
		{
			logger.debug("Calling findByName");
			@SuppressWarnings("unchecked")
			HashSet<Object> set = (HashSet<Object>) value;
			result = new HashSet<Tag>();
			for (Object item : set.toArray())
			{
				// Now we have to determine if the element is an identity or an
				// actual tag
				if (item instanceof Long)
					result.add(daoTag.findById((Long) item));
				else
					result.add((Tag) item);
			}
		}

		logger.debug("result: {}", result);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<? extends Object> convertToPresentation(Set<Tag> value,
			Class<? extends Set<? extends Object>> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		Set<? extends Object> result = new HashSet<>();
		if (targetType.getName().equals("java.util.Set"))
		{
			result = new HashSet<Tag>();

			logger.debug("convertToPresentation: value: {} targetType: {}", value, targetType);
			if (value != null)
				((HashSet<Tag>) result).addAll(value);
		}
		else
			throw new UnsupportedOperationException("Conversion for Tag from type " + targetType + " not supported");

		logger.debug("result: {}", result);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Set<Tag>> getModelType()
	{
		Set<Tag> a = new HashSet<>();
		return (Class<Set<Tag>>) a.getClass();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Set<? extends Object>> getPresentationType()
	{
		Set<Object> a = new HashSet<>();
		return (Class<Set<? extends Object>>) a.getClass();
	}

}
