package au.org.scoutmaster.domain.converter;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.org.scoutmaster.domain.Age;

import com.vaadin.data.util.converter.Converter;

public class AgeConverter implements Converter<Object, Age>
{
	private static Logger logger = LogManager.getLogger(AgeConverter.class);
	private static final long serialVersionUID = 1L;

	@Override
	public Age convertToModel(final Object value, final Class<? extends Age> targetType, final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		Age result = null;

		AgeConverter.logger.debug("converToModel: value={} valueType: {} targetType: {}", value,
				value != null ? value.getClass() : "null", targetType);

		if (value instanceof Object || value instanceof String)
		{
			result = new Age((String) value);
		}

		AgeConverter.logger.debug("result: {}", result);
		return result;
	}

	@Override
	public Object convertToPresentation(final Age value, final Class<? extends Object> targetType, final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		String result = "0";

		AgeConverter.logger.debug("convertToPresentation: value {} targetType: {}", value, targetType);
		if (value != null)
		{
			result = value.toString();
		}
		else
		{
			result = "";
		}
		AgeConverter.logger.debug("result: {}", result);
		return result;
	}

	@Override
	public Class<Age> getModelType()
	{
		return Age.class;
	}

	@Override
	public Class<Object> getPresentationType()
	{
		return Object.class;
	}

}
