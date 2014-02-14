package au.org.scoutmaster.domain.converter;

import au.org.scoutmaster.domain.Activity;
import au.org.scoutmaster.domain.ActivityType;
import au.org.scoutmaster.domain.Age;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Period;
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
		else if (Tag.class == modelType)
			return (Converter<PRESENTATION, MODEL>) new TagConverter();
		else if (Phone.class == modelType)
			return (Converter<PRESENTATION, MODEL>) new PhoneConverter();
		else if (Activity.class == modelType)
			return (Converter<PRESENTATION, MODEL>) new ActivityConverter();
		else if (ActivityType.class == modelType)
			return (Converter<PRESENTATION, MODEL>) new ActivityTypeConverter();
		else if (Contact.class == modelType)
			return (Converter<PRESENTATION, MODEL>) new ContactConverter();
		else if (Age.class == modelType)
			return (Converter<PRESENTATION, MODEL>) new AgeConverter();
		else if (Period.class == modelType)
			return (Converter<PRESENTATION, MODEL>) new PeriodConverter();

		// Default to the supertype
		return super.createConverter(presentationType, modelType);
	}
}
