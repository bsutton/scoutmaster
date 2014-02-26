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
	public SectionType convertToModel(Object value, Class<? extends SectionType> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		SectionType result = null;
		SectionTypeDao daoSectionType = new SectionTypeDao();

		logger.debug("converToModel: value: {} valueType: targetType: {}", value, (value != null ? value.getClass()
				: "null"), targetType);

		if (value instanceof Long)
		{
			logger.debug("Calling findById");
			result = daoSectionType.findById((Long) value);
		}
		else if (value instanceof Object || value instanceof String)
		{
			logger.debug("Calling findByName");
			result = daoSectionType.findByName((String) value);
		}

		logger.debug("result: {}", result);
		return result;
	}

	@Override
	public Object convertToPresentation(SectionType value, Class<? extends Object> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		String result = "0";

		logger.debug("convertToPresentation: value: {} targetType: {}", value, targetType);
		if (value != null)
			result = value.getName();
		else
			result = "";
		logger.debug("result: {}", result);
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
