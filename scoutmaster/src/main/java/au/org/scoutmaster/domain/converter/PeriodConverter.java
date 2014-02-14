package au.org.scoutmaster.domain.converter;

import java.util.Locale;

import org.apache.log4j.Logger;

import au.org.scoutmaster.domain.Period;

import com.vaadin.data.util.converter.Converter;

public class PeriodConverter implements Converter<Object, Period>
{
	private static Logger logger = Logger.getLogger(PeriodConverter.class);
	private static final long serialVersionUID = 1L;

	@Override
	public Period convertToModel(Object value, Class<? extends Period> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		Period result = null;
		
		logger.debug("converToModel: value=" + value + "valueType:" + (value != null ? value.getClass() : "null") + " targetType:" + targetType);

		if (value instanceof Object || value instanceof String)
		{
			result = new Period((String)value);
		}

		logger.debug("result:" + result);
		return result;
	}

	@Override
	public Object convertToPresentation(Period value, Class<? extends Object> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		String result = "0";
		
		logger.debug("convertToPresentation: value" + (value == null ? "null" : value) + " targetType:" + targetType);
		if (value != null)
			result = value.toString();
		else
			result = "";
		logger.debug("result: " + result);
		return result;
	}

	@Override
	public Class<Period> getModelType()
	{
		return Period.class;
	}

	@Override
	public Class<Object> getPresentationType()
	{
		return Object.class;
	}

}
