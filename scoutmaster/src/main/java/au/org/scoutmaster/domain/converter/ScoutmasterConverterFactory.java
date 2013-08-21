package au.org.scoutmaster.domain.converter;

import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.SectionType;
import au.org.scoutmaster.domain.Tag;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.DefaultConverterFactory;

public class ScoutmasterConverterFactory extends DefaultConverterFactory
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> createConverter(Class<PRESENTATION> presentationType,
			Class<MODEL> modelType)
	{
		// Handle one particular type conversion
		if (SectionType.class == modelType)
			return (Converter<PRESENTATION, MODEL>) new SectionTypeConverter();
		if (Tag.class == modelType)
			return (Converter<PRESENTATION, MODEL>) new TagConverter();
		if (Phone.class == modelType)
			return (Converter<PRESENTATION, MODEL>) new PhoneConverter();

		// Default to the supertype
		return super.createConverter(presentationType, modelType);
	}
}
