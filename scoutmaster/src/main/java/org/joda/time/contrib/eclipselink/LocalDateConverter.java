package org.joda.time.contrib.eclipselink;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;
import org.joda.time.LocalDate;

/**
 * Persist LocalDate via EclipseLink
 * 
 * @author georgi.knox
 * 
 */
public class LocalDateConverter implements Converter
{
	private static final long serialVersionUID = 1L;

	public Object convertDataValueToObjectValue(Object dataValue, Session session)
	{
		if (dataValue instanceof Date)
			return new LocalDate(dataValue);
		throw new IllegalStateException("Converstion exception, value is not of LocalDate type.");
	}

	public Object convertObjectValueToDataValue(Object objectValue, Session arg1)
	{

		if (objectValue instanceof LocalDate)
		{
			LocalDate localDate = (LocalDate) objectValue;
			String dateString = localDate.toString();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try
			{
				return df.parse(dateString);
			}
			catch (ParseException e)
			{
				throw new IllegalStateException("Converstion exception, value is not of java.util.Date type.");
			}
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
