package au.org.scoutmaster.domain.converter;

import java.util.Locale;

import org.apache.log4j.Logger;

import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.PhoneDao;
import au.org.scoutmaster.domain.Phone;

import com.vaadin.data.util.converter.Converter;

public class PhoneConverter implements Converter<Object, Phone>
{
	private static Logger logger = Logger.getLogger(PhoneConverter.class);
	private static final long serialVersionUID = 1L;

	@Override
	public Phone convertToModel(Object value, Class<? extends Phone> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		Phone result = null;
		PhoneDao daoPhone = new DaoFactory().getPhoneDao();
		
		logger.debug("converToModel: value=" + value + "valueType:" + (value != null ? value.getClass() : "null") + " targetType:" + targetType);

		if (value instanceof Long)
		{
			logger.debug("Calling findById");
			result = daoPhone.findById((Long) value);
		}
		else if (value instanceof Object || value instanceof String)
		{
			result = new Phone((String)value);
		}

		logger.debug("result:" + result);
		return result;
	}

	@Override
	public Object convertToPresentation(Phone value, Class<? extends Object> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		String result = "0";
		
		logger.debug("convertToPresentation: value" + value + " targetType:" + targetType);
		if (value != null)
			result = value.getPhoneNo();
		else
			result = "";
		logger.debug("result: " + result);
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
