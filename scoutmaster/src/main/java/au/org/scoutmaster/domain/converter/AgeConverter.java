package au.org.scoutmaster.domain.converter;

import java.util.Locale;

import org.apache.log4j.Logger;

import au.org.scoutmaster.domain.Age;

import com.vaadin.data.util.converter.Converter;

public class AgeConverter implements Converter<Object, Age>
{
	private static Logger logger = Logger.getLogger(AgeConverter.class);
	private static final long serialVersionUID = 1L;

	@Override
	public Age convertToModel(Object value, Class<? extends Age> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		Age result = null;
		
		logger.debug("converToModel: value=" + value + "valueType:" + (value != null ? value.getClass() : "null") + " targetType:" + targetType);

		if (value instanceof Object || value instanceof String)
		{
			result = new Age((String)value);
		}

		logger.debug("result:" + result);
		return result;
	}

	@Override
	public Object convertToPresentation(Age value, Class<? extends Object> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		String result = "0";
		
		logger.debug("convertToPresentation: value" + value + " targetType:" + targetType);
		if (value != null)
			result = value.toString();
		else
			result = "";
		logger.debug("result: " + result);
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
