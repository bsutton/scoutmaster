package org.joda.time.contrib.eclipselink;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;
import org.joda.time.Duration;

/**
 * Persists org.joda.time.Duration using EclipseLink. Value is stored as a
 * varchar.
 * 
 * @author georgi.knox
 * 
 */
public class DurationConverter implements Converter
{
	private static final long serialVersionUID = 1L;

	public Object convertDataValueToObjectValue(Object dataValue, Session session)
	{
		if (dataValue instanceof String)
			return new Duration(dataValue);
		throw new IllegalStateException("Converstion exception, value is not of LocalDate type.");

	}

	public Object convertObjectValueToDataValue(Object objectValue, Session arg1)
	{

		if (objectValue instanceof Duration)
		{
			return ((Duration) objectValue).toString();
		}
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
