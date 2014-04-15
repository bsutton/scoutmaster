package org.joda.time.contrib.eclipselink;

import java.util.Date;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;
import org.joda.time.DateTime;

public class DateTimeConverter implements Converter
{
	private static final long serialVersionUID = 1L;

	@Override
	public Object convertDataValueToObjectValue(final Object dataValue, final Session arg1)
	{
		if (dataValue instanceof Date)
		{
			return new DateTime(dataValue);
		}
		else
		{
			throw new IllegalStateException("Converstion exception, value is not of LocalDate type.");
		}

	}

	@Override
	public Object convertObjectValueToDataValue(final Object objectValue, final Session arg1)
	{
		if (objectValue instanceof DateTime)
		{
			return ((DateTime) objectValue).toDate();
		}
		else
		{
			throw new IllegalStateException("Converstion exception, value is not of java.util.Date type.");
		}

	}

	@Override
	public void initialize(final DatabaseMapping arg0, final Session arg1)
	{
	}

	@Override
	public boolean isMutable()
	{
		return false;
	}

}
