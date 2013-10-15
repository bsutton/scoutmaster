package org.joda.time.contrib.eclipselink;

import java.util.Date;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;
import org.joda.time.DateTime;

public class DateTimeConverter implements Converter
{
	private static final long serialVersionUID = 1L;

	public Object convertDataValueToObjectValue(Object dataValue, Session arg1)
	{
		if (dataValue instanceof Date)
			return new DateTime(dataValue);
		else
			throw new IllegalStateException("Converstion exception, value is not of LocalDate type.");

	}

	public Object convertObjectValueToDataValue(Object objectValue, Session arg1)
	{
		if (objectValue instanceof DateTime)
		{
			return ((DateTime) objectValue).toDate();
		}
		else
			throw new IllegalStateException("Converstion exception, value is not of java.util.Date type.");

	}

	public void initialize(DatabaseMapping arg0, Session arg1)
	{
	}

	public boolean isMutable()
	{
		return false;
	}

}
