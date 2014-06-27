package au.org.scoutmaster.domain.converter;

import au.org.scoutmaster.domain.Age;
import au.org.scoutmaster.domain.CommunicationLog;
import au.org.scoutmaster.domain.CommunicationType;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Period;
import au.org.scoutmaster.domain.Phone;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.SectionType;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.domain.accounting.Money;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.DefaultConverterFactory;

public class ScoutmasterConverterFactory extends DefaultConverterFactory
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> createConverter(
			final Class<PRESENTATION> presentationType, final Class<MODEL> modelType)
			{
		// Handle one particular type conversion
		if (CommunicationLog.class == modelType)
		{
			return (Converter<PRESENTATION, MODEL>) new ActivityConverter();
		}
		else if (CommunicationType.class == modelType)
		{
			return (Converter<PRESENTATION, MODEL>) new ActivityTypeConverter();
		}
		else if (Age.class == modelType)
		{
			return (Converter<PRESENTATION, MODEL>) new AgeConverter();
		}
		else if (Contact.class == modelType)
		{
			return (Converter<PRESENTATION, MODEL>) new ContactConverter();
		}
		else if (Money.class == modelType)
		{
			return (Converter<PRESENTATION, MODEL>) new MoneyConverter();
		}
		else if (Period.class == modelType)
		{
			return (Converter<PRESENTATION, MODEL>) new PeriodConverter();
		}
		else if (Phone.class == modelType)
		{
			return (Converter<PRESENTATION, MODEL>) new PhoneConverter();
		}
		else if (Raffle.class == modelType)
		{
			return (Converter<PRESENTATION, MODEL>) new EntityConverter<Raffle>();
		}
		else if (SectionType.class == modelType)
		{
			return (Converter<PRESENTATION, MODEL>) new SectionTypeConverter();
		}
		else if (Tag.class == modelType)
		{
			return (Converter<PRESENTATION, MODEL>) new TagConverter();
		}

		// Default to the supertype
		return super.createConverter(presentationType, modelType);
			}
}
