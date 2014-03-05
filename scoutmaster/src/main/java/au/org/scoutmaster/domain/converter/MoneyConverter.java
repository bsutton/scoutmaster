package au.org.scoutmaster.domain.converter;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.org.scoutmaster.domain.accounting.Money;

import com.vaadin.data.util.converter.Converter;

public class MoneyConverter implements Converter<Object, Money>
{
	private static Logger logger = LogManager.getLogger(MoneyConverter.class);
	private static final long serialVersionUID = 1L;

	@Override
	public Money convertToModel(Object value, Class<? extends Money> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		Money result = null;

		logger.debug("converToModel: value: {} valueType: {} targetType: {}", value, (value != null ? value.getClass()
				: "null"), targetType);

		if (value instanceof Object || value instanceof String)
		{
			result = new Money((String) value);
		}

		logger.debug("result: {}", result);
		return result;
	}

	@Override
	public Object convertToPresentation(Money value, Class<? extends Object> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		String result = "0";

		logger.debug("convertToPresentation: value: {}  targetType: {}", (value == null ? "null" : value), targetType);
		if (value != null)
			result = value.toString();
		else
			result = "";
		logger.debug("result: {}", result);
		return result;
	}

	@Override
	public Class<Money> getModelType()
	{
		return Money.class;
	}

	@Override
	public Class<Object> getPresentationType()
	{
		return Object.class;
	}

}
