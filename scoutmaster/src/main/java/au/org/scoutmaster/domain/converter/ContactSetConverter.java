package au.org.scoutmaster.domain.converter;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;

import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.domain.Contact;

import com.vaadin.data.util.converter.Converter;

public class ContactSetConverter implements Converter<Set<? extends Object>, Set<Contact>>
{
	private static Logger logger = Logger.getLogger(ContactSetConverter.class);
	private static final long serialVersionUID = 1L;

	@Override
	public Set<Contact> convertToModel(Set<? extends Object> value, Class<? extends Set<Contact>> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		HashSet<Contact> result = null;
		ContactDao daoContact = new ContactDao();

		logger.debug("converToModel: value=" + value + "valueType:" + (value != null ? value.getClass() : "null")
				+ " targetType:" + targetType);

		if (value instanceof Set)
		{
			logger.debug("Calling findByName");
			@SuppressWarnings("unchecked")
			HashSet<Object> set = (HashSet<Object>) value;
			result = new HashSet<Contact>();
			for (Object item : set.toArray())
			{
				// Now we have to determine if the element is an identity or an
				// actual Contact
				if (item instanceof Long)
					result.add(daoContact.findById((Long) item));
				else
					result.add((Contact) item);
			}
		}

		logger.debug("result:" + result);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<? extends Object> convertToPresentation(Set<Contact> value,
			Class<? extends Set<? extends Object>> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		Set<? extends Object> result = new HashSet<>();
		if (targetType.getName().equals("java.util.Set"))
		{
			result = new HashSet<Contact>();

			logger.debug("convertToPresentation: value" + value + " targetType:" + targetType);
			if (value != null)
				((HashSet<Contact>) result).addAll(value);
		}
		else
			throw new UnsupportedOperationException("Conversion for Contact from type " + targetType + " not supported");

		logger.debug("result: " + result.toString());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Set<Contact>> getModelType()
	{
		Set<Contact> a = new HashSet<>();
		return (Class<Set<Contact>>) a.getClass();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Set<? extends Object>> getPresentationType()
	{
		Set<Object> a = new HashSet<>();
		return (Class<Set<? extends Object>>) a.getClass();
	}

}
