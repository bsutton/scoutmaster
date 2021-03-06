package au.org.scoutmaster.domain.converter;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.PhoneDao;
import au.org.scoutmaster.domain.Phone;

import com.vaadin.data.util.converter.Converter;

public class PhoneConverter implements Converter<Object, Phone>
{
	private static Logger logger = LogManager.getLogger(PhoneConverter.class);
	private static final long serialVersionUID = 1L;

	@Override
	public Phone convertToModel(final Object value, final Class<? extends Phone> targetType, final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		Phone result = null;
		final PhoneDao daoPhone = new DaoFactory().getPhoneDao();

		PhoneConverter.logger.debug("converToModel: value: {} valueType: {} targetType: {}", value,
				value != null ? value.getClass() : "null", targetType);

		if (value instanceof Long)
		{
			PhoneConverter.logger.debug("Calling findById");
			result = daoPhone.findById((Long) value);
		}
		else if (value instanceof Object || value instanceof String)
		{
			result = new Phone((String) value);
		}

		PhoneConverter.logger.debug("result: {}", result);
		return result;
	}

	@Override
	public Object convertToPresentation(final Phone value, final Class<? extends Object> targetType, final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		String result = "0";

		PhoneConverter.logger.debug("convertToPresentation: value: {} targetType: {}", value, targetType);
		if (value != null)
		{
			result = value.getPhoneNo();
		}
		else
		{
			result = "";
		}
		PhoneConverter.logger.debug("result: {}", result);
		return result;
	}

	@Override
	public Class<Phone> getModelType()
	{
		return Phone.class;
	}

	@Override
	public Class<Object> getPresentationType()
	{
		return Object.class;
	}

}
