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
	public Money convertToModel(final Object value, final Class<? extends Money> targetType, final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		Money result = null;

		MoneyConverter.logger.debug("converToModel: value: {} valueType: {} targetType: {}", value,
				value != null ? value.getClass() : "null", targetType);

		if (value instanceof Object || value instanceof String)
		{
			result = new Money((String) value);
		}

		MoneyConverter.logger.debug("result: {}", result);
		return result;
	}

	@Override
	public Object convertToPresentation(final Money value, final Class<? extends Object> targetType, final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		String result = "0";

		MoneyConverter.logger.debug("convertToPresentation: value: {}  targetType: {}", value == null ? "null" : value,
				targetType);
		if (value != null)
		{
			result = value.toString();
		}
		else
		{
			result = "";
		}
		MoneyConverter.logger.debug("result: {}", result);
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
