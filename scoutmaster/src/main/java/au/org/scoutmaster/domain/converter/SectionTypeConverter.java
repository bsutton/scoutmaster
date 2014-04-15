package au.org.scoutmaster.domain.converter;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.org.scoutmaster.dao.SectionTypeDao;
import au.org.scoutmaster.domain.SectionType;

import com.vaadin.data.util.converter.Converter;

public class SectionTypeConverter implements Converter<Object, SectionType>
{
	private static Logger logger = LogManager.getLogger(SectionTypeConverter.class);
	private static final long serialVersionUID = 1L;

	@Override
	public SectionType convertToModel(final Object value, final Class<? extends SectionType> targetType,
			final Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		SectionType result = null;
		final SectionTypeDao daoSectionType = new SectionTypeDao();

		SectionTypeConverter.logger.debug("converToModel: value: {} valueType: targetType: {}", value,
				value != null ? value.getClass() : "null", targetType);

		if (value instanceof Long)
		{
			SectionTypeConverter.logger.debug("Calling findById");
			result = daoSectionType.findById((Long) value);
		}
		else if (value instanceof Object || value instanceof String)
		{
			SectionTypeConverter.logger.debug("Calling findByName");
			result = daoSectionType.findByName((String) value);
		}

		SectionTypeConverter.logger.debug("result: {}", result);
		return result;
	}

	@Override
	public Object convertToPresentation(final SectionType value, final Class<? extends Object> targetType,
			final Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		String result = "0";

		SectionTypeConverter.logger.debug("convertToPresentation: value: {} targetType: {}", value, targetType);
		if (value != null)
		{
			result = value.getName();
		}
		else
		{
			result = "";
		}
		SectionTypeConverter.logger.debug("result: {}", result);
		return result;
	}

	@Override
	public Class<SectionType> getModelType()
	{
		return SectionType.class;
	}

	@Override
	public Class<Object> getPresentationType()
	{
		return Object.class;
	}

}
