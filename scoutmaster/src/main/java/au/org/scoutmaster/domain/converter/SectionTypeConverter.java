package au.org.scoutmaster.domain.converter;

import java.util.Locale;

import au.org.scoutmaster.dao.SectionTypeDao;
import au.org.scoutmaster.domain.SectionType;

import com.vaadin.data.util.converter.Converter;

public class SectionTypeConverter implements Converter<Object, SectionType>
{
	private static final long serialVersionUID = 1L;

	@Override
	public SectionType convertToModel(Object value, Class<? extends SectionType> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		SectionType result = null;
		SectionTypeDao daoSectionType = new SectionTypeDao();

		if (value instanceof Long)
			result = daoSectionType.findById((Long) value);
		else if (value instanceof Object || value instanceof String)
			result = daoSectionType.findByName((String) value);

		return result;
	}

	@Override
	public Object convertToPresentation(SectionType value, Class<? extends Object> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{

		if (value != null)
			return value.getName();
		else
			return "0";
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
